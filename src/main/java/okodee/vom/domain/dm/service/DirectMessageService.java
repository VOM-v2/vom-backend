package okodee.vom.domain.dm.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import okodee.vom.domain.dm.dto.DirectMessageRoomCreateRequest;
import okodee.vom.domain.dm.dto.DirectMessageRoomResponse;
import okodee.vom.domain.dm.entity.DirectMessageRoom;
import okodee.vom.domain.dm.exception.DMRoomAlreadyExistsException;
import okodee.vom.domain.dm.mapper.DirectMessageRoomMapper;
import okodee.vom.domain.dm.repository.DirectMessageRoomRepository;
import okodee.vom.domain.user.entity.User;
import okodee.vom.domain.user.exception.UserNotFoundException;
import okodee.vom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRoomRepository roomRepository;
    private final UserRepository userRepository;
    private final DirectMessageRoomMapper roomMapper;

    @Transactional
    public DirectMessageRoomResponse createRoom(UUID currentUserId, DirectMessageRoomCreateRequest request) {

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
        roomRepository.save(room);

        return roomMapper.toResponse(room);
    }
}
