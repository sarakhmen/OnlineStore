package model;

import model.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductDao {
    private final DBManager dbManager = DBManager.getInstance();

    private static final String SQL_SELECT_ALL_PRODUCTS = "SELECT * FROM product ORDER BY ";
    private static final String SQL_SELECT_PRODUCT_PROPERTIES = "SELECT propertyName, propertyValue FROM property " +
            "WHERE productId = ?";
    private static final String SQL_SELECT_DISTINCT_PROPERTY_NAMES = "SELECT DISTINCT propertyName FROM property";

    public List<Product> selectAllProducts(String column, String order){
        Connection con = null;
        PreparedStatement selectAllProd = null;
        PreparedStatement selectPropertiesForProd = null;
        List<Product> products = new LinkedList<>();
        try {
            con = dbManager.getConnection();
            selectAllProd = con.prepareStatement(SQL_SELECT_ALL_PRODUCTS + column + " " + order);
            ResultSet rsProducts = selectAllProd.executeQuery();
            EntityMapper<Product> pm = new ProductMapper();
            selectPropertiesForProd = con.prepareStatement(SQL_SELECT_PRODUCT_PROPERTIES);
            while(rsProducts.next()){
                Product product = pm.mapRow(rsProducts);
                selectPropertiesForProd.setInt(1, product.getId());
                ResultSet rsProperties = selectPropertiesForProd.executeQuery();
                while(rsProperties.next()){
                    String propName = rsProperties.getString(1);
                    String propValue = rsProperties.getString(2);
                    product.getProperties().put(propName, propValue);
                }
                products.add(product);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //throw custom exception
        }
        finally {
            dbManager.closeStatement(selectPropertiesForProd);
            dbManager.closeStatement(selectAllProd);
            dbManager.closeConnection(con);
        }
        return products;
    }

    public Set<String> selectDistinctPropertyNames(){
        Connection con = null;
        PreparedStatement pstmnt = null;
        Set<String> property = new HashSet<>();
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_SELECT_DISTINCT_PROPERTY_NAMES);
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                property.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //throw custom exception
        }
        finally {
            dbManager.closeStatement(pstmnt);
            dbManager.closeConnection(con);
        }
        return property;
    }


    public static class ProductMapper implements EntityMapper<Product>{
        @Override
        public Product mapRow(ResultSet rs) {
            try {
                Product product = new Product();
                product.setId(rs.getInt(DBConstants.ENTITY_ID));
                product.setName(rs.getString(DBConstants.PRODUCT_NAME));
                product.setPrice(rs.getInt(DBConstants.PRODUCT_PRICE));
                product.setCreationDate(rs.getDate(DBConstants.PRODUCT_CREATION_DATE));
                product.setProperties(new HashMap<>());
                return product;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
    }
}
