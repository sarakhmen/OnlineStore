package model;

import model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDao {
    private final DBManager dbManager = DBManager.getInstance();

    private static final String SQL_SELECT_ALL_USERS = "SELECT * FROM user";
    private static final String SQL_SELECT_USER_BY_LOGIN = "SELECT * FROM user WHERE login=?";
    private static final String SQL_INSERT_USER = "INSERT INTO user(login, password, name, role) values(?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER_STATUS = "UPDATE user SET blocked=? WHERE id=?";
    private static final String SQL_DELETE_USER_BY_LOGIN = "DELETE FROM user WHERE login=?";

    public User selectUserByLogin(String login){
        Connection con = null;
        PreparedStatement pstmnt = null;
        User user = null;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_SELECT_USER_BY_LOGIN);
            pstmnt.setString(1, login);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next()){
                EntityMapper<User> mapper = new UserMapper();
                user = mapper.mapRow(rs);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            user = null;
        }
        return user;
    }

    public boolean isRegistered(String login){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean registered = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_SELECT_USER_BY_LOGIN);
            pstmnt.setString(1, login);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next()){
                con.commit();
                con.close();
                registered = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
        }
        return registered;
    }

    public User insertUser(String login, String password, String userName){
        return insertUser(login, password, userName, DBConstants.USER_USER);
    }

    public User insertUser(String login, String password, String userName, String role){
        Connection con = null;
        PreparedStatement pstmnt = null;
        User user = null;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_INSERT_USER);
            pstmnt.setString(1, login);
            pstmnt.setString(2, password);
            pstmnt.setString(3, userName);
            pstmnt.setString(4, role);
            pstmnt.executeUpdate();
            pstmnt = con.prepareStatement(SQL_SELECT_USER_BY_LOGIN);
            pstmnt.setString(1, login);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next()){
                EntityMapper<User> mapper = new UserMapper();
                user = mapper.mapRow(rs);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            user = null;
        }
        return user;
    }

    public List<User> selectAllUsers(){
        Connection con = null;
        PreparedStatement pstmnt = null;
        List<User> users = new LinkedList<>();
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_SELECT_ALL_USERS);
            ResultSet rs = pstmnt.executeQuery();
            EntityMapper<User> userMapper = new UserMapper();
            while(rs.next()){
                User user = userMapper.mapRow(rs);
                users.add(user);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            users = null;
            //throw custom exception
        }
        return users;
    }

    public boolean updateUserStatus(int userId, boolean blocked){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean updated = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_UPDATE_USER_STATUS);
            pstmnt.setBoolean(1, blocked);
            pstmnt.setInt(2, userId);
            pstmnt.executeUpdate();
            con.commit();
            con.close();
            updated = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            //throw custom exception
        }
        return updated;
    }

    public boolean deleteUserByLogin(String login){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean deleted = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_DELETE_USER_BY_LOGIN);
            pstmnt.setString(1, login);
            pstmnt.executeUpdate();
            con.commit();
            con.close();
            deleted = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            //throw custom exception
        }
        return deleted;
    }
    public static class UserMapper implements EntityMapper<User>{
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
