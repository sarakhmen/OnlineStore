package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteOrderCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int orderId = Integer.parseInt(request.getParameter(Parameters.ORDER_ID));
        OrderDao orderDao = new OrderDao();
        System.out.println("processing delete...");
        orderDao.deleteOrder(orderId);

        String userRole = (String)session.getAttribute(Parameters.ROLE);
        if(userRole.equals("ADMIN")){
            return "redirect:" + request.getContextPath() + Actions.ADMIN_CART_VIEW_ACTION;
        }
        return "redirect:" + request.getContextPath() + Actions.CART_VIEW_ACTION;
    }
}
