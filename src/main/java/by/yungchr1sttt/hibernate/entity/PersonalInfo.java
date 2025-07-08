package by.yungchr1sttt.hibernate.entity;

import by.yungchr1sttt.hibernate.convertor.BirthdayConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo {

    private String firstName;

    private String lastName;

    @Convert(converter = BirthdayConverter.class)
    @Column(name = "birth_date")
    private Birthday birthDate;
}
