package swis.kacper.start.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swis.kacper.start.Utilities.SecurityUtilities;
import swis.kacper.start.model.User;
import swis.kacper.start.service.IUserService;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashSet;
import java.util.Set;
@Service
public class   UserDetailService implements UserDetailsService {

    @Autowired
    private IUserService userService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //https://stackoverflow.com/questions/19525380/difference-between-role-and-grantedauthority-in-spring-security
        User user=userService.findByEmail(email)  .orElseThrow(() ->
                new IllegalArgumentException("Email not found in database"));
        Set<GrantedAuthority> authorities = Set.of(SecurityUtilities.convertToAuthority(user.getRole().name()));

        return UserDetail.builder()
                .user(user).number(user.getNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
