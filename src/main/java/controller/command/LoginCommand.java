package controller.command;

import controller.Actions;
import controller.Parameters;
import model.UserDao;
import model.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);

        if(login == null || password == null || login.isEmpty() || password.isEmpty()){
            request.setAttribute(Parameters.ERROR, "Login/password cannot be empty");
            return Actions.ERROR_PAGE;
        }

        User user = new UserDao().selectUserByLogin(login);

        if(user == null ){
            request.setAttribute(Parameters.ERROR, "Cannot find user with such login");
            return Actions.ERROR_PAGE;
        }

        if(!user.getPassword().equals(password)){
            request.setAttribute(Parameters.ERROR, "Wrong password");
            return Actions.ERROR_PAGE;
        }
        HttpSession session = request.getSession();
        session.setAttribute(Parameters.USER_ID, user.getId());
        session.setAttribute(Parameters.USERNAME, user.getName());
        session.setAttribute(Parameters.ROLE, user.getRole());
        return "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
    }
}
