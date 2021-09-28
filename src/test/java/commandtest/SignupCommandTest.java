package commandtest;

import util.Actions;
import util.Parameters;
import command.SignupCommand;
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
public class SignupCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SignupCommand signupCommand = new SignupCommand();

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
    public void shouldPrintAlertAndReturnNullWhenParamsAreIncorrect() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn(null);
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn("notNull");
        Mockito.when(request.getParameter(Parameters.USERNAME)).thenReturn("notNull");

        String processResult = signupCommand.process(request, response);
        Assert.assertNull(processResult);
        String expectedPrint = "<script type='text/javascript'>alert('Incorrect input');" +
                "location='" + request.getContextPath() + Actions.SIGNUP_PAGE + "'</script>" + System.lineSeparator();
        Assert.assertEquals(expectedPrint, out.toString());

        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn("notNull");
        Mockito.when(request.getParameter(Parameters.USERNAME)).thenReturn("");
        out.reset();
        processResult = signupCommand.process(request, response);
        Assert.assertNull(processResult);
        Assert.assertEquals(expectedPrint, out.toString());
    }

    @Test
    public void shouldPrintAlertAndReturnNullIfUserIsAlreadyRegistered() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn("artur.sarahman@gmail.com");
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn("qwerty");
        Mockito.when(request.getParameter(Parameters.USERNAME)).thenReturn("Artur Sarakhman");

        String processResult = signupCommand.process(request, response);
        Assert.assertNull(processResult);

        String expectedPrint = "<script type='text/javascript'>alert('A user with this login already exists');" +
                "location='" + request.getContextPath() + Actions.SIGNUP_PAGE + "'</script>" + System.lineSeparator();
        Assert.assertEquals(expectedPrint, out.toString());
    }

    @Test
    public void shouldRedirectToCatalogActionIfUserFound() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn("newUser@gmail.com");
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn("qwerty");
        Mockito.when(request.getParameter(Parameters.USERNAME)).thenReturn("New User");
        Mockito.when(session.getAttribute(Parameters.USER_ID)).thenReturn(4);

        String expectedProcessResult = "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
        String actualProcessResult = signupCommand.process(request, response);
        Assert.assertEquals(expectedProcessResult, actualProcessResult);
    }
}
