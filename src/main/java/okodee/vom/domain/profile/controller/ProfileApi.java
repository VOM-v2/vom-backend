package okodee.vom.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.profile.dto.ProfileUpdateRequest;
import okodee.vom.global.exception.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Profile", description = "프로필 관리 API")
@SecurityRequirement(name = "bearer-jwt")
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

    // ==================== 신규 엔드포인트 (/me) ====================

    @Operation(
        summary = "내 프로필 수정",
        description = """
            현재 로그인한 사용자의 프로필 정보를 수정합니다.
            
            - 이름, 성별, 생년월일을 수정할 수 있습니다.
            - 프로필 이미지는 선택적으로 업로드할 수 있습니다.
            - JWT 토큰으로 자동 인증되어 본인 프로필만 수정됩니다.
            
            ⭐ v2.0부터 권장되는 엔드포인트입니다.
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
                        "path": "/api/v1/profiles/me"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패 (로그인 필요)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
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
                        "path": "/api/v1/profiles/me"
                    }
                    """
                )
            )
        )
    })
    ResponseEntity<ProfileDto> updateMyProfile(
        Authentication authentication,

        @Parameter(
            description = "프로필 수정 정보 (JSON)",
            required = true
        )
        @Valid ProfileUpdateRequest request,

        @Parameter(
            description = "프로필 이미지 파일 (선택사항, JPG/PNG)",
            required = false
        )
        MultipartFile image
    );

    // ==================== 레거시 엔드포인트 (/users/{userId}/profiles) ====================

    @Operation(
        summary = "[DEPRECATED] 사용자 프로필 수정",
        description = """
            ⚠️ 이 API는 v2.0부터 deprecated 되었습니다.
            
            **대신 사용:** `PATCH /api/v1/profiles/me`
            
            사용자 프로필 정보를 수정합니다.
            
            - 이름, 성별, 생년월일을 수정할 수 있습니다.
            - 프로필 이미지는 선택적으로 업로드할 수 있습니다.
            - 본인의 프로필만 수정 가능합니다. (인증 필요)
            
            **종료 예정일:** 2026-03-01
            """,
        deprecated = true
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
    ResponseEntity<ProfileDto> updateProfile(
        @Parameter(
            description = "수정할 사용자 ID",
            required = true,
            example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID userId,

        @Parameter(
            description = "프로필 수정 정보 (JSON)",
            required = true
        )
        @Valid ProfileUpdateRequest request,

        @Parameter(
            description = "프로필 이미지 파일 (선택사항, JPG/PNG)",
            required = false
        )
        MultipartFile image,

        Authentication authentication
    );
}
