package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;
import model.OrderDao;
import model.UserDao;
import model.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignupCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);
        String userNameEn = request.getParameter(Parameters.USERNAME_EN);
        String userNameUk = request.getParameter(Parameters.USERNAME_EN);

        if(login == null || password == null || userNameEn == null || userNameUk == null
                || login.isEmpty() || password.isEmpty() || userNameEn.isEmpty() || userNameUk.isEmpty()){
            request.setAttribute(Parameters.ERROR, "Incorrect input");
            return Actions.ERROR_PAGE;
        }

        HttpSession session = request.getSession();
        String locale = (String)session.getAttribute(Parameters.LOCALE);
        UserDao userDao = new UserDao(locale);
        boolean registered = userDao.isRegistered(login);

        if(registered){
            request.setAttribute(Parameters.ERROR, "A user with this login already exists");
            return Actions.ERROR_PAGE;
        }

        User newUser = userDao.insertUser(login, password, userNameEn, userNameUk);
        if(newUser == null){
            request.setAttribute(Parameters.ERROR, "Error adding user to database");
            return Actions.ERROR_PAGE;
        }


        if(session.getAttribute(Parameters.USER_ID) != null){
            OrderDao orderDao = new OrderDao(locale);
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