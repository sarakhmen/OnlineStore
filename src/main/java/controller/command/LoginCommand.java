package controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginCommand implements Command{
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return null;
    }
}
