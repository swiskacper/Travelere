package swis.kacper.start.service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import swis.kacper.start.Security.IJwtDeliverer;
import swis.kacper.start.Security.UserDetail;
import swis.kacper.start.model.User;

@Service
public class AuthenticationService implements IAuthenticationService{
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private IJwtDeliverer jwtDeliverer;
    @Override
    public User signInAndReturnJWT(User signInRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
        );

        UserDetail userDetail= (UserDetail) authentication.getPrincipal();
        String jwt=jwtDeliverer.generateToken(userDetail);

        User signInUser=userDetail.getUser();
        signInUser.setToken(jwt);

        return signInUser;
    }

}
