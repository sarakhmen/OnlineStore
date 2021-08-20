package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;
import model.ProductDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteProductCommand implements Command{
    private static final Logger log = Logger.getLogger(DeleteProductCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int productId = Integer.parseInt(request.getParameter(Parameters.PRODUCT_ID));
        String locale = (String)session.getAttribute(Parameters.LOCALE);
        ProductDao productDao = new ProductDao(locale);
        productDao.deleteProductById(productId);
        return "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
    }
}