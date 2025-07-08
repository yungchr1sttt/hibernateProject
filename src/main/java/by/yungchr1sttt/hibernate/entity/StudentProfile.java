package by.yungchr1sttt.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student_profile")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;

    private Double attendance;

    private String performance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;
}
