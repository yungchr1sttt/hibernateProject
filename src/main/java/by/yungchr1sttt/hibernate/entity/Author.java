package by.yungchr1sttt.hibernate.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "authors")
@NamedEntityGraph(
        name = "Author.withBooks",
        attributeNodes = @NamedAttributeNode("books")
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    @BatchSize(size = 10)
    private List<Book> books = new ArrayList<>();
}
