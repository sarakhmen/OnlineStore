package commandtest;

import controller.Actions;
import controller.Parameters;
import controller.command.LogoutCommand;
import controller.command.SignupCommand;
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

@RunWith(MockitoJUnitRunner.class)
public class LogoutCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    LogoutCommand logoutCommand = new LogoutCommand();

    @Before
    public void initMocks() throws IOException {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getContextPath()).thenReturn("/");
    }

    @Test
    public void shouldPrintAlertAndReturnNullWhenParamsAreIncorrect() throws ServletException, IOException {
        String expectedPrint = "redirect:" + request.getContextPath() + Actions.INDEX_PAGE;
        String processResult = logoutCommand.process(request, response);
        Assert.assertEquals(expectedPrint, processResult);
    }

}
