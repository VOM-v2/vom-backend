package okodee.vom.domain.dm.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import okodee.vom.domain.dm.dto.DirectMessageRoomCreateRequest;
import okodee.vom.domain.dm.dto.DirectMessageRoomResponse;
import okodee.vom.domain.dm.service.DirectMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid DirectMessageRoomCreateRequest request) {

        UUID currentUserId = UUID.fromString(userDetails.getUsername());
        DirectMessageRoomResponse response = directMessageService.createRoom(currentUserId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
