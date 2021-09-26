package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;
import model.DBManager;
import model.OrderDao;
import model.UserDao;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserStatusCommand implements Command{
    private static final Logger log = Logger.getLogger(UserStatusCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userRole = request.getParameter(Parameters.ROLE);
        if(userRole.equals(DBConstants.USER_ADMIN)){
            return "redirect:" + request.getContextPath() + Actions.ADMIN_MANAGEMENT_ACTION;
        }

        String newBlockStatus = request.getParameter(Parameters.NEW_BLOCK_STATUS);
        int userId = Integer.parseInt(request.getParameter(Parameters.USER_ID));
        UserDao userDao = new UserDao(DBManager.getInstance());
        userDao.updateUserStatus(userId, newBlockStatus.equals(DBConstants.USER_BLOCKED));

        return "redirect:" + request.getContextPath() + Actions.ADMIN_MANAGEMENT_ACTION;
    }
}