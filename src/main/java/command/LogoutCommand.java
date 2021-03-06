package command;

import util.Actions;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Logs the user out.
 * Invalidates session.
 */
public class LogoutCommand implements Command{
    private static final Logger log = Logger.getLogger(LogoutCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        log.info("User successfully logged out");
        return "redirect:" + request.getContextPath() + Actions.INDEX_PAGE;
    }
}
