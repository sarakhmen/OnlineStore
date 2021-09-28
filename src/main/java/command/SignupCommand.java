package command;

import util.Actions;
import util.Parameters;
import util.DBConstants;
import database.DBManager;
import dao.OrderDao;
import dao.UserDao;
import model.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Signs up the user.
 * Validates the fields value, prints alert if anything is incorrect.
 * Otherwise, sets appropriate session's attributes.
 */
public class SignupCommand implements Command{
    private static final Logger log = Logger.getLogger(SignupCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);
        String userName = request.getParameter(Parameters.USERNAME);

        if(login == null || password == null || userName == null
                || login.isEmpty() || password.isEmpty() || userName.isEmpty()){
            response.getWriter().println("<script type='text/javascript'>alert('Incorrect input');" +
                    "location='" + request.getContextPath() + Actions.SIGNUP_PAGE + "'</script>");
            log.info("Incorrect input");
            return null;
        }

        HttpSession session = request.getSession();
        UserDao userDao = new UserDao(DBManager.getInstance());
        boolean registered = userDao.isRegistered(login);

        if(registered){
            response.getWriter().println("<script type='text/javascript'>alert('A user with this login already exists');" +
                    "location='" + request.getContextPath() + Actions.SIGNUP_PAGE + "'</script>");
            log.info("A user with this login already exists");
            return null;
        }

        User newUser = userDao.insertUser(login, password, userName);
        if(newUser == null){
            response.getWriter().println("<script type='text/javascript'>alert('Error adding user to database');" +
                    "location='" + request.getContextPath() + Actions.SIGNUP_PAGE + "'</script>");
            log.info("Error adding user to database");
            return null;
        }


        if(session.getAttribute(Parameters.USER_ID) != null){
            OrderDao orderDao = new OrderDao(DBManager.getInstance());
            int guestId = (int)session.getAttribute(Parameters.USER_ID);
            orderDao.transferOrders(guestId, newUser.getId());
        }
        session.setAttribute(Parameters.USER_ID, newUser.getId());
        session.setAttribute(Parameters.USERNAME, newUser.getName());
        session.setAttribute(Parameters.ROLE, DBConstants.USER_USER);
        session.setAttribute(Parameters.CART_USER_ID, newUser.getId());
        return "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
    }
}