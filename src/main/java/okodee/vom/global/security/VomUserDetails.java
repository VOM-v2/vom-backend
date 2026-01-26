package okodee.vom.global.security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import okodee.vom.domain.user.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode(of = "userDto")
@Getter
public class VomUserDetails implements UserDetails {

    private final UserDto userDto;
    private final String password;

    public VomUserDetails(UserDto userDto, String password) {
        this.userDto = userDto;
        this.password = password;
    }

    /**
     * @PreAuthorize에서 principal.id로 접근하기 위한 메서드
     *
     * 사용 예시: @PreAuthorize("principal.id == #userId")
     */
    public UUID getId() {
        return userDto.id();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userDto.role().name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userDto.email();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !userDto.locked();
    }
}
