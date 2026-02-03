package okodee.vom.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import okodee.vom.domain.profile.dto.KeywordUpdateRequest;
import okodee.vom.domain.profile.dto.UserKeywordResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@Tag(name = "Keyword", description = "관심 키워드 API")
@SecurityRequirement(name = "bearer-jwt")
public interface KeywordApi {
    @Operation(
        summary = "내 관심 키워드 조회",
        description = "현재 로그인한 사용자의 관심 키워드 목록을 조회합니다. (최대 5개)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserKeywordResponse.class))
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음 (본인의 키워드만 조회 가능)"
        )
    })
    ResponseEntity<List<UserKeywordResponse>> getMyKeywords(Authentication userId);

    @Operation(
        summary = "내 관심 키워드 수정",
        description = """
            현재 로그인한 사용자의 관심 키워드를 수정합니다.
            
            - 기존 키워드를 모두 삭제하고 새로운 키워드로 교체합니다.
            - 최대 5개까지 설정할 수 있습니다.
            - 존재하지 않는 키워드 ID는 오류를 반환합니다.
            - 빈 배열을 보내면 모든 키워드가 삭제됩니다.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserKeywordResponse.class)),
                examples = @ExampleObject(
                    name = "수정 성공",
                    value = """
                    [
                        {
                            "id": "123e4567-e89b-12d3-a456-426614174000",
                            "keyword": {
                                "id": 1,
                                "name": "게임",
                                "category": "DIGITAL",
                                "categoryDescription": "디지털",
                                "displayOrder": 1
                            },
                            "createdAt": "2024-12-19T10:30:00+09:00"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "최대 개수 초과",
                        value = """
                        {
                            "code": "INVALID_INPUT",
                            "message": "관심 키워드는 최대 5개까지 설정할 수 있습니다.",
                            "path": "/api/keywords/me"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "존재하지 않는 키워드",
                        value = """
                        {
                            "code": "KEYWORD_NOT_FOUND",
                            "message": "존재하지 않는 키워드입니다.",
                            "details": {
                                "keywordId": 999
                            },
                            "path": "/api/keywords/me"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패"
        )
    })
    ResponseEntity<List<UserKeywordResponse>> updateMyKeywords(
        Authentication authentication,
        @Valid KeywordUpdateRequest request
    );
}
