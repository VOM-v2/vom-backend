package okodee.vom.domain.dm.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

        return roomMapper.toResponse(room);
    }

    @Transactional(readOnly = true)
    public List<DirectMessageRoomListResponse> getRooms(UUID currentUserId) {

        List<DirectMessageRoom> rooms = roomRepository.findAllBySenderIdOrReceiverId(currentUserId, currentUserId);

        return rooms.stream()
            .map(room -> {
                long unreadCount = messageRepository.countByRoomIdAndSenderIdNotAndIsReadFalse(
                    room.getId(), currentUserId);
                return roomMapper.toListResponse(room, currentUserId, unreadCount);
            })
            .toList();
    }

    @Transactional(readOnly = true)
    public Page<DirectMessageResponse> getMessages(UUID currentUserId, UUID roomId, Pageable pageable) {

        DirectMessageRoom room = roomRepository.findById(roomId)
            .orElseThrow(() -> new DMRoomNotFoundException());

        boolean isParticipant = room.getSender().getId().equals(currentUserId)
            || room.getReceiver().getId().equals(currentUserId);

        if (!isParticipant) {
            throw new DMUnauthorizedException();
        }

        return messageRepository.findByRoomIdOrderByCreatedAtAsc(roomId, pageable)
            .map(messageMapper::toResponse);
    }

    @Transactional
    public void markAsRead(UUID currentUserId, UUID roomId) {

        DirectMessageRoom room = roomRepository.findById(roomId)
            .orElseThrow(() -> new DMRoomNotFoundException());

        boolean isParticipant = room.getSender().getId().equals(currentUserId)
            || room.getReceiver().getId().equals(currentUserId);

        if (!isParticipant) {
            throw new DMUnauthorizedException();
        }

        // 상대방이 보낸 메시지 중 안 읽은 것만 읽음 처리
        List<DirectMessage> unreadMessages = messageRepository
            .findAllByRoomIdAndSenderIdNotAndIsReadFalse(roomId, currentUserId);

        unreadMessages.forEach(DirectMessage::markAsRead);
    }

    @Transactional
    public DirectMessageSendResult sendMessage(UUID currentUserId, UUID roomId, DirectMessageSendRequest request) {

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

        // 상대방 ID 계산
        UUID receiverId = room.getSender().getId().equals(currentUserId)
            ? room.getReceiver().getId()
            : room.getSender().getId();

        return new DirectMessageSendResult(
            messageMapper.toResponse(message),
            messageMapper.toNotificationResponse(message),
            receiverId
        );
    }
}
