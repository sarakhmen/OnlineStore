package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command{
    private static final Logger log = Logger.getLogger(LogoutCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:" + request.getContextPath() + Actions.INDEX_PAGE;
    }
}
