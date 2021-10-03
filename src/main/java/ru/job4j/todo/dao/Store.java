package ru.job4j.todo.dao;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {

    void save(Item item);

    void save(User item);

    void save(Role item);

    void delete(Integer id);

    List<Item> findAllItem();

    Item findItemById(int id);

    User findUserByEmail(String email);

    Role findRoleById(int id);
}
