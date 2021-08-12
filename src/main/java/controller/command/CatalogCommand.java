package controller.command;

import controller.Pages;
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

public class CatalogCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        ProductDao productDao = new ProductDao();
        List<Product> products = productDao.selectAllProducts(DBConstants.PRODUCT_NAME, DBConstants.ORDER_DESCENDING);
        Set<String> properties = productDao.selectDistinctPropertyNames();
        System.out.println(products);
        System.out.println(properties);
        session.setAttribute(Parameters.PRODUCTS, products);
        session.setAttribute(Parameters.PRODUCT_PROPERTIES, properties);
        return Pages.CATALOG_PAGE;
    }
}
