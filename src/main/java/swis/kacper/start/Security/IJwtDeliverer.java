package swis.kacper.start.Security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IJwtDeliverer {

    String generateToken(UserDetail userDetail);
    Authentication getAuthentication(HttpServletRequest request);
    boolean validateToken(HttpServletRequest request);

}
