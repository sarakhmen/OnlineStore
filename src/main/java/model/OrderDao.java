package model;

import model.entity.Order;
import model.entity.Product;
import model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderDao {
    private final DBManager dbManager = DBManager.getInstance();

    private static final String SQL_SELECT_ALL_ORDERS = "SELECT bag.id, product.name, product.price, product.status " +
            "FROM product JOIN bag ON bag.productId=product.id WHERE bag.userId = ?";
    private static final String SQL_INSERT_ORDER = "INSERT INTO bag(userId, productId) values(?, ?);";

    public List<Order> selectAllOrders(int userId){
        Connection con = null;
        PreparedStatement selectAllOrders = null;
        List<Order> orders = new LinkedList<>();
        try {
            con = dbManager.getConnection();
            selectAllOrders = con.prepareStatement(SQL_SELECT_ALL_ORDERS);
            selectAllOrders.setInt(1, userId);
            ResultSet rsOrders = selectAllOrders.executeQuery();
            EntityMapper<Order> om = new OrderMapper();
            while(rsOrders.next()){
                Order order = om.mapRow(rsOrders);
                orders.add(order);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //throw custom exception
        }
        finally {
            dbManager.closeStatement(selectAllOrders);
            dbManager.closeConnection(con);
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
            if(pstmnt.executeUpdate() != 0){
                inserted = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            dbManager.closeStatement(pstmnt);
            dbManager.closeConnection(con);
        }
        return inserted;
    }

    public static class OrderMapper implements EntityMapper<Order>{
        @Override
        public Order mapRow(ResultSet rs) {
            try {
                Order order = new Order();
                order.setId(rs.getInt(DBConstants.ENTITY_ID));
                order.setName(rs.getString(DBConstants.PRODUCT_NAME));
                order.setPrice(rs.getInt(DBConstants.PRODUCT_PRICE));
                order.setStatus(rs.getString(DBConstants.PRODUCT_STATUS));
                return order;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
    }
}
