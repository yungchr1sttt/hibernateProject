package by.yungchr1sttt.hibernate.util;

import by.yungchr1sttt.hibernate.convertor.BirthdayConverter;
import by.yungchr1sttt.hibernate.entity.Birthday;
import by.yungchr1sttt.hibernate.entity.Company;
import by.yungchr1sttt.hibernate.entity.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.addAttributeConverter(new BirthdayConverter());
        return configuration.buildSessionFactory();
    }

    public static EntityManagerFactory buildEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("myJpaUnit");
    }
}

