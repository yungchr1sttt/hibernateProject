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
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "student")
    private StudentProfile studentProfile;

    public void addStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

}
