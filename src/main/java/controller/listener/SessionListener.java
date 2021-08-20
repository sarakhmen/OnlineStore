package controller.listener;

import controller.Parameters;
import model.DBConstants;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute(Parameters.ROLE, DBConstants.USER_GUEST);
    }

}
