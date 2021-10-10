package ru.job4j.hibernate.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        List<Candidate> candidates = new ArrayList<>();
        Candidate rsl = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Vacancy vJava = Vacancy.of("Java Develoder");
            Vacancy vSQL = Vacancy.of("SQL Develoder");
            Vacancy vJS = Vacancy.of("JS Develoder");
            session.save(vJava);
            session.save(vSQL);
            session.save(vJS);

            Base base = new Base();
            base.addVacancy(vJava);
            base.addVacancy(vSQL);
            base.addVacancy(vJS);
            session.persist(base);

            Candidate one = Candidate.of("Alex", 6, 95000, base);
            Candidate two = Candidate.of("Nikolay", 1, 45000, base);
            Candidate three = Candidate.of("Nikita", 25, 75000, base);
            session.save(one);
            session.save(two);
            session.save(three);

            session.createQuery(
                    "update Candidate c set c.name = :name, c.experience = :experience,"
                            + " c.salary = :salary where c.id = :fId")
                    .setParameter("name", "Tomas")
                    .setParameter("experience", 22)
                    .setParameter("salary", 140000)
                    .setParameter("fId", 2)
                    .executeUpdate();

            session.createQuery("insert into Candidate (name, experience, salary, base) "
                    + "select concat(c.name, 'Stiven'), "
                    + "c.experience + 5, c.salary + 25000, c.base "
                    + "from Candidate c "
                    + "where c.id = :fId")
                    .setParameter("fId", 1)
                    .executeUpdate();

            session.createQuery("delete from Candidate where id = :fId")
                    .setParameter("fId", 4)
                    .executeUpdate();

            Query query = session.createQuery("select distinct c from Candidate c "
                                               + "join fetch c.base b "
                                               + "join fetch b.vacancies");

            query = session.createQuery("from Candidate where id = :id")
                    .setParameter("id", 1);

            query = session.createQuery("from Candidate where name = :name")
                    .setParameter("name", "Alex");

            candidates = query.list();

            rsl = session.createQuery(
                    "select distinct c from Candidate c "
                            + "join fetch c.base b "
                            + "join fetch b.vacancies "
                            + "where c.id = :sId", Candidate.class
            ).setParameter("sId", 1).uniqueResult();

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Object st : candidates) {
            System.out.println(st);
        }
        System.out.println(rsl);
    }
}