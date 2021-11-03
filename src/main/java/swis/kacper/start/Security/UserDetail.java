package swis.kacper.start.Security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import swis.kacper.start.Utilities.SecurityUtilities;
import swis.kacper.start.model.Role;
import swis.kacper.start.model.User;

import java.util.Collection;
import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail implements org.springframework.security.core.userdetails.UserDetails {
    //Here UserDetails is container for core user information.
    // According to docs, its implementations are not used directly by Spring Security for security purposes.
    // They simply store user information which is later encapsulated into Authentication objects.

    private Integer number;
    private String email;
    transient private String password; //don't show up on an searialized places
    transient private User user; //user for only login operation, don't use in JWT.
    private Set<GrantedAuthority> authorities;


    public static UserDetail createSuperUser()
    {
        Set<GrantedAuthority> authorities = Set.of(SecurityUtilities.convertToAuthority(Role.SYSTEM_MANAGER.name()));

        return UserDetail.builder()
                .number(-1)
                .email("system-administrator")
                .authorities(authorities)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
