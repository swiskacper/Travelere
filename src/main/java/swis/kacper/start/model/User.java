package swis.kacper.start.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="db_users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer number;


    @Column(name="name",nullable = false, length = 100)
    private String name;
    @Column(name="surname",nullable = false, length = 100)
    private String surname;



    @Enumerated(EnumType.STRING)
    @Column(name="role",nullable = false)
    private Role role;


    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;


    @Transient
    private String token;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return number != null && Objects.equals(number, user.number);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
