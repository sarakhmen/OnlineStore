package controller.command;

import controller.Actions;
import controller.Parameters;
import model.OrderDao;

import javax.naming.NamingEnumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AddProductCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

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

        for(String field : necessaryFields){
            if(field == null || field.equals("")){
                session.setAttribute(Parameters.ERROR, "All fields except property should be filled");
                return Actions.ERROR_PAGE;
            }
        }

        List<String> propertyFields = new LinkedList<>();
        propertyFields.add(propertyNamesEn);
        propertyFields.add(propertyValuesEn);
        propertyFields.add(propertyNamesUk);
        propertyFields.add(propertyValuesUk);

        for(String field : propertyFields){
            if(field != null && !field.equals("")){
                for (String otherField : propertyFields){
                    if(otherField == null || otherField.equals("")){
                        session.setAttribute(Parameters.ERROR, "Property fields should be symmetrically filled");
                        return Actions.ERROR_PAGE;
                    }
                }
            }
        }

        String[][] propertiesAndValues = new String[propertyFields.size()][];
        propertiesAndValues[0] = propertyFields.get(0).split(",");
        int propertyAmount = propertiesAndValues[0].length;

        for(int i = 1; i < propertyFields.size(); i++){
            propertiesAndValues[i] = propertyFields.get(i).split(",");
            if(propertiesAndValues[i].length != propertyAmount){
                session.setAttribute(Parameters.ERROR, "Property fields should be symmetrically filled");
                return Actions.ERROR_PAGE;
            }
        }


        for(int i = 0; i < propertiesAndValues.length; i+=2){

        }





        return Actions.ADD_PRODUCT_PAGE;
    }
}
