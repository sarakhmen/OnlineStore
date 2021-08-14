package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;
import model.entity.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class OrderStatusCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String newStatus = request.getParameter(Parameters.ORDER_STATUS);
        int orderId = Integer.parseInt(request.getParameter(Parameters.ORDER_ID));
        System.out.println(newStatus + " " + orderId);
        OrderDao orderDao = new OrderDao();
        orderDao.updateOrderStatus(orderId, newStatus);
        return "redirect:" + request.getContextPath() + Actions.CART_VIEW_ACTION;
    }
}