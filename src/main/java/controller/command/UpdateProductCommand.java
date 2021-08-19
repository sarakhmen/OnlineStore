package controller.command;

import controller.Actions;
import controller.Parameters;
import model.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class UpdateProductCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String strProdId = request.getParameter(Parameters.PRODUCT_ID);
        if(strProdId == null){
            response.getWriter().println("<script type='text/javascript'>alert('Unknown product id');" +
                    "location='" + request.getContextPath() + Actions.CATALOG_ACTION + "'</script>");
            return null;
        }

        int productId = Integer.parseInt(strProdId);
        String prodNameEn = request.getParameter(Parameters.PROD_NAME_EN);
        String prodNameUk = request.getParameter(Parameters.PROD_NAME_UK);
        String price = request.getParameter(Parameters.PRICE);
        String propertyNamesEn = request.getParameter(Parameters.PROPERTY_NAMES_EN);
        String propertyNamesUk = request.getParameter(Parameters.PROPERTY_NAMES_UK);
        String propertyValuesEn = request.getParameter(Parameters.PROPERTY_VALUES_EN);
        String propertyValuesUk = request.getParameter(Parameters.PROPERTY_VALUES_UK);

        List<String> necessaryFields = new LinkedList<>();
        necessaryFields.add(prodNameEn);
        necessaryFields.add(prodNameUk);
        necessaryFields.add(price);

        for (String field : necessaryFields) {
            if (field == null || field.trim().equals("")) {
                response.getWriter().println("<script type='text/javascript'>alert('All fields except property " +
                        "should be filled');" + "location='" + request.getContextPath() + Actions.CATALOG_ACTION +
                        "'</script>");
                return null;
            }
        }

        String locale = (String) session.getAttribute(Parameters.LOCALE);
        ProductDao productDao = new ProductDao(locale);

        if ((propertyNamesEn == null && propertyNamesUk == null && propertyValuesEn == null && propertyValuesUk == null)
                || (propertyNamesEn.trim().equals("") && propertyNamesUk.trim().equals("") &&
                propertyValuesEn.trim().equals("") && propertyValuesUk.trim().equals(""))) {
            productDao.updateProductWithProperties(productId, prodNameEn.trim(), prodNameUk.trim(),
                    Double.parseDouble(price), null, null, null, null);
        }
        else{
            Stream<String> streamOfPropNamesEn =
                    Pattern.compile(",").splitAsStream(propertyNamesEn);
            Stream<String> streamOfPropValuesEn =
                    Pattern.compile(",").splitAsStream(propertyValuesEn);
            Stream<String> streamOfPropNamesUk =
                    Pattern.compile(",").splitAsStream(propertyNamesUk);
            Stream<String> streamOfPropValuesUk =
                    Pattern.compile(",").splitAsStream(propertyValuesUk);
            String[][] propertiesAndValues = new String[4][];
            propertiesAndValues[0] = streamOfPropNamesEn.map(String::trim).toArray(String[]::new);
            propertiesAndValues[1] = streamOfPropValuesEn.map(String::trim).toArray(String[]::new);
            propertiesAndValues[2] = streamOfPropNamesUk.map(String::trim).toArray(String[]::new);
            propertiesAndValues[3] = streamOfPropValuesUk.map(String::trim).toArray(String[]::new);

            for(int i = 1; i < propertiesAndValues.length; i++){
                if(propertiesAndValues[i].length != propertiesAndValues[i-1].length){
                    response.getWriter().println("<script type='text/javascript'>alert('Property fields should be " +
                            "symmetrically filled');" + "location='" + request.getContextPath() +
                            Actions.CATALOG_ACTION + "'</script>");
                    return null;
                }
            }

            int propertiesAmount = propertiesAndValues[0].length;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < propertiesAmount; j++) {
                    if (propertiesAndValues[i][j].equals("")) {
                        response.getWriter().println("<script type='text/javascript'>alert('Some property names or " +
                                "values are empty');" + "location='" + request.getContextPath() +
                                Actions.CATALOG_ACTION + "'</script>");
                    }
                }
            }

            productDao.updateProductWithProperties(productId, prodNameEn.trim(), prodNameUk.trim(),
                    Double.parseDouble(price), propertiesAndValues[0], propertiesAndValues[1], propertiesAndValues[2],
                    propertiesAndValues[3]);
        }

        response.getWriter().println("<script type='text/javascript'>alert('Product was successfully updated');" +
                "location='" + request.getContextPath() + Actions.CATALOG_ACTION + "'</script>");
        return null;
    }
}