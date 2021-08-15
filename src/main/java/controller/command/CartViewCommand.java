package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;
import model.entity.Order;

import javax.print.attribute.standard.PageRanges;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CartViewCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int cartUserId;
        if(request.getParameter(Parameters.CART_USER_ID) != null){
            cartUserId = Integer.parseInt(request.getParameter(Parameters.CART_USER_ID));
            session.setAttribute(Parameters.CART_USER_ID, cartUserId);
        }
        else if(session.getAttribute(Parameters.CART_USER_ID) == null){
            cartUserId = (int)session.getAttribute(Parameters.USER_ID);
            session.setAttribute(Parameters.CART_USER_ID, cartUserId);
        }
        else{
            cartUserId = (int)session.getAttribute(Parameters.CART_USER_ID);
        }

        String locale = (String)session.getAttribute(Parameters.LOCALE);
        OrderDao orderDao = new OrderDao(locale);
        List<Order> orders = orderDao.selectAllOrders(cartUserId);
        System.out.println("processing cart...");
        System.out.println(orders);
        session.setAttribute(Parameters.ORDERS, orders);

        String userRole = (String)session.getAttribute(Parameters.ROLE);
        if(userRole.equals("ADMIN")){
            return Actions.ADMIN_CART_PAGE;
        }
        return Actions.CART_PAGE;
    }
}