//package commandtest;
//
//import controller.command.AddProductCommand;
//import controller.command.CatalogViewCommand;
//import model.DBManager;
//import model.entity.Product;
//import org.apache.ibatis.jdbc.ScriptRunner;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.*;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//@RunWith(MockitoJUnitRunner.class)
//public class AddProductCommandTest {
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    @Mock
//    private HttpSession session;
//
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    AddProductCommand addProductCommand = new AddProductCommand();
//
//    @BeforeClass
//    public static void initDB() throws SQLException, IOException {
//        DBManager dbManager = DBManager.getInstance();
//        Connection connection = dbManager.getConnection();
//        ScriptRunner runner = new ScriptRunner(connection);
//        Reader reader = new BufferedReader(new FileReader("src\\test\\resources\\dbScript.sql"));
//        runner.runScript(reader);
//        runner.closeConnection();
//    }
//
//    @Before
//    public void initMocks() throws IOException {
//        Mockito.when(request.getSession()).thenReturn(session);
//        Mockito.when(request.getContextPath()).thenReturn("/");
//        PrintWriter writer = new PrintWriter(out, true);
//        Mockito.when(response.getWriter()).thenReturn(writer);
//    }
//
//    @Test
//    public void shouldReturnAlertIfNecessaryFieldsAreNotFilled(){
//        Mockito.when(request.getSession()).thenReturn(session);
//
//    }
//}
