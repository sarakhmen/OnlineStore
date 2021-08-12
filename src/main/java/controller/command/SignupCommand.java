package controller.command;

import controller.Pages;
import controller.Parameters;
import model.DBConstants;
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
        String userName = request.getParameter(Parameters.USERNAME);

        if(login == null || password == null || userName == null
                || login.isEmpty() || password.isEmpty() || userName.isEmpty()){
            request.setAttribute(Parameters.ERROR, "Incorrect input");
            return Pages.ERROR_PAGE;
        }

        UserDao userDao = new UserDao();
        boolean registered = userDao.isRegistered(login);

        if(registered){
            request.setAttribute(Parameters.ERROR, "A user with this login already exists");
            return Pages.ERROR_PAGE;
        }

        User newUser = userDao.putUser(login, password, userName);
        if(newUser == null){
            request.setAttribute(Parameters.ERROR, "Error adding user to database");
            return Pages.ERROR_PAGE;
        }

        HttpSession session = request.getSession();
        session.setAttribute(Parameters.ID, newUser.getId());
        session.setAttribute(Parameters.USERNAME, userName);
        session.setAttribute(Parameters.ROLE, DBConstants.USER_USER);
        return Pages.CATALOG_PAGE;
    }
}