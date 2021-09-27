package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBManager;
import model.OrderDao;
import model.UserDao;
import model.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Logins the user.
 * Validates the fields value, prints alert if anything is incorrect.
 * Otherwise, sets appropriate session's attributes.
 */
public class LoginCommand implements Command{
    private static final Logger log = Logger.getLogger(LoginCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);

        if(login == null || password == null || login.isEmpty() || password.isEmpty()){
            response.getWriter().println("<script type='text/javascript'>alert('Login/password cannot be empty');" +
                    "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>");
            log.info("Login/password cannot be empty");
            return null;
        }

        HttpSession session = request.getSession();
        UserDao userDao = new UserDao(DBManager.getInstance());
        User user = userDao.selectUserByLogin(login);

        if(user == null ){
            response.getWriter().println("<script type='text/javascript'>alert('Cannot find user with such login');" +
                    "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>");
            log.info("Cannot find user with such login");
            return null;
        }

        if(!user.getPassword().equals(password)){
            response.getWriter().println("<script type='text/javascript'>alert('Wrong password');" +
                    "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>");
            log.info("Wrong password");
            return null;
        }

        if(session.getAttribute(Parameters.USER_ID) != null){
            int guestId = (int)session.getAttribute(Parameters.USER_ID);
            OrderDao orderDao = new OrderDao(DBManager.getInstance());
            orderDao.transferOrders(guestId, user.getId());
        }

        session.setAttribute(Parameters.USER_ID, user.getId());
        session.setAttribute(Parameters.USERNAME, user.getName());
        session.setAttribute(Parameters.ROLE, user.getRole());
        session.setAttribute(Parameters.CART_USER_ID, user.getId());
        log.info("User successfully logged in");
        return "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
    }
}
