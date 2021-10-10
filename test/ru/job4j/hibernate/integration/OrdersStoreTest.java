package ru.job4j.hibernate.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();
    private OrdersStore store;

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                        "src\\main\\java\\ru\\job4j\\hibernate\\integration\\db\\update_001.sql"
                )))) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();

        store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
    }

    @After
    public void delete() throws SQLException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                        "src\\main\\java\\ru\\job4j\\hibernate\\integration\\db\\delete_001.sql"
                )))) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenUpdateOrderAndFindUpdateOrder() {
        Order order = Order.of("name2", "description1");
        order.setId(1);
        assertThat(store.update(order), is(Boolean.TRUE));
        assertThat(store.findById(1).getName(), is(order.getName()));
    }

    @Test
    public void whenFindOrderById1ThenReturnOrderWithId1() {
        Order order = Order.of("name2", "description1");
        store.save(order);
        assertThat(store.findById(2), is(order));
    }

    @Test
    public void whenFindOrderByNameTestThenReturnOrderWithThisName() {
        Order order = Order.of("test", "description1");
        store.save(order);
        assertThat(store.findByName("test"), is(order));
    }
}