package okodee.vom.domain.dm.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import okodee.vom.domain.dm.dto.DirectMessageResponse;
import okodee.vom.domain.dm.dto.DirectMessageRoomCreateRequest;
import okodee.vom.domain.dm.dto.DirectMessageRoomListResponse;
import okodee.vom.domain.dm.dto.DirectMessageRoomResponse;
import okodee.vom.domain.dm.service.DirectMessageService;
import okodee.vom.global.security.VomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/direct-messages")
@RequiredArgsConstructor
public class DirectMessageController {

    private final DirectMessageService directMessageService;

    @PostMapping
    public ResponseEntity<DirectMessageRoomResponse> createRoom(
        Authentication authentication,
        @RequestBody @Valid DirectMessageRoomCreateRequest request) {

        UUID currentUserId = extractUserIdFromAuthentication(authentication);
        DirectMessageRoomResponse response = directMessageService.createRoom(currentUserId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DirectMessageRoomListResponse>> getRooms(
        Authentication authentication) {

        UUID currentUserId = extractUserIdFromAuthentication(authentication);
        List<DirectMessageRoomListResponse> response = directMessageService.getRooms(currentUserId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<DirectMessageResponse>> getMessages(
        Authentication authentication,
        @PathVariable UUID roomId) {

        UUID currentUserId = extractUserIdFromAuthentication(authentication);
        List<DirectMessageResponse> response = directMessageService.getMessages(currentUserId, roomId);

        return ResponseEntity.ok(response);
    }

    private UUID extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof VomUserDetails vomUserDetails) {
            return vomUserDetails.getId();
        }

        throw new IllegalStateException("지원하지 않는 Principal 타입입니다: " + principal.getClass());
    }
}
