package commandtest;

import util.Actions;
import util.Parameters;
import command.UpdateProductCommand;
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
public class UpdateProductCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    UpdateProductCommand updateProductCommand = new UpdateProductCommand();

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
    public void shouldPrintAlertWhenProductIdEqNull() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.PRODUCT_ID)).thenReturn(null);
        String expectedPrint = "<script type='text/javascript'>alert('Unknown product id');" +
                "location='" + request.getContextPath() + Actions.CATALOG_ACTION + "'</script>"
                + System.lineSeparator();
        String result = updateProductCommand.process(request, response);
        Assert.assertEquals(expectedPrint, out.toString());
        Assert.assertNull(result);
    }
}
