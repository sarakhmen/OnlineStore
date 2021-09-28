package filter;

import util.Parameters;
import util.DBConstants;
import database.DBManager;
import dao.UserDao;
import model.User;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GuestFilter implements Filter {
    private static final String GUEST_LOGIN = "guestLogin";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpSession session = httpRequest.getSession();
        if(session.getAttribute(Parameters.USER_ID) == null){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            UserDao userDao = new UserDao(DBManager.getInstance());
            Cookie[] cookies = httpRequest.getCookies();
            Cookie cookie = null;
            if(cookies != null){
                for(Cookie c: cookies){
                    if(GUEST_LOGIN.equals(c.getName())){
                        cookie = c;
                        break;
                    }
                }
            }

            Cookie newCookie = new Cookie(GUEST_LOGIN, session.getId());
            newCookie.setMaxAge(60*60*24*30);
            if(cookie == null){
                httpResponse.addCookie(newCookie);
            }
            else{
                String guestLogin = cookie.getValue();
                userDao.deleteUserByLogin(guestLogin);
                httpResponse.addCookie(newCookie);
            }
            User guest = userDao.insertUser(session.getId(), null, null, DBConstants.USER_GUEST);
            session.setAttribute(Parameters.USER_ID, guest.getId());
            session.setAttribute(Parameters.ROLE, guest.getRole());
        }

        chain.doFilter(request, response);
    }
}