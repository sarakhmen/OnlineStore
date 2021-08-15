package controller.command;

import controller.Actions;
import controller.Parameters;
import model.DBConstants;
import model.OrderDao;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserStatusCommand implements Command{

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userRole = request.getParameter(Parameters.ROLE);
        if(userRole.equals(DBConstants.USER_ADMIN)){
            return "redirect:" + request.getContextPath() + Actions.ADMIN_MANAGEMENT_ACTION;
        }

        String newBlockStatus = request.getParameter(Parameters.NEW_BLOCK_STATUS);
        int userId = Integer.parseInt(request.getParameter(Parameters.USER_ID));
        String locale = (String)session.getAttribute(Parameters.LOCALE);
        UserDao userDao = new UserDao(locale);
        if(newBlockStatus.equals(DBConstants.USER_BLOCKED)){
            userDao.updateUserStatus(userId, true);
        }
        else{
            userDao.updateUserStatus(userId, false);
        }

        return "redirect:" + request.getContextPath() + Actions.ADMIN_MANAGEMENT_ACTION;
    }
}