package commandtest;

import util.Actions;
import util.Parameters;
import command.CatalogViewCommand;
import util.DBConstants;
import database.DBManager;
import model.Product;
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
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class CatalogViewCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    CatalogViewCommand catalogViewCommand = new CatalogViewCommand();
    List<Product> products;

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
    public void shouldGetCorrectPage() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.PAGE)).thenReturn("2");
        Mockito.doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            int pageNumber = (Integer)arguments[1];
            Assert.assertEquals(2, pageNumber);
            return null;
        }).when(request).setAttribute(Mockito.eq("currentPage"), Mockito.any());

        String processResult = catalogViewCommand.process(request, response);
        String expectedResult = Actions.CATALOG_PAGE;
        Assert.assertEquals(expectedResult, processResult);
    }

    @Test
    public void shouldGetAdminCatalogPageIfUserRoleEqAdmin() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_ADMIN);
        String processResult = catalogViewCommand.process(request, response);
        String expectedResult = Actions.ADMIN_CATALOG_PAGE;
        Assert.assertEquals(expectedResult, processResult);
    }

    @Test
    public void shouldSetSortOptionAsSessionAttributeIfSelected() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter("options")).thenReturn(Parameters.SORT_BY_NAME_AZ);

        catalogViewCommand.process(request, response);
        Mockito.verify(session).setAttribute(Parameters.SORT_OPTION, Parameters.SORT_BY_NAME_AZ);
    }

    @Test
    public void shouldPrintAlertAndReturnNullWhenPriceIsIncorrect() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(session.getAttribute(Parameters.PRICE_FROM)).thenReturn(-1);
        String processResult;
        String expectedPrint = "<script type='text/javascript'>alert('Incorrect price input');"
                + "location='" + request.getContextPath() + Actions.CATALOG_ACTION + "'</script>"
                + System.lineSeparator();

        processResult = catalogViewCommand.process(request, response);
        Assert.assertNull(processResult);
        Assert.assertEquals(expectedPrint, out.toString());

        out.reset();
        Mockito.when(session.getAttribute(Parameters.PRICE_FROM)).thenReturn(10);
        Mockito.when(session.getAttribute(Parameters.PRICE_TO)).thenReturn(5);

        processResult = catalogViewCommand.process(request, response);
        Assert.assertNull(processResult);
        Assert.assertEquals(expectedPrint, out.toString());
    }

    @Test
    public void shouldGetPropertiesAndPriceIfActionIsCorrect() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.PRICE_FROM)).thenReturn("1");
        Mockito.when(request.getParameter(Parameters.PRICE_TO)).thenReturn("20000");
        Mockito.when(request.getParameter(Parameters.ACTION)).thenReturn("selectByProperties");
        Mockito.when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList("Color")));
        Mockito.when(request.getParameterValues("Color")).thenReturn(new String[]{"grey"});

        catalogViewCommand.process(request, response);
        Mockito.verify(request, Mockito.times(2)).getParameter(Parameters.PRICE_FROM);
        Mockito.verify(request, Mockito.times(2)).getParameter(Parameters.PRICE_TO);
        Mockito.verify(request).getParameterNames();
        Mockito.verify(request).getParameterValues("Color");
    }

    @Test
    public void shouldGetProductsSortedByNameAZIfAppropriateReqParamExist() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.PRICE_TO)).thenReturn("100");
        Mockito.when(request.getParameter("options")).thenReturn(Parameters.SORT_BY_NAME_AZ);
        Mockito.doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            products = (List<Product>) arguments[1];
            Assert.assertTrue(products.get(0).getName().compareTo(products.get(1).getName()) <= 0);
            return null;
        }).when(session).setAttribute(Mockito.eq(Parameters.PRODUCTS), Mockito.any());
        catalogViewCommand.process(request, response);
    }

    @Test
    public void shouldGetProductsSortedByNameZAIfAppropriateReqParamExist() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.PRICE_TO)).thenReturn("100");
        Mockito.when(request.getParameter("options")).thenReturn(Parameters.SORT_BY_NAME_ZA);
        Mockito.doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            products = (List<Product>) arguments[1];
            Assert.assertTrue(products.get(0).getName().compareTo(products.get(1).getName()) >= 0);
            return null;
        }).when(session).setAttribute(Mockito.eq(Parameters.PRODUCTS), Mockito.any());
        catalogViewCommand.process(request, response);
    }

    @Test
    public void shouldGetProductsSortedByPriceLowHighIfAppropriateReqParamExist() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.PRICE_TO)).thenReturn("100");
        Mockito.when(request.getParameter("options")).thenReturn(Parameters.SORT_PRICE_LOW_HIGH);
        Mockito.doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            products = (List<Product>) arguments[1];
            Assert.assertTrue(products.get(0).getPrice() <= products.get(1).getPrice());
            return null;
        }).when(session).setAttribute(Mockito.eq(Parameters.PRODUCTS), Mockito.any());
        catalogViewCommand.process(request, response);
    }

    @Test
    public void shouldGetProductsSortedByPriceHighLowIfAppropriateReqParamExist() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.PRICE_TO)).thenReturn("100");
        Mockito.when(request.getParameter("options")).thenReturn(Parameters.SORT_PRICE_HIGH_LOW);
        Mockito.doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            products = (List<Product>) arguments[1];
            Assert.assertTrue(products.get(0).getPrice() >= products.get(1).getPrice());
            return null;
        }).when(session).setAttribute(Mockito.eq(Parameters.PRODUCTS), Mockito.any());
        catalogViewCommand.process(request, response);
    }

    @Test
    public void shouldGetProductsSortedByNewestIfAppropriateReqParamExist() throws ServletException, IOException {
        Mockito.when(session.getAttribute(Parameters.ROLE)).thenReturn(DBConstants.USER_GUEST);
        Mockito.when(request.getParameter(Parameters.PRICE_TO)).thenReturn("100");
        Mockito.when(request.getParameter("options")).thenReturn(Parameters.SORT_NEWEST);
        Mockito.doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            products = (List<Product>) arguments[1];
            Assert.assertTrue(products.get(0).getCreationDate().getTime()
                    <= products.get(1).getCreationDate().getTime());
            return null;
        }).when(session).setAttribute(Mockito.eq(Parameters.PRODUCTS), Mockito.any());
        catalogViewCommand.process(request, response);
    }
}
