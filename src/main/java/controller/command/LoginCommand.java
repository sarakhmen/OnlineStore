package controller.command;

import controller.Pages;
import model.UserDao;
import model.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements Command{
    private final String errorAttribute = "errorMessage";
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if(login == null || password == null || login.isEmpty() || password.isEmpty()){
            request.setAttribute(errorAttribute, "Login/password cannot be empty");
            return Pages.ERROR_PAGE;
        }

        User user = new UserDao().findUserByLogin(login);

        if(user == null ){
            request.setAttribute(errorAttribute, "Cannot find user with such login");
            return Pages.ERROR_PAGE;
        }

        if(!user.getPassword().equals(password)){
            request.setAttribute(errorAttribute, "Wrong password");
            return Pages.ERROR_PAGE;
        }

        HttpSession session = request.getSession();
        session.setAttribute("username", user.getName());
        session.setAttribute("role", user.getRole());
        return Pages.MAIN_PAGE;
    }
}
