package okodee.vom.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.profile.dto.ProfileUpdateRequest;
import okodee.vom.global.exception.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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

    @Operation(
        summary = "사용자 프로필 수정",
        description = """
            사용자 프로필 정보를 수정합니다.
            
            - 이름, 성별, 생년월일을 수정할 수 있습니다.
            - 프로필 이미지는 선택적으로 업로드할 수 있습니다.
            - 본인의 프로필만 수정 가능합니다. (인증 필요)
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로필 수정 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProfileDto.class),
                examples = @ExampleObject(
                    name = "수정 성공",
                    value = """
                    {
                        "userId": "550e8400-e29b-41d4-a716-446655440000",
                        "name": "김철수",
                        "gender": "MALE",
                        "birthDate": "1995-03-15",
                        "profileImageUrl": "https://vom.s3.ap-northeast-2.amazonaws.com/profiles/abc123.jpg"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효성 검증 실패)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "유효성 검증 실패",
                    value = """
                    {
                        "code": "INVALID_INPUT",
                        "message": "입력값이 올바르지 않습니다.",
                        "details": {
                            "name": "이름은 2자 이상 20자 이하여야 합니다."
                        },
                        "path": "/api/v1/users/550e8400-e29b-41d4-a716-446655440000/profiles"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음 (본인 프로필이 아님)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "권한 없음",
                    value = """
                    {
                        "code": "ACCESS_DENIED",
                        "message": "접근 권한이 없습니다.",
                        "details": {
                            "reason": "본인의 프로필만 수정할 수 있습니다."
                        },
                        "path": "/api/v1/users/550e8400-e29b-41d4-a716-446655440000/profiles"
                    }
                    """
                )
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
                        "path": "/api/v1/users/550e8400-e29b-41d4-a716-446655440000/profiles"
                    }
                    """
                )
            )
        )
    })
    ResponseEntity<ProfileDto> update(
        @Parameter(
            description = "수정할 사용자 ID",
            required = true,
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        @PathVariable("userId") UUID userId,

        @Parameter(
            description = "프로필 수정 정보 (JSON)",
            required = true
        )
        @RequestPart("request") @Valid ProfileUpdateRequest request,

        @Parameter(
            description = "프로필 이미지 파일 (선택사항, JPG/PNG)",
            required = false
        )
        @RequestPart(value = "image", required = false) MultipartFile image
    );
}
