package by.yungchr1sttt.hibernate;

import by.yungchr1sttt.hibernate.entity.*;
import by.yungchr1sttt.hibernate.util.HibernateUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManagerFactory;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


class HibernateRunnerTest {

    @Test
    public void solveProblemUsingNamedEntityGraph() {
        @Cleanup var em = HibernateUtil.buildEntityManagerFactory().createEntityManager();

        // Данный способ с использованием NamedEntityGraph позволил решить проблему N+1
        // с использованием лишь одного запроса
        EntityGraph<?> graph = em.getEntityGraph("Author.withBooks");

        List<Author> authors = em.createQuery("select a from Author a", Author.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .getResultList();

        authors.forEach(author -> {
            System.out.println(author.getName());
            author.getBooks().forEach(book -> System.out.println("  " + book.getTitle()));
        });
    }

    @Test
    public void solveProblemUsingEntityGraph() {
        @Cleanup var em = HibernateUtil.buildEntityManagerFactory().createEntityManager();

        // Данный способ с использованием EntityGraph позволил решить проблему N+1
        // с использованием лишь одного запроса
        EntityGraph<Author> graph = em.createEntityGraph(Author.class);
        graph.addSubgraph("books");

        List<Author> authors = em.createQuery("select a from Author a", Author.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .getResultList();

        authors.forEach(author -> {
            System.out.println(author.getName());
            author.getBooks().forEach(book -> System.out.println("  " + book.getTitle()));
        });
    }

    @Test
    public void solveProblemUsingBatchSize() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        // Воссоздание проблемы N+1
        List<Author> authors = session.createQuery("from Author", Author.class).list();

        // Использование аннотации BatchSize позволило избежать проблемы N+1
        // ,но количество запросов в данном случае составляет 1 < BatchSize < N.
        for (Author author : authors) {
            System.out.println("Author: " + author.getName());

            for(Book book : author.getBooks()) {
                System.out.println("Book: " + book.getTitle());
            }
        }

        session.getTransaction().commit();
    }

    @Test
    public void solveProblemUsingJoinFetchHql() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        // Воссоздание проблемы N+1
        //List<Author> authors = session.createQuery("from Author", Author.class).list();

        // Решение проблемы N+1 с помощью JOIN FETCH HQL.
        // Данный способ позволяет решить проблему за 1 запрос.
        List<Author> authors = session.createQuery(
                "select distinct a from Author a join fetch a.books", Author.class
        ).list();

        for (Author author : authors) {
            System.out.println("Author: " + author.getName());

            for(Book book : author.getBooks()) {
                System.out.println("Book: " + book.getTitle());
            }
        }

        session.getTransaction().commit();
    }

    @Test
    public void testNPlusOneProblem() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        // Воссоздание проблемы N+1
        List<Author> authors = session.createQuery("from Author", Author.class).list();

        // Решение проблемы N+1 с помощью JOIN FETCH HQL
//        List<Author> authors = session.createQuery(
//                "select distinct a from Author a join fetch a.books", Author.class
//        ).list();

        // Данный код отработает корректно, благодаря использованию BatchSize(10)
        // ,но количество запросов будет меньше, чем N, но больше 1.
        for (Author author : authors) {
            System.out.println("Author: " + author.getName());

            for(Book book : author.getBooks()) {
                System.out.println("Book: " + book.getTitle());
            }
        }

        session.getTransaction().commit();
    }

    @Test
    public void testProblem() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Author author = Author.builder()
                .name("Kevin Spice")
                .build();
        Author author2 = Author.builder()
                .name("Pushkin")
                .build();
        Author author3 = Author.builder()
                .name("Lermontov")
                .build();
        session.persist(author);
        session.persist(author2);
        session.persist(author3);

        Book book = Book.builder()
                .title("Unknown")
                .author(author)
                .build();
        Book book2 = Book.builder()
                .title("Evgeniy Onegin")
                .author(author2)
                .build();
        Book book3 = Book.builder()
                .title("Hero of Our Time")
                .author(author3)
                .build();
        Book book4 = Book.builder()
                .title("Mciry")
                .author(author3)
                .build();
        Book book5 = Book.builder()
                .title("Borodino")
                .author(author3)
                .build();
        session.persist(book);
        session.persist(book2);
        session.persist(book3);
        session.persist(book4);
        session.persist(book5);


        session.getTransaction().commit();
    }


