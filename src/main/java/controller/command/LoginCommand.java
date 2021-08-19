package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;
import model.UserDao;
import model.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class LoginCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);

        if(login == null || password == null || login.isEmpty() || password.isEmpty()){
            response.getWriter().println("<script type='text/javascript'>alert('Login/password cannot be empty');" +
                    "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>");
            return null;
        }

        HttpSession session = request.getSession();
        String locale = (String)session.getAttribute(Parameters.LOCALE);
        UserDao userDao = new UserDao(locale);
        User user = userDao.selectUserByLogin(login);

        if(user == null ){
            response.getWriter().println("<script type='text/javascript'>alert('Cannot find user with such login');" +
                    "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>");
            return null;
        }

        if(!user.getPassword().equals(password)){
            response.getWriter().println("<script type='text/javascript'>alert('Wrong password');" +
                    "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>");
            return null;
        }

        if(session.getAttribute(Parameters.USER_ID) != null){
            int guestId = (int)session.getAttribute(Parameters.USER_ID);
            OrderDao orderDao = new OrderDao(locale);
            orderDao.transferOrders(guestId, user.getId());
        }

        List<String> names = userDao.selectUserNames(user.getId());
        session.setAttribute(Parameters.USER_ID, user.getId());
        session.setAttribute(Parameters.USERNAME_EN, names.get(0));
        session.setAttribute(Parameters.USERNAME_UK, names.get(1));
        session.setAttribute(Parameters.ROLE, user.getRole());
        session.setAttribute(Parameters.CART_USER_ID, user.getId());
        return "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
    }
}
