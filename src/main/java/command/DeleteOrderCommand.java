package command;

import util.Actions;
import util.Parameters;
import database.DBManager;
import dao.OrderDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Deletes the user's order.
 * Returns redirect to the page based on the value of the user's role.
 */
public class DeleteOrderCommand implements Command{
    private static final Logger log = Logger.getLogger(DeleteOrderCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int orderId = Integer.parseInt(request.getParameter(Parameters.ORDER_ID));
        OrderDao orderDao = new OrderDao(DBManager.getInstance());
        orderDao.deleteOrder(orderId);

        String userRole = (String)session.getAttribute(Parameters.ROLE);
        if(userRole.equals("ADMIN")){
            return "redirect:" + request.getContextPath() + Actions.ADMIN_CART_VIEW_ACTION;
        }
        return "redirect:" + request.getContextPath() + Actions.CART_VIEW_ACTION;
    }
}
