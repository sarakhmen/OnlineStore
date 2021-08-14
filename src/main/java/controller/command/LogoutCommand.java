package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command{
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        //should implement using cookie
        session.invalidate();
//        session.setAttribute(Parameters.USER_ID, null);
//        session.setAttribute(Parameters.USERNAME, null);
//        session.setAttribute(Parameters.ROLE, DBConstants.USER_GUEST);

        return "redirect:" + request.getContextPath() + Actions.INDEX_PAGE;
    }
}
