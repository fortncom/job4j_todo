package ru.job4j.todo.controller;

import ru.job4j.todo.model.User;
import ru.job4j.todo.dao.HiberStore;
import ru.job4j.todo.dao.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Store store = HiberStore.instOf();
        User user = store.findUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("error", "Не верный email или пароль.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } else {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/welcome/index.html");
        }
    }
}
