package controller.filter;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;
import model.UserDao;
import model.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        String locale = (String)session.getAttribute(Parameters.LOCALE);
        UserDao userDao = new UserDao(locale);

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        path = path.replaceAll(".*" + contextPath + "/", "");

        String userRole = (String)session.getAttribute(Parameters.ROLE);
        boolean guest = userRole.equals(DBConstants.USER_GUEST);
        boolean admin = userRole.equals(DBConstants.USER_ADMIN);
        System.out.println("in auth filter");
        if(path.contains("admin") && !admin){
            httpResponse.sendRedirect(httpRequest.getContextPath() + Actions.LOGIN_PAGE);
        }
        else if((!path.contains("/") || path.contains("index.jsp")) && !guest){
            System.out.println(userRole);
            System.out.println("redirected not guest");
            httpResponse.sendRedirect(httpRequest.getContextPath() + Actions.CATALOG_ACTION);
        }
        else if(path.contains("orderStatus") && guest){
            httpResponse.sendRedirect(httpRequest.getContextPath() + Actions.LOGIN_PAGE);
        }
        else if(path.contains("main") && !guest && userDao.isBlocked((int)session.getAttribute(Parameters.USER_ID))){
            httpRequest.setAttribute(Parameters.ERROR, "Your account is blocked!");
            session.invalidate();
            httpRequest.getRequestDispatcher(Actions.ERROR_PAGE).forward(request, response);
        }
        else {
            chain.doFilter(request, response);
        }
    }

}
