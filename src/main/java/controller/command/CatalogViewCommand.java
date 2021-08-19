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
import java.util.*;

public class CatalogViewCommand implements Command {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int page = 1;
        if (request.getParameter(Parameters.PAGE) != null) {
            page = Integer.parseInt(request.getParameter(Parameters.PAGE));
        }
        String locale = (String) session.getAttribute(Parameters.LOCALE);
        ProductDao productDao = new ProductDao(locale);
        Map<String, Set<String>> properties = productDao.selectProperties();
        session.setAttribute(Parameters.PRODUCT_PROPERTIES, properties);

        Enumeration<String> selectedProps = request.getParameterNames();
        Map<String, Set<String>> checkedProperties = new HashMap<>();
        while (selectedProps.hasMoreElements()) {
            String propertyName = selectedProps.nextElement();
            if (properties.containsKey(propertyName)) {
                Set<String> propertyValues = new HashSet<>(Arrays.asList(request.getParameterValues(propertyName)));
                checkedProperties.put(propertyName, propertyValues);

            }
        }

        String sortOption = request.getParameter("options");
        if (sortOption != null && !sortOption.equals("")) {
            session.setAttribute(Parameters.SORT_OPTION, sortOption);
        } else {
            sortOption = (String) session.getAttribute(Parameters.SORT_OPTION);
        }

        List<Product> products = null;
        if (sortOption == null || sortOption.equals("")) {
            products = productDao.selectProductsOrderedAndByProperties(Parameters.PRODUCT_NAME,
                    DBConstants.ORDER_ASCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE, Parameters.RECORDS_PER_PAGE);
        } else {
            switch (sortOption) {
                case Parameters.SORT_BY_NAME_ZA:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRODUCT_NAME,
                            DBConstants.ORDER_DESCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE);
                    break;
                case Parameters.SORT_PRICE_HIGH_LOW:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRICE,
                            DBConstants.ORDER_DESCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE);
                    break;
                case Parameters.SORT_PRICE_LOW_HIGH:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRICE,
                            DBConstants.ORDER_ASCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE);
                    break;
                case Parameters.SORT_NEWEST:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRODUCT_CREATION_DATE,
                            DBConstants.ORDER_DESCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE);
                    break;
                default:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRODUCT_NAME,
                            DBConstants.ORDER_ASCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE);
                    break;
            }
        }
        session.setAttribute(Parameters.PRODUCTS, products);

        int numberOfRecords = productDao.getNumberOfRecords();
        int numberOfPages = (int) Math.ceil(numberOfRecords * 1.0 / Parameters.RECORDS_PER_PAGE);
        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("currentPage", page);

        String userRole = (String) session.getAttribute(Parameters.ROLE);
        if (userRole.equals(DBConstants.USER_ADMIN)) {
            return Actions.ADMIN_CATALOG_PAGE;
        }
        return Actions.CATALOG_PAGE;
    }
}
