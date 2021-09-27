package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.function.Function;

public class HiberStore implements Store {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry).buildMetadata()
            .buildSessionFactory();

    private HiberStore() {
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    private static final class Lazy {
        private static final Store INST = new HiberStore();
    }

    public void save(Item item) {
        if (item.getId() == 0) {
           create(item);
        } else {
            item = findById(item.getId());
            item.setDone(true);
            update(item);
        }
    }

    private Item create(Item item) {
        return execute(session -> {
            session.save(item);
            return item;
        });
    }

    private void update(Item item) {
        execute(session -> {
            session.update(item);
            return item;
        });
    }

    public void delete(Integer id) {
        execute(session -> {
            session.delete(new Item(id, null));
            return null;
        });
    }

    public List<Item> findAll() {
        return execute(session -> {
            final Query query = session.createQuery("from ru.job4j.todo.model.Item");
            return query.list();
        });
    }

    @Override
    public Item findById(int id) {
        return execute(session -> session.get(Item.class, id));
    }

    private <T> T execute(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
