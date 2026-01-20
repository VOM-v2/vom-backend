package okodee.vom.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Profile", description = "프로필 관리 API")
public interface ProfileApi {
    @Operation(
        summary = "프로필 조회",
        description = "사용자 ID로 프로필 정보를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로필 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProfileDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "사용자 없음",
                    value = """
                    {
                        "code": "USER_NOT_FOUND",
                        "message": "사용자를 찾을 수 없습니다.",
                        "details": {
                            "userId": "550e8400-e29b-41d4-a716-446655440000"
                        },
                        "path": "/api/users/550e8400-e29b-41d4-a716-446655440000/profiles"
                    }
                    """
                )
            )
        )
    })
    ResponseEntity<ProfileDto> find(
        @Parameter(
            description = "조회할 사용자 ID",
            required = true,
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable("userId") UUID userId
    );
}
