package controller;

import controller.command.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FrontController extends HttpServlet {
    private Map<String, Command> commands = new HashMap<>();

    @Override
    public void init() throws ServletException {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("signup", new SignupCommand());
        commands.put("catalog", new CatalogCommand());
        commands.put("order", new OrderCommand());
        commands.put("cart", new CartCommand());
        commands.put("deleteOrder", new DeleteOrderCommand());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        path = path.replaceAll(".*/main/", "");
        Command command = commands.get(path);
        if(command == null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            String page = command.process(req, resp);
            if (page.contains("redirect:")) {
                resp.sendRedirect(page.replace("redirect:", ""));
            } else {
                System.out.println("should be forwarded: " + page);
                req.getRequestDispatcher(page).forward(req, resp);
            }
        }catch (Exception e){
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + Actions.ERROR_PAGE);
        }
    }

    private Command getCommandFromUri(String uri){

        return null;
    }
}
