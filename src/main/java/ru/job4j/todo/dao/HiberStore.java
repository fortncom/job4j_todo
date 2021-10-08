package ru.job4j.todo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.function.Function;

public class HiberStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(HiberStore.class.getName());

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
            item = findItemById(item.getId());
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

    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private User create(User user) {
        return execute(session -> {
            session.save(user);
            return user;
        });
    }

    private void update(User user) {
        execute(session -> {
            session.update(user);
            return user;
        });
    }

    public void save(Role role) {
        if (role.getId() == 0) {
            create(role);
        } else {
            update(role);
        }
    }

    private Role create(Role role) {
        return execute(session -> {
            session.save(role);
            return role;
        });
    }

    private void update(Role role) {
        execute(session -> {
            session.update(role);
            return role;
        });
    }

    @Override
    public void save(Category category) {
        if (category.getId() == 0) {
            create(category);
        } else {
            update(category);
        }
    }

    private Category create(Category category) {
        return execute(session -> {
            session.save(category);
            return category;
        });
    }

    private void update(Category category) {
        execute(session -> {
            session.update(category);
            return category;
        });
    }

    public void delete(Integer id) {
        execute(session -> {
            Item item = new Item();
            item.setId(id);
            session.delete(item);
            return null;
        });
    }

    public List<Item> findAllItem() {
        return execute(session -> {
            final Query query = session.createQuery(
                    "select distinct i from item i join fetch i.categories");
            return query.list();
        });
    }

    @Override
    public List<Category> findAllCategories() {
        return execute(session ->
                session.createQuery("select c from categories c", Category.class).list());
    }

    @Override
    public Item findItemById(int id) {
        return (Item) execute(session -> {
            final Query query = session.createQuery(
                    "select distinct i from item i join fetch i.categories where i.id=:id");
            query.setParameter("id", id);
            return query.uniqueResult();
        });
    }

    @Override
    public Role findRoleById(int id) {
        return execute(session -> session.get(Role.class, id));
    }

    @Override
    public Category findCategoryById(int id) {
        return execute(session -> session.get(Category.class, id));
    }

    @Override
    public User findUserByEmail(String email) {
        return (User) execute(session -> {
            Query query = session.createQuery(
                    "from ru.job4j.todo.model.User as u where u.email=:email");
            query.setParameter("email", email);
            return query.uniqueResult();
        });
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
            LOG.error(e.getMessage());
            return null;
        } finally {
            session.close();
        }
    }
}
