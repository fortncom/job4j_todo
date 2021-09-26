package ru.job4j.todo.persistence;

import ru.job4j.todo.model.Item;

import java.util.List;

public interface Store {

    void save(Item item);

    void delete(Integer id);

    List<Item> findAll();

    Item findById(int id);
}
