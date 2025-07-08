package by.yungchr1sttt.hibernate;

import by.yungchr1sttt.hibernate.convertor.BirthdayConverter;
import by.yungchr1sttt.hibernate.entity.*;
import by.yungchr1sttt.hibernate.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
@Slf4j
public class HibernateRunner {
    //перенесён в lombok @Slf4j
    //private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        // TRANSIENT
        Company company = Company.builder()
                .name("Mail")
                .build();


        User user = User.builder()
                .username("ivan52@mail.ru")
                .personalInfo(PersonalInfo.builder()
                        .firstName("Ivan")
                        .lastName("Ivanov")
                        .birthDate(new Birthday(LocalDate.of(2002, 01, 01)))
                        .build())
                .role(Role.ADMIN)
                .company(company)
                .build();

        log.info("User object is transient state: {}", user);
        // TRANSIENT
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                session1.persist(user);


                //PERSISTENT к session1 и TRANSIENT к session2
                //session1.merge(company);
                //session1.merge(user);
                session1.getTransaction().commit();
            }
        } catch (Exception ex) {
            log.error("Exception occurred", ex);
            throw ex;
        }
    }
}

