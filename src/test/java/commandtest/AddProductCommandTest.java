package commandtest;

import controller.Actions;
import controller.Parameters;
import controller.command.AddProductCommand;
import model.DBManager;
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

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    AddProductCommand addProductCommand = new AddProductCommand();

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
        Mockito.when(request.getContextPath()).thenReturn("/");
        PrintWriter writer = new PrintWriter(out, true);
        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void shouldReturnAlertIfNecessaryFieldIsNotFilled() throws ServletException, IOException {
        String expected = "<script type='text/javascript'>alert('All fields except property " +
                "should be filled');" + "location='" + request.getContextPath() +
                Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>" + System.lineSeparator();
        addProductCommand.process(request, response);
        String actual = out.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnAlertIfProductWasSuccessfullyAdded() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.PRODUCT_NAME)).thenReturn("new name");
        Mockito.when(request.getParameter(Parameters.PRICE)).thenReturn("100");
        Mockito.when(request.getParameter(Parameters.PROPERTY_NAMES)).thenReturn(null);
        Mockito.when(request.getParameter(Parameters.PROPERTY_VALUES)).thenReturn(null);
        String expected = "<script type='text/javascript'>alert('Product was successfully added');" +
                "location='" + request.getContextPath() + Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>" + System.lineSeparator();
        addProductCommand.process(request, response);
        String actual = out.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnAlertIfPropertyFieldsAreNotSymmetricallyFilled() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.PRODUCT_NAME)).thenReturn("new name");
        Mockito.when(request.getParameter(Parameters.PRICE)).thenReturn("100");
        Mockito.when(request.getParameter(Parameters.PROPERTY_NAMES)).thenReturn("color, age");
        Mockito.when(request.getParameter(Parameters.PROPERTY_VALUES)).thenReturn("black");
        String expected = "<script type='text/javascript'>alert('Property fields should " +
                "be symmetrically filled');" + "location='" + request.getContextPath() +
                Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>" + System.lineSeparator();
        addProductCommand.process(request, response);
        String actual = out.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnAlertIfSomePropertyNamesOrValuesAreEmpty() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.PRODUCT_NAME)).thenReturn("new name");
        Mockito.when(request.getParameter(Parameters.PRICE)).thenReturn("100");
        Mockito.when(request.getParameter(Parameters.PROPERTY_NAMES)).thenReturn("color, age");
        Mockito.when(request.getParameter(Parameters.PROPERTY_VALUES)).thenReturn("black,   ");
        String expected = "<script type='text/javascript'>alert('Some property names or " +
                "values are empty');" + "location='" + request.getContextPath() +
                Actions.ADMIN_ADD_PRODUCT_VIEW_ACTION + "'</script>" + System.lineSeparator();
        addProductCommand.process(request, response);
        String actual = out.toString();
        Assert.assertEquals(expected, actual);
    }
}