//    @Test
//    public void checkHQL() {
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        var users = session.createQuery("select u from User u").list();
//        System.out.println(users);
//        session.getTransaction().commit();
//    }



//    @Test
//    public void checkInheritance() {
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        Company company = Company.builder()
//                .name("Yandex")
//                .build();
//
//        session.persist(company);
//
//        Programmer programmer = Programmer.builder()
//                .username("ivan@gmail.com")
//                .language(Language.JAVA)
//                .company(company)
//                .build();
//
//        session.persist(programmer);
//
//        Manager manager = Manager.builder()
//                .username("petr@gmail.com")
//                .company(company)
//                .project("Java Enterprise")
//                .build();
//
//        session.persist(manager);
//
//        session.flush();
//        session.clear();
//
//        Programmer programmer1 = session.find(Programmer.class, 1L);
//        User manager1 = session.find(User.class, 2L);
//
//        session.getTransaction().commit();
//    }

    @Test
    public void checkH2() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Google")
                .build();

        session.persist(company);

        session.getTransaction().commit();
    }


    @Test
    public void testAddingStudentProfile() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Course course= session.find(Course.class, 1L);

        Student  student = Student.builder()
                .name("Katyusha")
                .course(course)
                .build();

        session.persist(student);

        StudentProfile studentProfile = StudentProfile.builder()
                .grade("The Best")
                .attendance(95d)
                .performance("Excellent student")
                .student(student)  // Связываем профиль с созданным студентом
                .build();

        session.persist(studentProfile);

        session.getTransaction().commit();
    }

    @Test
    public void createStudentAndCourse() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

//        Course course = Course.builder()
//                .name("Java Enterprise")
//                .build();
        Course course = session.find(Course.class, 1);

        Student student = Student.builder()
                .name("Vanek")
                .course(course)
                .build();

        //course.addStudent(student);
        //session.persist(course);
        System.out.println(course.getStudents());

        session.getTransaction().commit();
    }


    @Test
    public void checkManyToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Chat chat = session.find(Chat.class, 1);
        User user = session.find(User.class, 4);
//        UserChat userChat = UserChat.builder()
//                .createdAt(Instant.now())
//                .createdBy("yungchr1sttt")
//                .build();
        UserChat userChat = new UserChat();
        userChat.setChat(chat);
        userChat.setUser(user);

        session.persist(userChat);


        session.getTransaction().commit();
    }

    @Test
    public void checkOneToOne() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .username("ivanOneToOne52@gmail.ru")
                .personalInfo(PersonalInfo.builder()
                        .firstName("ivanOneToOne")
                        .lastName("iONEov")
                        .birthDate(new Birthday(LocalDate.of(2010, 07, 01)))
                        .build())
                .role(Role.USER)
                .build();

        Profile profile = Profile.builder()
                .street("Moskovskaya")
                .language("Ru")
                .build();

        session.persist(user);
        profile.setUser(user);
        session.persist(profile);

        session.getTransaction().commit();
    }



    @Test
    public void checkOrphalRemoval() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.find(Company.class, 4);
        company.getUsers().removeIf(user -> user.getId().equals(9));

        session.getTransaction().commit();
    }


    @Test
    public void addNewUserAndCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Zuzex")
                .build();

        User user = User.builder()
                .username("ivan3@mail.ru")
                .build();

        company.addUser(user);

        session.persist(company);

        session.getTransaction().commit();
    }

    @Test
    public void checkOneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.find(Company.class, 4);
        System.out.println(company.getUsers());

        session.getTransaction().commit();
    }




    @Test
    public void testHibernateApi() throws SQLException, IllegalAccessException {
       /* User user = User.builder()
                .username("ivan1@mail.ru")
                .firstName("ivan")
                .lastName("Ivanov")
                .birthDate(LocalDate.of(2000, 01, 01))
                .age(25)
                .build();

        var sql = """
                INSERT into
                %s
                (%s)
                values
                (%s)
                """;

        var tableName =  Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + ". " + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] fields = user.getClass().getDeclaredFields();

        var columnName = Arrays.stream(fields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName())
                ).collect(Collectors.joining(", "));

        var columnValues = Arrays.stream(fields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hibernate"
                ,"postgres"
                ,"root");
        PreparedStatement preparedStatement = connection
                .prepareStatement(sql.formatted(tableName, columnName, columnValues));

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            preparedStatement.setObject(i + 1, fields[i].get(user));
        }

        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        */
    }

}