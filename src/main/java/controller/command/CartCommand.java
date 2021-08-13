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

public class CartCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int)session.getAttribute(Parameters.USER_ID);
        OrderDao orderDao = new OrderDao();
        List<Order> orders = orderDao.selectAllOrders(userId);
        System.out.println("processing cart...");
        System.out.println(orders);
        session.setAttribute(Parameters.ORDERS, orders);
        return Actions.CART_PAGE;
    }
}