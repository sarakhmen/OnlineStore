package model;

import model.entity.Order;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderDao {
    private static final Logger log = Logger.getLogger(OrderDao.class);
    private final DBManager dbManager = DBManager.getInstance();

    private static final String SQL_SELECT_COUNT_OF_RECORDS = "SELECT COUNT(*) FROM bag WHERE userId=?";
    private static final String SQL_INSERT_ORDER = "INSERT INTO bag(userId, productId) values(?, ?)";
    private static final String SQL_DELETE_ORDER = "DELETE FROM bag WHERE id=?";
    private static final String SQL_UPDATE_ORDER_STATUS = "UPDATE bag SET status=? WHERE id=?";
    private static final String SQL_TRANSFER_ORDERS = "UPDATE bag SET userId=? WHERE userId=?";

    private final String sqlSelectAllOrders;
    private final double currencyRatio;

    private int numberOfRecords;

    public OrderDao(String locale){
        switch (locale){
            case "uk":
                sqlSelectAllOrders = "SELECT bag.id, product.nameUk as name, product.price, bag.status " +
                        "FROM product JOIN bag ON bag.productId=product.id WHERE bag.userId = ?";
                currencyRatio = 27;
                break;
            default:
                sqlSelectAllOrders = "SELECT bag.id, product.nameEn as name, product.price, bag.status " +
                        "FROM product JOIN bag ON bag.productId=product.id WHERE bag.userId = ?";
                currencyRatio = 1;
                break;
        }
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public List<Order> selectAllOrders(int userId, int offset, int rowcount){
        Connection con = null;
        PreparedStatement selectAllOrders = null;
        List<Order> orders = new LinkedList<>();
        try {
            con = dbManager.getConnection();
            PreparedStatement selectCountOfRecords = con.prepareStatement(SQL_SELECT_COUNT_OF_RECORDS);
            selectCountOfRecords.setInt(1, userId);
            ResultSet rsCount = selectCountOfRecords.executeQuery();
            if(rsCount.next()){
                numberOfRecords = rsCount.getInt(1);
            }

            selectAllOrders = con.prepareStatement(sqlSelectAllOrders + " LIMIT " + offset + ", " + rowcount);
            selectAllOrders.setInt(1, userId);
            ResultSet rsOrders = selectAllOrders.executeQuery();
            EntityMapper<Order> om = new OrderMapper();
            while(rsOrders.next()){
                Order order = om.mapRow(rsOrders);
                orders.add(order);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            orders = null;
            //throw custom exception
        }
        return orders;
    }

    public boolean insertOrder(int userId, int productId){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean inserted = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_INSERT_ORDER);
            pstmnt.setInt(1, userId);
            pstmnt.setInt(2, productId);
            pstmnt.executeUpdate();
            con.commit();
            con.close();
            inserted = true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return inserted;
    }

    public boolean deleteOrder(int orderId){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean deleted = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_DELETE_ORDER);
            pstmnt.setInt(1, orderId);
            pstmnt.executeUpdate();
            con.commit();
            con.close();
            deleted = true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return deleted;
    }

    public boolean updateOrderStatus(int orderId, String newStatus){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean updated = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_UPDATE_ORDER_STATUS);
            pstmnt.setString(1, newStatus);
            pstmnt.setInt(2, orderId);
            pstmnt.executeUpdate();
            con.commit();
            con.close();
            updated = true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return updated;
    }

    public boolean transferOrders(int idFrom, int idTo){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean transferred = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_TRANSFER_ORDERS);
            pstmnt.setInt(1, idTo);
            pstmnt.setInt(2, idFrom);
            pstmnt.executeUpdate();
            con.commit();
            con.close();
            transferred = true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return transferred;
    }

    public class OrderMapper implements EntityMapper<Order>{
        @Override
        public Order mapRow(ResultSet rs) {
            try {
                Order order = new Order();
                order.setId(rs.getInt(DBConstants.ENTITY_ID));
                order.setName(rs.getString(DBConstants.PRODUCT_NAME));
                order.setPrice(rs.getDouble(DBConstants.PRODUCT_PRICE) * currencyRatio);
                order.setStatus(rs.getString(DBConstants.BAG_STATUS));
                return order;
            } catch (SQLException e) {
                log.error(e.getMessage());
                throw new IllegalStateException(e);
            }
        }
    }
}
