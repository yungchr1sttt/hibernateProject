package by.yungchr1sttt.hibernate.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "students")
@Builder
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "course_name", nullable = false, unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        student.setCourse(this);
    }
}
