package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBManager;
import model.ProductDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Deletes the corresponding product.
 */
public class DeleteProductCommand implements Command{
    private static final Logger log = Logger.getLogger(DeleteProductCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter(Parameters.PRODUCT_ID));
        ProductDao productDao = new ProductDao(DBManager.getInstance());
        productDao.deleteProductById(productId);
        log.info("Product with id " + productId + " successfully deleted");
        return "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
    }
}