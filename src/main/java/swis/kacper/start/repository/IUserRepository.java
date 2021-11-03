package swis.kacper.start.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import swis.kacper.start.model.Role;
import swis.kacper.start.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {

/*/
    @Query("delete from User where number=:number")
    void deletingUser(@Param("number") Number number);
/*/

    @Modifying
    @Query("update User set role=:role where email=:email")
    void updateUserRole(@Param("email") String email, @Param("role") Role role);


    @Query("Select number,name,surname,email,role,password from User")
    ArrayList<String> gettingAllUsers();

    Optional<User>findByEmail(String email);


}
