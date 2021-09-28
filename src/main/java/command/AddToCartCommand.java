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
 * Adds the product to the user cart
 */
public class AddToCartCommand implements Command{
    private static final Logger log = Logger.getLogger(AddToCartCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int)session.getAttribute(Parameters.USER_ID);
        int productId = Integer.parseInt(request.getParameter(Parameters.PRODUCT_ID));
        OrderDao orderDao = new OrderDao(DBManager.getInstance());
        if(orderDao.insertOrder(userId, productId)){
            //some code;
        }
        else{
            //some code;
        }

        log.info("Product successfully added to the cart");
        return "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
    }
}