package by.yungchr1sttt.hibernate.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public interface BaseEntity<T extends Serializable> {
    T getId();

    void setId(T id);
}
