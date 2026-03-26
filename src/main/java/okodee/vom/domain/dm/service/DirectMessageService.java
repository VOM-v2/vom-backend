package okodee.vom.domain.dm.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.dm.dto.DirectMessageResponse;
import okodee.vom.domain.dm.dto.DirectMessageRoomCreateRequest;
import okodee.vom.domain.dm.dto.DirectMessageRoomListResponse;
import okodee.vom.domain.dm.dto.DirectMessageRoomResponse;
import okodee.vom.domain.dm.dto.DirectMessageSendRequest;
import okodee.vom.domain.dm.dto.DirectMessageSendResult;
import okodee.vom.domain.dm.entity.DirectMessage;
import okodee.vom.domain.dm.entity.DirectMessageRoom;
import okodee.vom.domain.dm.exception.DMRoomAlreadyExistsException;
import okodee.vom.domain.dm.exception.DMRoomNotFoundException;
import okodee.vom.domain.dm.exception.DMSelfChatNotAllowedException;
import okodee.vom.domain.dm.exception.DMUnauthorizedException;
import okodee.vom.domain.dm.mapper.DirectMessageMapper;
import okodee.vom.domain.dm.mapper.DirectMessageRoomMapper;
import okodee.vom.domain.dm.repository.DirectMessageRepository;
import okodee.vom.domain.dm.repository.DirectMessageRoomRepository;
import okodee.vom.domain.user.entity.User;
import okodee.vom.domain.user.exception.UserNotFoundException;
import okodee.vom.domain.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRoomRepository roomRepository;
    private final DirectMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DirectMessageRoomMapper roomMapper;
    private final DirectMessageMapper messageMapper;

    @Transactional
    public DirectMessageRoomResponse createRoom(UUID currentUserId, DirectMessageRoomCreateRequest request) {
        log.debug("DM 방 생성 시작: currentUserId={}, receiverId={}", currentUserId, request.receiverId());

        if (currentUserId.equals(request.receiverId())) {
            throw new DMSelfChatNotAllowedException();
        }

        roomRepository.findBySenderIdAndReceiverId(currentUserId, request.receiverId())
            .or(() -> roomRepository.findBySenderIdAndReceiverId(request.receiverId(), currentUserId))
            .ifPresent(room -> {
                throw new DMRoomAlreadyExistsException();
            });

        User sender = userRepository.findById(currentUserId)
            .orElseThrow(() -> UserNotFoundException.withId(currentUserId));

        User receiver = userRepository.findById(request.receiverId())
            .orElseThrow(() -> UserNotFoundException.withId(request.receiverId()));

        DirectMessageRoom room = DirectMessageRoom.create(sender, receiver);
        try {
            roomRepository.save(room);
        } catch (DataIntegrityViolationException e) {
            throw new DMRoomAlreadyExistsException();
        }

        log.info("DM 방 생성 완료: roomId={}, senderId={}, receiverId={}", room.getId(), currentUserId, request.receiverId());
        return roomMapper.toResponse(room);
    }

    @Transactional(readOnly = true)
    public List<DirectMessageRoomListResponse> getRooms(UUID currentUserId) {
        log.debug("DM 방 목록 조회 시작: currentUserId={}", currentUserId);

        List<DirectMessageRoom> rooms = roomRepository.findAllBySenderIdOrReceiverId(currentUserId, currentUserId);

        List<UUID> roomIds = rooms.stream()
            .map(DirectMessageRoom::getId)
            .toList();

        Map<UUID, Long> unreadCountMap = messageRepository
            .countUnreadByRoomIds(currentUserId, roomIds)
            .stream()
            .collect(Collectors.toMap(
                row -> (UUID) row[0],
                row -> (Long) row[1]
            ));

        List<DirectMessageRoomListResponse> response = rooms.stream()
            .map(room -> {
                long unreadCount = unreadCountMap.getOrDefault(room.getId(), 0L);
                return roomMapper.toListResponse(room, currentUserId, unreadCount);
            })
            .toList();

        log.debug("DM 방 목록 조회 완료: currentUserId={}, roomCount={}", currentUserId, response.size());
        return response;
    }

    @Transactional(readOnly = true)
    public Page<DirectMessageResponse> getMessages(UUID currentUserId, UUID roomId, Pageable pageable) {
        log.debug("메시지 내역 조회 시작: currentUserId={}, roomId={}", currentUserId, roomId);

        DirectMessageRoom room = roomRepository.findById(roomId)
            .orElseThrow(() -> new DMRoomNotFoundException());

        boolean isParticipant = room.getSender().getId().equals(currentUserId)
            || room.getReceiver().getId().equals(currentUserId);

        if (!isParticipant) {
            throw new DMUnauthorizedException();
        }

        Page<DirectMessageResponse> response = messageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable)
            .map(messageMapper::toResponse);

        log.debug("메시지 내역 조회 완료: roomId={}, messageCount={}", roomId, response.getTotalElements());
        return response;
    }

    @Transactional
    public void markAsRead(UUID currentUserId, UUID roomId) {
        log.debug("읽음 처리 시작: currentUserId={}, roomId={}", currentUserId, roomId);

        DirectMessageRoom room = roomRepository.findById(roomId)
            .orElseThrow(() -> new DMRoomNotFoundException());

        boolean isParticipant = room.getSender().getId().equals(currentUserId)
            || room.getReceiver().getId().equals(currentUserId);

        if (!isParticipant) {
            throw new DMUnauthorizedException();
        }

        messageRepository.markAllAsReadByRoomIdAndSenderIdNot(roomId, currentUserId);

        log.info("읽음 처리 완료: currentUserId={}, roomId={}", currentUserId, roomId);
    }

    @Transactional
    public DirectMessageSendResult sendMessage(UUID currentUserId, UUID roomId, DirectMessageSendRequest request) {
        log.debug("메시지 전송 시작: currentUserId={}, roomId={}", currentUserId, roomId);

        DirectMessageRoom room = roomRepository.findById(roomId)
            .orElseThrow(() -> new DMRoomNotFoundException());

        boolean isParticipant = room.getSender().getId().equals(currentUserId)
            || room.getReceiver().getId().equals(currentUserId);

        if (!isParticipant) {
            throw new DMUnauthorizedException();
        }

        User sender = userRepository.findById(currentUserId)
            .orElseThrow(() -> UserNotFoundException.withId(currentUserId));

        DirectMessage message = DirectMessage.create(room, sender, request.content());
        messageRepository.save(message);

        boolean isSender = room.getSender().getId().equals(currentUserId);
        UUID receiverId = isSender ? room.getReceiver().getId() : room.getSender().getId();
        String receiverEmail = isSender ? room.getReceiver().getEmail() : room.getSender().getEmail();

        log.info("메시지 전송 완료: messageId={}, roomId={}, senderId={}", message.getId(), roomId, currentUserId);
        return new DirectMessageSendResult(
            messageMapper.toResponse(message),
            messageMapper.toNotificationResponse(message),
            receiverId,
            receiverEmail
        );
    }
}
