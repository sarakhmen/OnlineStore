package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;
import model.entity.Order;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class OrderStatusCommand implements Command{
    private static final Logger log = Logger.getLogger(OrderStatusCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String newStatus = request.getParameter(Parameters.ORDER_STATUS);
        int orderId = Integer.parseInt(request.getParameter(Parameters.ORDER_ID));
        String locale = (String)session.getAttribute(Parameters.LOCALE);
        OrderDao orderDao = new OrderDao(locale);
        orderDao.updateOrderStatus(orderId, newStatus);

        String userRole = (String)session.getAttribute(Parameters.ROLE);
        if(userRole.equals("ADMIN")){
            return "redirect:" + request.getContextPath() + Actions.ADMIN_CART_VIEW_ACTION;
        }
        return "redirect:" + request.getContextPath() + Actions.CART_VIEW_ACTION;
    }
}