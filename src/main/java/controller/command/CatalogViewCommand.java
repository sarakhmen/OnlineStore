package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;
import model.ProductDao;
import model.entity.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CatalogViewCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ProductDao productDao = new ProductDao();
        List<Product> products = productDao.selectAllProducts(DBConstants.PRODUCT_NAME, DBConstants.ORDER_DESCENDING);
        Set<String> properties = productDao.selectDistinctPropertyNames();
        session.setAttribute(Parameters.PRODUCTS, products);
        session.setAttribute(Parameters.PRODUCT_PROPERTIES, properties);

        String userRole = (String)session.getAttribute(Parameters.ROLE);
        System.out.println(userRole);
        if(userRole.equals(DBConstants.USER_ADMIN)){
            return Actions.ADMIN_CATALOG_PAGE;
        }
        return Actions.CATALOG_PAGE;
    }
}
