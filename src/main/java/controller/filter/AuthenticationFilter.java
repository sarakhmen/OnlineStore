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

        System.out.println("in auth filter");

        HttpSession session = httpRequest.getSession();
        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        path = path.replaceAll(".*" + contextPath + "/", "");
        System.out.println("uri = " + path);
        String userRole = (String)session.getAttribute(Parameters.ROLE);
        if(path.contains("admin") && !userRole.equals(DBConstants.USER_ADMIN)){
            httpResponse.sendRedirect(httpRequest.getContextPath() + Actions.LOGIN_PAGE);
        }
        else if((!path.contains("/") || path.contains("index.jsp")) && !userRole.equals(DBConstants.USER_GUEST)){
            httpResponse.sendRedirect(httpRequest.getContextPath() + Actions.CATALOG_ACTION);
        }
        else if(path.contains("orderStatus") && userRole.equals(DBConstants.USER_GUEST)){
            httpResponse.sendRedirect(httpRequest.getContextPath() + Actions.LOGIN_PAGE);
        }
        else {
            chain.doFilter(request, response);
        }

//
//        String userRole = (String)session.getAttribute(Parameters.ROLE);
//        String requestUri = httpRequest.getRequestURI();
//        if(requestUri.contains("/admin") && !userRole.equals(DBConstants.USER_ADMIN)){
//            httpResponse.sendRedirect(httpRequest.getContextPath() + Actions.LOGIN_PAGE);
//        }
//        else{
//            chain.doFilter(request, response);
//        }





//
//        final HttpSession session = req.getSession();
//
//        if (session != null && session.getAttribute("user") != null && session.getAttribute("userRole") != null) {
//

//            return;
//
//        }
//
//        response.getWriter().write(notifyAccessDenied());
    }

    private String notifyAccessDenied() {
        return "<script>" + "alert('You should sign In!');" + "window.location = 'http://localhost:8080/publicationView?command=publication';" + "</script>";
    }

}
