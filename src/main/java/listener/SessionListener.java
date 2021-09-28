package listener;

import util.Parameters;
import util.DBConstants;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Assigns 'GUEST' value to user role
 */
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute(Parameters.ROLE, DBConstants.USER_GUEST);
    }

}
