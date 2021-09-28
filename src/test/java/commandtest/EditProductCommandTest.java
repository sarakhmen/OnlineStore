package commandtest;

import util.Actions;
import util.Parameters;
import command.EditProductViewCommand;
import database.DBManager;
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
public class EditProductCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    EditProductViewCommand editProductViewCommand = new EditProductViewCommand();

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
    public void shouldSetPropertyNamesAndValuesInRequestIfProductHasProperties() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.PRODUCT_ID)).thenReturn("1");
        editProductViewCommand.process(request, response);

        Mockito.verify(request).setAttribute(Mockito.eq(Parameters.PROPERTY_NAMES), Mockito.any());
        Mockito.verify(request).setAttribute(Mockito.eq(Parameters.PROPERTY_VALUES), Mockito.any());
    }

    @Test
    public void shouldReturnAdminEditProductPage() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.PRODUCT_ID)).thenReturn("1");
        String expected = Actions.ADMIN_EDIT_PRODUCT_PAGE;
        String actual = editProductViewCommand.process(request, response);
        Assert.assertEquals(expected, actual);
    }
}
