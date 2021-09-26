package model;

import model.entity.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDao {
    private static final Logger log = Logger.getLogger(UserDao.class);
    private DBManager dbManager = DBManager.getInstance();

    private static final String SQL_SELECT_COUNT_OF_NON_GUESTS = "SELECT COUNT(*) FROM user WHERE role!='" +
            DBConstants.USER_GUEST + "'";
    private static final String SQL_SELECT_USER_NAME = "SELECT name FROM user WHERE id=?";
    private static final String SQL_SELECT_USER_BLOCK_STATUS = "SELECT blocked FROM user WHERE id=?";
    private static final String SQL_INSERT_USER = "INSERT INTO user(login, password, name, role) " +
            "VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER_STATUS = "UPDATE user SET blocked=? WHERE id=?";
    private static final String SQL_DELETE_USER_BY_LOGIN = "DELETE FROM user WHERE login=?";

    private static final String SQL_SELECT_ALL_USERS_EXCEPT_GUESTS = "SELECT id, login, password, name, role, blocked FROM user " +
            "WHERE role!='" + DBConstants.USER_GUEST + "'";
    private static final String SQL_SELECT_USER_BY_LOGIN = "SELECT id, login, password, name, role, blocked FROM user " +
            "WHERE login=?";

    private int numberOfRecords;

    public UserDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public int getNumberOfRecords(){
        return numberOfRecords;
    }

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
            log.error(ex.getMessage());
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
                registered = true;
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return registered;
    }

    public boolean isBlocked(int id){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean blocked = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_SELECT_USER_BLOCK_STATUS);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next()){
                blocked = rs.getBoolean(DBConstants.USER_STATUS);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return blocked;
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
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            user = null;
        }
        return user;
    }

    public List<User> selectAllUsersExceptGuests(int offset, int rowcount){
        Connection con = null;
        PreparedStatement pstmnt = null;
        List<User> users = new LinkedList<>();
        try {
            con = dbManager.getConnection();
            PreparedStatement selectCountOfRecords = con.prepareStatement(SQL_SELECT_COUNT_OF_NON_GUESTS);
            ResultSet rsCount = selectCountOfRecords.executeQuery();
            if (rsCount.next()) {
                numberOfRecords = rsCount.getInt(1);
            }
            pstmnt = con.prepareStatement(SQL_SELECT_ALL_USERS_EXCEPT_GUESTS);
            ResultSet rs = pstmnt.executeQuery();
            EntityMapper<User> userMapper = new UserMapper();
            while(rs.next()){
                User user = userMapper.mapRow(rs);
                users.add(user);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            users = null;
            //throw custom exception
        }
        return users;
    }

    public String selectUserName(int id){
        Connection con = null;
        PreparedStatement pstmnt = null;
        String name = null;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_SELECT_USER_NAME);
            pstmnt.setInt(1, id);
            ResultSet rs = pstmnt.executeQuery();
            if(rs.next()){
                name = rs.getString(DBConstants.USER_NAME);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            name = null;
            //throw custom exception
        }
        return name;
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
            log.error(ex.getMessage());
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
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            //throw custom exception
        }
        return deleted;
    }

    public class UserMapper implements EntityMapper<User>{
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
                log.error(e.getMessage());
                throw new IllegalStateException(e);
            }
        }
    }
}
