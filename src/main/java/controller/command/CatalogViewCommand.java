package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;
import model.DBManager;
import model.ProductDao;
import model.entity.Product;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class CatalogViewCommand implements Command {
    private static final Logger log = Logger.getLogger(CatalogViewCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ProductDao productDao = new ProductDao(DBManager.getInstance());
        Map<String, Set<String>> properties = productDao.selectProperties();
        session.setAttribute(Parameters.PRODUCT_PROPERTIES, properties);

        int page = 1;
        if (request.getParameter(Parameters.PAGE) != null) {
            page = Integer.parseInt(request.getParameter(Parameters.PAGE));
        }

        String action = request.getParameter(Parameters.ACTION);
        Integer priceFrom = null;
        Integer priceTo = null;
        String priceParamTo;
        String priceParamFrom;
        Map<String, Set<String>> checkedProperties;

        if(action != null && action.equals("selectByProperties")){
            priceParamFrom = request.getParameter(Parameters.PRICE_FROM);
            if (priceParamFrom != null && !priceParamFrom.equals("")) {
                priceFrom = Integer.parseInt(request.getParameter(Parameters.PRICE_FROM));
            }

            priceParamTo = request.getParameter(Parameters.PRICE_TO);
            if (priceParamTo != null && !priceParamTo.equals("")) {
                priceTo = Integer.parseInt(request.getParameter(Parameters.PRICE_TO));
            }

            Enumeration<String> selectedProps = request.getParameterNames();
            checkedProperties = new HashMap<>();
            while (selectedProps.hasMoreElements()) {
                String propertyName = selectedProps.nextElement();
                if (properties.containsKey(propertyName)) {
                    Set<String> propertyValues = new HashSet<>(Arrays.asList(request.getParameterValues(propertyName)));
                    checkedProperties.put(propertyName, propertyValues);

                }
            }
        }
        else {
            priceFrom = (Integer)session.getAttribute(Parameters.PRICE_FROM);
            priceTo = (Integer)session.getAttribute(Parameters.PRICE_TO);
            checkedProperties = (Map<String, Set<String>>) session.getAttribute(Parameters.SELECTED_PROPERTIES);
            if(checkedProperties == null){
                checkedProperties = new HashMap<>();
            }
        }

        if ((priceFrom != null && priceFrom < 0) || (priceTo != null && priceTo < 0) || (priceFrom != null
                && priceTo != null && priceFrom > priceTo)) {
            response.getWriter().println("<script type='text/javascript'>alert('Incorrect price input');"
                    + "location='" + request.getContextPath() +
                    Actions.CATALOG_ACTION + "'</script>");
            log.info("Incorrect price input");
            return null;
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
                    DBConstants.ORDER_ASCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                    Parameters.RECORDS_PER_PAGE, priceFrom, priceTo);
        } else {
            switch (sortOption) {
                case Parameters.SORT_BY_NAME_ZA:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRODUCT_NAME,
                            DBConstants.ORDER_DESCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE, priceFrom, priceTo);
                    break;
                case Parameters.SORT_PRICE_HIGH_LOW:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRICE,
                            DBConstants.ORDER_DESCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE, priceFrom, priceTo);
                    break;
                case Parameters.SORT_PRICE_LOW_HIGH:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRICE,
                            DBConstants.ORDER_ASCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE, priceFrom, priceTo);
                    break;
                case Parameters.SORT_NEWEST:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRODUCT_CREATION_DATE,
                            DBConstants.ORDER_DESCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE, priceFrom, priceTo);
                    break;
                default:
                    products = productDao.selectProductsOrderedAndByProperties(Parameters.PRODUCT_NAME,
                            DBConstants.ORDER_ASCENDING, checkedProperties, (page - 1) * Parameters.RECORDS_PER_PAGE,
                            Parameters.RECORDS_PER_PAGE, priceFrom, priceTo);
                    break;
            }
        }
        session.setAttribute(Parameters.PRODUCTS, products);
        session.setAttribute(Parameters.SELECTED_PROPERTIES, checkedProperties);
        session.setAttribute(Parameters.PRICE_FROM, priceFrom);
        session.setAttribute(Parameters.PRICE_TO, priceTo);

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
