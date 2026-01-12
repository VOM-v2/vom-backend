package okodee.vom.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import okodee.vom.domain.auth.dto.SignupRequest;
import okodee.vom.domain.user.dto.UserDto;
import okodee.vom.global.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "AUTH", description = "인증 관련 API")
public interface AuthApi {

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserDto.class),
                examples = @ExampleObject(
                    name = "회원가입 성공 예시",
                    value = """
                        {
                            "id": "d73e0b9a-9607-4fd4-a52d-030a96413322",
                            "createdAt": "2026-01-08T16:35:52.960Z",
                            "email": "test@email.com",
                            "nickname": "test",
                            "role": "USER",
                            "locked": false
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "이메일 중복",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "이메일 중복 에러",
                    value = """
                        {
                            "code": "DUPLICATE_EMAIL",
                            "details": {
                                "email": "test@email.com"
                            },
                            "exceptionType": "DuplicateEmailException",
                            "message": "이미 가입된 이메일입니다.",
                            "status": 409,
                            "timestamp": "2026-01-08T16:33:06.081Z"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (Validation 실패)",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Validation 에러",
                    value = """
                        {
                            "code": "VALIDATION_FAILED",
                            "message": "입력값이 올바르지 않습니다.",
                            "status": 400,
                            "timestamp": "2026-01-08T16:33:06.081Z",
                            "details": {
                                "email": "이메일 형식이 올바르지 않습니다."
                            }
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<UserDto> signup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 정보",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SignupRequest.class),
                examples = @ExampleObject(
                    name = "회원가입 요청 예시",
                    value = """
                        {
                            "email": "test@email.com",
                            "password": "password123!",
                            "nickname": "테스트유저"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody SignupRequest signupRequest
    );

    @Operation(
        summary = "CSRF 토큰 발급",
        description = "클라이언트가 CSRF 토큰을 요청하면 쿠키로 XSRF-TOKEN을 발급합니다. " +
            "발급된 토큰은 이후 POST, PUT, DELETE 요청 시 X-XSRF-TOKEN 헤더에 포함해야 합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "CSRF 토큰이 쿠키로 설정되었습니다.",
            headers = @Header(
                name = "Set-Cookie",
                description = "XSRF-TOKEN 쿠키",
                schema = @Schema(type = "string", example = "XSRF-TOKEN=b05b8d29-dbb7-4dac-8466-5b81d79be8f1; Path=/")
            )
        )
    })
    ResponseEntity<Void> getCsrfToken(
        @Parameter(hidden = true) CsrfToken csrfToken
    );
}
