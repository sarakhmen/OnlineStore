package controller.command;

import controller.Actions;
import controller.Parameters;
import model.ProductDao;
import model.entity.ExtendedProduct;
import model.entity.Product;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class EditProductViewCommand implements Command{
    private static final Logger log = Logger.getLogger(EditProductViewCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter(Parameters.PRODUCT_ID));
        ProductDao productDao = new ProductDao("");
        ExtendedProduct exProduct = productDao.selectExProductById(productId);

        request.setAttribute(Parameters.PRODUCT_ID, exProduct.getId());
        request.setAttribute(Parameters.PROD_NAME_EN, exProduct.getName());
        request.setAttribute(Parameters.PROD_NAME_UK, exProduct.getNameUk());
        request.setAttribute(Parameters.PRICE, exProduct.getPrice());

        if(!exProduct.getProperties().isEmpty()){
            StringBuilder sbPropertyNamesEn = new StringBuilder();
            StringBuilder sbPropertyValuesEn = new StringBuilder();
            StringBuilder sbPropertyNamesUk = new StringBuilder();
            StringBuilder sbPropertyValuesUk = new StringBuilder();
            for(Map.Entry<String, String> propEn : exProduct.getProperties().entrySet()){
                sbPropertyNamesEn.append(',').append(propEn.getKey());
                sbPropertyValuesEn.append(',').append(propEn.getValue());
            }
            for(Map.Entry<String, String> propUk : exProduct.getPropertiesUk().entrySet()){
                sbPropertyNamesUk.append(',').append(propUk.getKey());
                sbPropertyValuesUk.append(',').append(propUk.getValue());
            }

            request.setAttribute(Parameters.PROPERTY_NAMES_EN, sbPropertyNamesEn.substring(1));
            request.setAttribute(Parameters.PROPERTY_VALUES_EN, sbPropertyValuesEn.substring(1));
            request.setAttribute(Parameters.PROPERTY_NAMES_UK, sbPropertyNamesUk.substring(1));
            request.setAttribute(Parameters.PROPERTY_VALUES_UK, sbPropertyValuesUk.substring(1));
        }

        return Actions.ADMIN_EDIT_PRODUCT_PAGE;
    }
}