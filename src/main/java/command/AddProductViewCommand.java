package command;

import util.Actions;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns addProduct page
 */
public class AddProductViewCommand implements Command{
    private static final Logger log = Logger.getLogger(AddProductViewCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return Actions.ADMIN_ADD_PRODUCT_PAGE;
    }
}