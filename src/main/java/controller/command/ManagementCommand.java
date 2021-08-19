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

public class ManagementCommand implements Command {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int page = 1;
        if (request.getParameter(Parameters.PAGE) != null) {
            page = Integer.parseInt(request.getParameter(Parameters.PAGE));
        }
        String locale = (String) session.getAttribute(Parameters.LOCALE);
        UserDao userDao = new UserDao(locale);
        List<User> users = userDao.selectAllUsersExceptGuests((page - 1) * Parameters.RECORDS_PER_PAGE,
                Parameters.RECORDS_PER_PAGE);

        int numberOfRecords = userDao.getNumberOfRecords();
        int numberOfPages = (int) Math.ceil(numberOfRecords * 1.0 / Parameters.RECORDS_PER_PAGE);
        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("currentPage", page);

        session.setAttribute(Parameters.USERS, users);
        return Actions.ADMIN_MANAGEMENT_PAGE;
    }
}