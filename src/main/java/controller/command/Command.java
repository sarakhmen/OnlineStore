package controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Generic interface for command pattern
 */
public interface Command {
    String process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
