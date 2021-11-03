package swis.kacper.start.Security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import swis.kacper.start.Utilities.SecurityUtilities;
import swis.kacper.start.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//Simple Logging Facade for Java (abbreviated SLF4J) – acts as a facade for different logging frameworks
// (e.g. java.util.logging, logback, Log4j).
// It offers a generic API making the logging independent of the actual implementation

public class InternalApiAuthenticationFilter extends OncePerRequestFilter {

    private final String accessKey;

    public InternalApiAuthenticationFilter(String accessKey)
    {
        this.accessKey = accessKey;
    }


    @Autowired
    private IJwtDeliverer jwtDeliverer;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        return !request.getRequestURI().startsWith("/user/users");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {//https://stackoverflow.com/questions/26340275/where-to-save-a-jwt-in-a-browser-based-application-and-how-to-use-it
        try
        {
            String requestKey = SecurityUtilities.extractAuthTokenFromRequest(request);

            System.out.println("rrddd      "+requestKey);
            System.out.println("accKey "+accessKey);
            if (requestKey == null || !requestKey.equals(accessKey))
            {
                log.warn("Internal key endpoint requested without/wrong key uri: {}", request.getRequestURI());
                throw new RuntimeException("UNAUTHORIZEDddd");
            }

            UserDetail user = UserDetail.createSuperUser();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception ex)
        {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
