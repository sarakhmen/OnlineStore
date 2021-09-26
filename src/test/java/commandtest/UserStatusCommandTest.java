package commandtest;

import controller.Actions;
import controller.Parameters;
import controller.command.CatalogViewCommand;
import controller.command.UserStatusCommand;
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
public class UserStatusCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    UserStatusCommand userStatusCommand = new UserStatusCommand();

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
    }

    @Test
    public void shouldNotChangeStatusIfUserIsAdmin() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.ROLE)).thenReturn(DBConstants.USER_ADMIN);
        userStatusCommand.process(request, response);
        Mockito.verify(request, Mockito.never()).getParameter(Parameters.NEW_BLOCK_STATUS);
    }

    @Test
    public void shouldReturnRedirectAdminManagementAction() throws ServletException, IOException {
        Mockito.when(request.getParameter(Parameters.ROLE)).thenReturn(DBConstants.USER_USER);
        Mockito.when(request.getParameter(Parameters.NEW_BLOCK_STATUS)).thenReturn("Unblocked");
        Mockito.when(request.getParameter(Parameters.USER_ID)).thenReturn("0");
        String expected = "redirect:/" + Actions.ADMIN_MANAGEMENT_ACTION;
        String actual = userStatusCommand.process(request, response);
        Assert.assertEquals(expected, actual);
    }
}
