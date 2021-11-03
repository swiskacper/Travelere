package swis.kacper.start.service;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swis.kacper.start.model.Role;
import swis.kacper.start.model.User;
import swis.kacper.start.repository.IUserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements IUserService {


    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
    user.setRole(Role.USER);
    user.setCreateTime(LocalDateTime.now());
    user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public ArrayList<String> findAllUsers() {

        ArrayList <String> lista=userRepository.gettingAllUsers();
        return lista;
    }

    @Override
    public void deleteUser(Integer number) {
        userRepository.deleteById(number);


}

    @Override
    @Transactional
    public void changeRole(String email, Role role) {
        userRepository.updateUserRole(email,role);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
