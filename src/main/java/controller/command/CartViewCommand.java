package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBManager;
import model.OrderDao;
import model.entity.Order;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CartViewCommand implements Command{
    private static final Logger log = Logger.getLogger(CartViewCommand.class);

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

        int page = 1;
        if (request.getParameter(Parameters.PAGE) != null) {
            page = Integer.parseInt(request.getParameter(Parameters.PAGE));
        }
        OrderDao orderDao = new OrderDao(DBManager.getInstance());

        List<Order> orders = orderDao.selectAllOrders(cartUserId, (page-1)*Parameters.RECORDS_PER_PAGE,
                Parameters.RECORDS_PER_PAGE);
        session.setAttribute(Parameters.ORDERS, orders);

        int numberOfRecords = orderDao.getNumberOfRecords();
        int numberOfPages = (int) Math.ceil(numberOfRecords * 1.0 / Parameters.RECORDS_PER_PAGE);
        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("currentPage", page);

        String userRole = (String)session.getAttribute(Parameters.ROLE);
        if(userRole.equals("ADMIN")){
            return Actions.ADMIN_CART_PAGE;
        }
        return Actions.CART_PAGE;
    }
}