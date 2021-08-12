package controller.command;

import controller.Pages;
import controller.Parameters;
import model.DBConstants;
import model.ProductDao;
import model.UserDao;
import model.entity.Product;
import model.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class LoginCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);

        if(login == null || password == null || login.isEmpty() || password.isEmpty()){
            request.setAttribute(Parameters.ERROR, "Login/password cannot be empty");
            return Pages.ERROR_PAGE;
        }

        User user = new UserDao().selectUserByLogin(login);

        if(user == null ){
            request.setAttribute(Parameters.ERROR, "Cannot find user with such login");
            return Pages.ERROR_PAGE;
        }

        if(!user.getPassword().equals(password)){
            request.setAttribute(Parameters.ERROR, "Wrong password");
            return Pages.ERROR_PAGE;
        }
        HttpSession session = request.getSession();

        ProductDao productDao = new ProductDao();
        List<Product> products = productDao.selectAllProducts(DBConstants.PRODUCT_NAME, DBConstants.ORDER_DESCENDING);
        Set<String> properties = productDao.selectDistinctPropertyNames();

        session.setAttribute(Parameters.PRODUCTS, products);
        session.setAttribute(Parameters.PRODUCT_PROPERTIES, properties);
        session.setAttribute(Parameters.ID, user.getId());
        session.setAttribute(Parameters.USERNAME, user.getName());
        session.setAttribute(Parameters.ROLE, user.getRole());
        return Pages.CATALOG_PAGE;
    }
}
