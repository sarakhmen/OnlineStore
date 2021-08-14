package controller.listener;

import controller.Parameters;
import model.DBConstants;
import model.UserDao;
import model.entity.User;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setAttribute(Parameters.ROLE, DBConstants.USER_GUEST);

//        UserDao userDao = new UserDao();
//        User guest = userDao.insertUser(null, null, null, DBConstants.USER_GUEST);
//        session.setAttribute(Parameters.USER_ID, guest.getId());
//        session.setAttribute(Parameters.ROLE, guest.getRole());
    }
}
