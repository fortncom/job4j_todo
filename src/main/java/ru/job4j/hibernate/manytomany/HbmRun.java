package ru.job4j.hibernate.manytomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book one = Book.of("Fahrenheit 451");
            Book two = Book.of("The Martian Chronicles");
            Book three = Book.of("Tomorrow and Tomorrow");
            Book four = Book.of("The Nemesis from Terra");

            Author first = Author.of("Ray Douglas Bradbury");
            first.getBooks().add(one);
            first.getBooks().add(two);
            first.getBooks().add(three);

            Author second = Author.of("Leigh Douglass Brackett");
            second.getBooks().add(three);
            second.getBooks().add(four);

            session.persist(first);
            session.persist(second);

            Author author = session.get(Author.class, 1);
            session.remove(author);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}