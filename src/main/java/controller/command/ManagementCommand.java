package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBManager;
import model.OrderDao;
import model.UserDao;
import model.entity.Order;
import model.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ManagementCommand implements Command {
    private static final Logger log = Logger.getLogger(ManagementCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int page = 1;
        if (request.getParameter(Parameters.PAGE) != null) {
            page = Integer.parseInt(request.getParameter(Parameters.PAGE));
        }
        UserDao userDao = new UserDao(DBManager.getInstance());
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