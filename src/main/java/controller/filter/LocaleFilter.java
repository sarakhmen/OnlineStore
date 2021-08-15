package controller.filter;

import controller.Parameters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class        LocaleFilter implements Filter {
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        String locale = (String)session.getAttribute(Parameters.LOCALE);

        if (req.getParameter("sessionLocale") != null) {
            req.getSession().setAttribute("locale", req.getParameter("sessionLocale"));
        }
        else if(locale == null){
            session.setAttribute(Parameters.LOCALE, "en");
        }

        chain.doFilter(request, response);
    }
}
