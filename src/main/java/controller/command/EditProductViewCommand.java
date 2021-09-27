package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBManager;
import model.ProductDao;
import model.entity.Product;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Sets up the editProduct page.
 */
public class EditProductViewCommand implements Command{
    private static final Logger log = Logger.getLogger(EditProductViewCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter(Parameters.PRODUCT_ID));
        ProductDao productDao = new ProductDao(DBManager.getInstance());
        Product product = productDao.selectProductById(productId);

        request.setAttribute(Parameters.PRODUCT_ID, product.getId());
        request.setAttribute(Parameters.PRODUCT_NAME, product.getName());
        request.setAttribute(Parameters.PRICE, product.getPrice());

        if(!product.getProperties().isEmpty()){
            StringBuilder sbPropertyNames = new StringBuilder();
            StringBuilder sbPropertyValues = new StringBuilder();
            for(Map.Entry<String, String> prop : product.getProperties().entrySet()){
                sbPropertyNames.append(',').append(prop.getKey());
                sbPropertyValues.append(',').append(prop.getValue());
            }

            request.setAttribute(Parameters.PROPERTY_NAMES, sbPropertyNames.substring(1));
            request.setAttribute(Parameters.PROPERTY_VALUES, sbPropertyValues.substring(1));
        }

        log.info("editProduct page successfully set up");
        return Actions.ADMIN_EDIT_PRODUCT_PAGE;
    }
}