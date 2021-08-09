package controller.filter;

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
//        final HttpServletRequest httpRequest = (HttpServletRequest) request;
//        final HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        final HttpSession session = req.getSession();
//
//        if (session != null && session.getAttribute("user") != null && session.getAttribute("userRole") != null) {
//
            chain.doFilter(request, response);
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
