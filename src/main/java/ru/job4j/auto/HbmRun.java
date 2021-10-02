package ru.job4j.auto;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.auto.model.Mark;
import ru.job4j.auto.model.Model;

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model one = Model.of("Octavia");
            session.save(one);
            Model two = Model.of("Rapid");
            session.save(two);
            Model three = Model.of("Roomster");
            session.save(three);
            Model four = Model.of("Superb");
            session.save(four);
            Model five = Model.of("Fabia");
            session.save(five);

            Mark mark = Mark.of("Skoda");
            mark.addModel(session.load(Model.class, 1));
            mark.addModel(session.load(Model.class, 2));
            mark.addModel(session.load(Model.class, 3));
            mark.addModel(session.load(Model.class, 4));
            mark.addModel(session.load(Model.class, 5));
            session.save(mark);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
