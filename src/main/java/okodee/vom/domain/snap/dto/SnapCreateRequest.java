package okodee.vom.domain.snap.dto;

import jakarta.validation.constraints.Size;

public record SnapCreateRequest(
    @Size(max = 20, message = "content는 최대 20자까지 가능합니다.")
    String content
) {

}
