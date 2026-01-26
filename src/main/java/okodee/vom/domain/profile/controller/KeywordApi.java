package okodee.vom.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserKeywordResponse.class))
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한 없음 (본인의 키워드만 조회 가능)"
        )
    })
    ResponseEntity<List<UserKeywordResponse>> getMyKeywords(Authentication userId);
}
