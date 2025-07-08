package by.yungchr1sttt.hibernate.entity;

import by.yungchr1sttt.hibernate.convertor.BirthdayConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"company", "profile", "userChats"})
@EqualsAndHashCode(of = "username")
@Builder
@Entity
@Table(name = "users", schema = "public")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    @GeneratedValue(generator = "user_gen",strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true ,nullable = false)
    private String username;
    //@AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    @Embedded
    private PersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();
}
