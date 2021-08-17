package model;

import model.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductDao {
    private final DBManager dbManager = DBManager.getInstance();

    private static final String SQL_INSERT_PROPERTY = "INSERT INTO property(productId, propertyNameEn, " +
            "propertyValueEn, propertyNameUk, propertyValueUk) VALUES(?,?,?,?,?)";
    private static final String SQL_INSERT_PRODUCT = "INSERT INTO product(nameEn, nameUk, price, creationDate) " +
            "VALUES (?, ?, ?, NOW())";

    private final String sqlSelectAllProducts;
    private final String sqlSelectProductProperties;
    private final String sqlSelectDistinctPropertyNames;


    private final double currencyRatio;

    public ProductDao(String locale){
        switch (locale){
            case "uk":
                sqlSelectAllProducts = "SELECT id, nameUk as name, price, creationDate FROM product ORDER BY ";
                sqlSelectProductProperties = "SELECT propertyNameUk as propertyName, propertyValueUk " +
                        "as propertyValue FROM property WHERE productId = ?";
                sqlSelectDistinctPropertyNames = "SELECT DISTINCT propertyNameUk as propertyName FROM property";
                currencyRatio = 27;
                break;
            default:
                sqlSelectAllProducts = "SELECT id, nameEn as name, price, creationDate FROM product ORDER BY ";
                sqlSelectProductProperties = "SELECT propertyNameEn as propertyName, propertyValueEn " +
                        "as propertyValue FROM property WHERE productId = ?";
                sqlSelectDistinctPropertyNames = "SELECT DISTINCT propertyNameEn as propertyName FROM property";
                currencyRatio = 1;
                break;
        }
    }


    public List<Product> selectAllProducts(String column, String order){
        Connection con = null;
        PreparedStatement selectAllProd = null;
        PreparedStatement selectPropertiesForProd = null;
        List<Product> products = new LinkedList<>();
        try {
            con = dbManager.getConnection();
            selectAllProd = con.prepareStatement(sqlSelectAllProducts + column + " " + order);
            ResultSet rsProducts = selectAllProd.executeQuery();
            EntityMapper<Product> pm = new ProductMapper();
            selectPropertiesForProd = con.prepareStatement(sqlSelectProductProperties);
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
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            products = null;
            //throw custom exception
        }
        return products;
    }

    public Set<String> selectDistinctPropertyNames(){
        Connection con = null;
        PreparedStatement pstmnt = null;
        Set<String> property = new HashSet<>();
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(sqlSelectDistinctPropertyNames);
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                property.add(rs.getString(1));
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            property = null;
            //throw custom exception
        }
        return property;
    }

    public boolean insertProductWithProperties(String nameEn, String nameUk, int price, String propNameEn, String propNameUk, String propValueEn, String propValueUk){
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean inserted = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_INSERT_PRODUCT);
            pstmnt.setString(1, nameEn);
            pstmnt.setString(2, nameUk);
            pstmnt.setInt(3, price);
            pstmnt.executeUpdate();

            while(rs.next()){
                property.add(rs.getString(1));
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            dbManager.rollbackAndClose(con);
            property = null;
            //throw custom exception
        }
        return property;

    }

    public class ProductMapper implements EntityMapper<Product>{
        @Override
        public Product mapRow(ResultSet rs) {
            try {
                Product product = new Product();
                product.setId(rs.getInt(DBConstants.ENTITY_ID));
                product.setName(rs.getString(DBConstants.PRODUCT_NAME));
                product.setPrice(rs.getDouble(DBConstants.PRODUCT_PRICE) * currencyRatio);
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
