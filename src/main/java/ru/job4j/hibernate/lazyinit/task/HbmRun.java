package ru.job4j.hibernate.lazyinit.task;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hibernate.lazyinit.task.model.Category;
import ru.job4j.hibernate.lazyinit.task.model.Task;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        List<Category> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Category first = Category.of("Consultation");
            session.persist(first);
            Task one = Task.of("Consultation on Hibernate", first);
            Task two = Task.of("Consultation on Spring", first);
            Task three = Task.of("Consultation on Servlet", first);
            session.persist(one);
            session.save(two);
            session.save(three);

            list = session.createQuery("select distinct c from task_categories c join fetch c.tasks"
            ).list();

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Category category : list) {
            for (Task task : category.getTasks()) {
                System.out.println(task);
            }
        }
    }
}