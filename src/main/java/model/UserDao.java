package model;

import model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final DBManager dbManager = DBManager.getInstance();

    private static final String SQL_SELECT_USER_BY_LOGIN = "SELECT * FROM user WHERE login=?";
    private static final String SQL_PUT_USER = "INSERT INTO user(login, password, name) values(?, ?, ?)";
    private static final String SQL_SET_USER_ROLE = "UPDATE user SET role=? WHERE login=?";


    public User findUserByLogin(String login){
        Connection con = null;
        PreparedStatement pstmnt = null;
        User user = null;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_SELECT_USER_BY_LOGIN);
            pstmnt.setString(1, login);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next()){
                UserMapper um = new UserMapper();
                user = um.mapRow(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            dbManager.closeStatement(pstmnt);
            dbManager.closeConnection(con);
        }
        return user;
    }

    public boolean putUser(String login, String password, String name){
        return true;
    }

    public boolean setRole(String login, String role){
        return true;
    }

    private class UserMapper implements EntityMapper<User>{
        @Override
        public User mapRow(ResultSet rs) {
            try {
                User user = new User();
                user.setId(rs.getInt(DBConstants.ENTITY_ID));
                user.setLogin(rs.getString(DBConstants.USER_LOGIN));
                user.setPassword(rs.getString(DBConstants.USER_PASSWORD));
                user.setName(rs.getString(DBConstants.USER_NAME));
                user.setRole(rs.getString(DBConstants.USER_ROLE));
                user.setBlocked(rs.getBoolean(DBConstants.USER_STATUS));
                return user;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
    }
}
