package controller;

import controller.command.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FrontController extends HttpServlet {
    private static final Logger log = Logger.getLogger(FrontController.class);
    private Map<String, Command> commands = new HashMap<>();

    @Override
    public void init() throws ServletException {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("signup", new SignupCommand());
        commands.put("catalog", new CatalogViewCommand());
        commands.put("addToCart", new AddToCartCommand());
        commands.put("cartView", new CartViewCommand());
        commands.put("deleteOrder", new DeleteOrderCommand());
        commands.put("orderStatus", new OrderStatusCommand());
        commands.put("admin/management", new ManagementCommand());
        commands.put("admin/userStatus", new UserStatusCommand());
        commands.put("admin/cartView", new CartViewCommand());
        commands.put("admin/orderStatus", new OrderStatusCommand());
        commands.put("admin/addProductView", new AddProductViewCommand());
        commands.put("admin/addProduct", new AddProductCommand());
        commands.put("admin/deleteProduct", new DeleteProductCommand());
        commands.put("admin/editProductView", new EditProductViewCommand());
        commands.put("admin/updateProduct", new UpdateProductCommand());
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
        log.debug("Controller starts");

        String path = req.getRequestURI();
        path = path.replaceAll(".*/main/", "");
        log.trace("Request parameter: command --> " + path);

        Command command = commands.get(path);
        if(command == null){
            log.error("Unknown command --> " + path);
            req.setAttribute(Parameters.ERROR, "the page doesn't exist");
            req.getRequestDispatcher(Actions.ERROR_PAGE).forward(req, resp);
            return;
        }

        try {
            String page = command.process(req, resp);
            if(page != null){
                if (page.contains("redirect:")) {
                    log.trace("redirect --> " + page);
                    resp.sendRedirect(page.replace("redirect:", ""));
                }
                else {
                    log.trace("forward --> " + page);
                    req.getRequestDispatcher(page).forward(req, resp);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            req.setAttribute(Parameters.ERROR, e.getMessage());
            req.getRequestDispatcher(Actions.ERROR_PAGE).forward(req, resp);
        }
    }
}
