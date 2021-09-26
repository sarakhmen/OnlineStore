package commandtest;

import controller.Actions;
import controller.Parameters;
import controller.command.CartViewCommand;
import controller.command.CatalogViewCommand;
import model.DBConstants;
import model.DBManager;
import model.entity.Product;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CartViewCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    CartViewCommand cartViewCommand = new CartViewCommand();

    @BeforeClass
    public static void initDB() throws SQLException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        Reader reader = new BufferedReader(new FileReader("src\\test\\resources\\dbScript.sql"));
        runner.runScript(reader);
        runner.closeConnection();
    }

    @Before
    public void initMocks() throws IOException {
        Mockito.when(request.getSession()).thenReturn(session);
    }

    @Test
    public void shouldGetCartUserIdFromRequestIfSent() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.CART_USER_ID)).thenReturn("2");
        Mockito.when(request.getParameter(Parameters.PAGE)).thenReturn("2");

        cartViewCommand.process(request, response);
        Mockito.verify(request, Mockito.atLeast(2)).getParameter(Parameters.CART_USER_ID);
    }

    @Test
    public void shouldReturnAdminCartViewIfRoleEqAdmin() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_ADMIN);
        Mockito.when(session.getAttribute(Parameters.CART_USER_ID)).thenReturn(2);
        Mockito.when(request.getParameter(Parameters.CART_USER_ID)).thenReturn(null);
        Mockito.when(request.getParameter(Parameters.PAGE)).thenReturn("1");

        String expected = Actions.ADMIN_CART_PAGE;
        String actual = cartViewCommand.process(request, response);
        Assert.assertEquals(expected, actual);

    }
}
