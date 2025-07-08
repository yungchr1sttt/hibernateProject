package by.yungchr1sttt.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = "users")
@Builder
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }
}
