package ru.job4j.hibernate.lazyinit.auto;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hibernate.lazyinit.auto.model.Mark;
import ru.job4j.hibernate.lazyinit.auto.model.Model;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {

    public static void main(String[] args) {
        List<Mark> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Mark mark = Mark.of("Skoda");
            session.persist(mark);
            Model one = Model.of("Octavia", mark);
            Model two = Model.of("Rapid", mark);
            Model three = Model.of("Fabia", mark);

            session.persist(one);
            session.save(two);
            session.save(three);

            list = session.createQuery("select distinct m from l_mark m join fetch m.models"
            ).list();

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Mark mark : list) {
            for (Model model : mark.getModels()) {
                System.out.println(model);
            }
        }
    }
}
