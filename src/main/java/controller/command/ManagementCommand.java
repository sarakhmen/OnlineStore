package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;
import model.UserDao;
import model.entity.Order;
import model.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ManagementCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDao userDao = new UserDao();
        List<User> users = userDao.selectAllUsersExceptGuests();
        session.setAttribute(Parameters.USERS, users);
        return Actions.ADMIN_MANAGEMENT_PAGE;
    }
}