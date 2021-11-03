package swis.kacper.start.service;

import org.springframework.stereotype.Service;
import swis.kacper.start.model.Role;
import swis.kacper.start.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface IUserService {
    User saveUser(User user);
    ArrayList<String> findAllUsers();
    void deleteUser(Integer number);
    void changeRole(String email, Role role);
    Optional<User> findByEmail(String email);

}
