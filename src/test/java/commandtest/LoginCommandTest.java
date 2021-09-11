package commandtest;

import controller.Actions;
import controller.Parameters;
import controller.command.LoginCommand;
import model.DBManager;
import model.UserDao;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.print.DocFlavor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class LoginCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LoginCommand loginCommand = new LoginCommand();

    @BeforeClass
    public static void initDB() throws SQLException, FileNotFoundException {
        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        Reader reader = new BufferedReader(new FileReader("src\\test\\resources\\dbScript.sql"));
        runner.runScript(reader);
    }

    @Before
    public void initMocks() throws IOException {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getContextPath()).thenReturn("/");
        Mockito.when(session.getAttribute(Parameters.LOCALE)).thenReturn("en");
        PrintWriter writer = new PrintWriter(out, true);
        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void shouldPrintAlertAndReturnNullWhenLoginEqNull() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn(null);
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn("pass");

        String processResult = loginCommand.process(request, response);
        Assert.assertNull(processResult);

        String expectedPrint = "<script type='text/javascript'>alert('Login/password cannot be empty');" +
                "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>" + System.lineSeparator();
        Assert.assertEquals(expectedPrint, out.toString());
    }

    @Test
    public void shouldPrintAlertAndReturnNullWhenPassEqNull() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn("login");
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn(null);

        String processResult = loginCommand.process(request, response);
        Assert.assertNull(processResult);

        String expectedPrint = "<script type='text/javascript'>alert('Login/password cannot be empty');" +
                "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>" + System.lineSeparator();
        Assert.assertEquals(expectedPrint, out.toString());
    }

    @Test
    public void shouldPrintAlertAndReturnNullWhenThereAreNoSuchUserInDB() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn("Unreal user");
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn("pass");

        String processResult = loginCommand.process(request, response);
        Assert.assertNull(processResult);

        String expectedPrint = "<script type='text/javascript'>alert('Cannot find user with such login');" +
                "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>" + System.lineSeparator();
        Assert.assertEquals(expectedPrint, out.toString());
    }

    @Test
    public void shouldPrintAlertAndReturnNullWhenPassIsIncorrect() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn("artur.sarahman@gmail.com");
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn("incorrectPass");

        String processResult = loginCommand.process(request, response);
        Assert.assertNull(processResult);

        String expectedPrint = "<script type='text/javascript'>alert('Wrong password');" +
                "location='" + request.getContextPath() + Actions.LOGIN_PAGE + "'</script>" + System.lineSeparator();
        Assert.assertEquals(expectedPrint, out.toString());
    }

    @Test
    public void shouldRedirectToCatalogActionIfUserFound() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.LOGIN)).thenReturn("artur.sarahman@gmail.com");
        Mockito.when(request.getParameter(Parameters.PASSWORD)).thenReturn("qwerty");
        Mockito.when(session.getAttribute(Parameters.USER_ID)).thenReturn(4);

        String expectedProcessResult = "redirect:" + request.getContextPath() + Actions.CATALOG_ACTION;
        String actualProcessResult = loginCommand.process(request, response);
        Assert.assertEquals(expectedProcessResult, actualProcessResult);
    }

}
