package swis.kacper.start.service;

import swis.kacper.start.model.User;

public interface IAuthenticationService {
    User signInAndReturnJWT(User singInRequest);

}
