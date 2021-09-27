package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBManager;
import model.ProductDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AddProductCommand implements Command {
    private static final Logger log = Logger.getLogger(AddProductCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prodName = request.getParameter(Parameters.PRODUCT_NAME);
        String price = request.getParameter(Parameters.PRICE);
        String propertyNames = request.getParameter(Parameters.PROPERTY_NAMES);
        String propertyValues = request.getParameter(Parameters.PROPERTY_VALUES);

        List<String> necessaryFields = new LinkedList<>();
        necessaryFields.add(prodName);
        necessaryFields.add(price);

        for (String field : necessaryFields) {
            if (field == null || field.trim().equals("")) {
                response.getWriter().println("<script type='text/javascript'>alert('All fields except property " +
                        "should be filled');" + "location='" + request.getContextPath() +
                        Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>");
                log.info("All fields except property should be filled");
                return null;
            }
        }

        ProductDao productDao = new ProductDao(DBManager.getInstance());

        if ((propertyNames == null && propertyValues == null) || (propertyNames.trim().equals("")
                && propertyValues.trim().equals(""))) {
            productDao.insertProductWithProperties(prodName.trim(), Double.parseDouble(price),
                    null, null);
        } else {
            Stream<String> streamOfPropNames =
                    Pattern.compile(",").splitAsStream(propertyNames);
            Stream<String> streamOfPropValues =
                    Pattern.compile(",").splitAsStream(propertyValues);
            String[][] propertiesAndValues = new String[2][];
            propertiesAndValues[0] = streamOfPropNames.map(String::trim).toArray(String[]::new);
            propertiesAndValues[1] = streamOfPropValues.map(String::trim).toArray(String[]::new);

            if (propertiesAndValues[0].length != propertiesAndValues[1].length) {
                response.getWriter().println("<script type='text/javascript'>alert('Property fields should " +
                        "be symmetrically filled');" + "location='" + request.getContextPath() +
                        Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>");
                log.info("Property fields should be symmetrically filled");
                return null;
            }

            int propertiesAmount = propertiesAndValues[0].length;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < propertiesAmount; j++) {
                    if (propertiesAndValues[i][j].equals("")) {
                        response.getWriter().println("<script type='text/javascript'>alert('Some property names or " +
                                "values are empty');" + "location='" + request.getContextPath() +
                                Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>");
                        log.info("Some property names values are empty");
                        return null;
                    }
                }
            }

            productDao.insertProductWithProperties(prodName.trim(), Double.parseDouble(price),
                    propertiesAndValues[0], propertiesAndValues[1]);
        }

        response.getWriter().println("<script type='text/javascript'>alert('Product was successfully added');" +
                "location='" + request.getContextPath() + Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>");
        log.info("Product was successfully added");
        return null;
    }
}
