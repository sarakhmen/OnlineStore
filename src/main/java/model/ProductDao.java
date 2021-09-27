package model;

import controller.Parameters;
import model.entity.Product;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ProductDao {
    private static final Logger log = Logger.getLogger(ProductDao.class);
    private DBManager dbManager;


    private static final String SQL_SELECT_COUNT_OF_RECORDS = "SELECT COUNT(*) FROM product";
    private static final String SQL_SELECT_PRODUCT_ID_WITH_SPECIFIED_PROPS = "SELECT productId FROM property WHERE ";
    private static final String SQL_INSERT_PROPERTY = "INSERT INTO property(productId, propertyName, " +
            "propertyValue) VALUES(?,?,?)";
    private static final String SQL_INSERT_PRODUCT = "INSERT INTO product(name, price, creationDate) " +
            "VALUES (?, ?, NOW())";
    private static final String SQL_SELECT_LAST_INSERT_ID = "SELECT last_insert_id()";
    private static final String SQL_DELETE_PRODUCT_BY_ID = "DELETE FROM product WHERE id=?";
    private static final String SQL_SELECT_PRODUCT_BY_ID = "SELECT id, name, price, creationDate FROM product WHERE id=?";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE product SET name=?, price=? WHERE id=?";
    private static final String SQL_DELETE_PROPERTIES_BY_PRODUCT_ID = "DELETE FROM property WHERE productId=?";

    private static final String SQL_SELECT_ALL_PRODUCTS = "SELECT id, name, price, creationDate FROM product";
    private static final String SQL_SELECT_PRODUCTS_IN = "SELECT id, name, price, creationDate FROM product " +
            "WHERE id IN ^ ^ ORDER BY ";
    private static final String SQL_SELECT_PRODUCT_PROPERTIES = "SELECT propertyName, propertyValue " +
            " FROM property WHERE productId = ?";
    private static final String SQL_SELECT_DISTINCT_PROPERTY_NAMES = "SELECT DISTINCT propertyName FROM property";
    private static final String SQL_SELECT_PROPERTY_VALUES = "SELECT DISTINCT propertyValue FROM property " +
            "WHERE propertyName=?";

    private int numberOfRecords;

    public ProductDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Product> selectProductsOrderedAndByProperties(String column, String order, Map<String,
            Set<String>> properties, int offset, int rowcount, Integer priceFrom, Integer priceTo){

        if(priceFrom == null && priceTo == null){
            return selectProductsOrderedAndByProperties("", column, order, properties, offset, rowcount);
        }

        if(priceFrom == null){
            return selectProductsOrderedAndByProperties("price <= " + priceTo + " ",
                    column, order, properties, offset, rowcount);
        }

        if(priceTo == null){
            return selectProductsOrderedAndByProperties("price >= " + priceFrom + " ",
                    column, order, properties, offset, rowcount);
        }

        return selectProductsOrderedAndByProperties("price BETWEEN " + priceFrom + " AND "
                + priceTo + " ", column, order, properties, offset, rowcount);
    }

    public List<Product> selectProductsOrderedAndByProperties(String sqlPriceRange, String column, String order, Map<String,
            Set<String>> properties, int offset, int rowcount){

        Connection con = null;
        List<Product> products = null;
        try {
            con = dbManager.getConnection();
            if(properties.isEmpty()){
                String sqlSelectCountOfRecords = SQL_SELECT_COUNT_OF_RECORDS;
                String prefix = "";
                if(!sqlPriceRange.equals("")){
                    prefix = " WHERE ";
                    sqlSelectCountOfRecords += " WHERE " + sqlPriceRange;
                }
                products = selectProductsWithProperties(con, SQL_SELECT_ALL_PRODUCTS
                        + prefix + sqlPriceRange + " ORDER BY " + column + " " + order + " LIMIT "
                        + offset + ", " + rowcount);

                PreparedStatement selectCountOfRecords = con.prepareStatement(sqlSelectCountOfRecords);
                ResultSet rsCount = selectCountOfRecords.executeQuery();
                if (rsCount.next()) {
                    numberOfRecords = rsCount.getInt(1);
                }
            }
            else{
                int i = 0;
                StringBuilder sb1 = new StringBuilder();
                for(Map.Entry<String, Set<String>> property : properties.entrySet()) {
                    sb1.append(" AND productId IN (" + SQL_SELECT_PRODUCT_ID_WITH_SPECIFIED_PROPS)
                            .append('(');
                    StringBuilder sb2 = new StringBuilder();
                    for (String value : property.getValue()) {
                        sb2.append(" || ")
                                .append('(')
                                .append(DBConstants.PROPERTY_NAME)
                                .append('=')
                                .append("'")
                                .append(property.getKey())
                                .append("'")
                                .append(" && ")
                                .append(DBConstants.PROPERTY_VALUE)
                                .append('=')
                                .append("'")
                                .append(value)
                                .append("'")
                                .append(')');
                    }
                    sb1.append(sb2.substring(4));
                    sb1.append(')');
                    if(i == 0){
                        i++;
                    }
                    else{
                        sb1.append(')');
                    }
                }
                String selectQuery = sb1.substring(19) + " LIMIT " + offset + ", " + rowcount;
                PreparedStatement selectProductIdWithSpecifiedProps =
                        con.prepareStatement(selectQuery);
                ResultSet rsProductIds = selectProductIdWithSpecifiedProps.executeQuery();
                List<Integer> productIds = new LinkedList<>();
                while(rsProductIds.next()){
                    productIds.add(rsProductIds.getInt(1));
                }
                if(!productIds.isEmpty()){
                    String enclosedInParenthesisIds = productIds.stream().map(Object::toString).collect(Collectors
                            .joining(", ", "(", ")"));
                    String sqlQuery = SQL_SELECT_PRODUCTS_IN.replaceFirst("\\^", enclosedInParenthesisIds)
                            + column + " " + order;
                    if(!sqlPriceRange.equals("")){
                        sqlQuery = sqlQuery.replaceFirst("\\^", " && " + sqlPriceRange);
                    }
                    else{
                        sqlQuery = sqlQuery.replaceFirst("\\^", "");

                    }
                    products = selectProductsWithProperties(con, sqlQuery);
                }
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            products = null;
        }
        return products;
    }

    private List<Product> selectProductsWithProperties(Connection con, String sqlSelectProductQuery) throws SQLException{
        List<Product> products = new LinkedList<>();
        PreparedStatement selectAllProd = con.prepareStatement(sqlSelectProductQuery);
        ResultSet rsProducts = selectAllProd.executeQuery();
        EntityMapper<Product> pm = new ProductMapper();
        PreparedStatement selectPropertiesForProd = con.prepareStatement(SQL_SELECT_PRODUCT_PROPERTIES);
        numberOfRecords = 0;
        while (rsProducts.next()) {
            numberOfRecords++;
            Product product = pm.mapRow(rsProducts);
            selectPropertiesForProd.setInt(1, product.getId());
            ResultSet rsProperties = selectPropertiesForProd.executeQuery();
            while (rsProperties.next()) {
                String propName = rsProperties.getString(1);
                String propValue = rsProperties.getString(2);
                product.getProperties().put(propName, propValue);
            }
            products.add(product);
        }
        return products;
    }

    public int getNumberOfRecords(){
        return numberOfRecords;
    }

    public Product selectProductById(int productId){
        Connection con = null;
        PreparedStatement selectProd = null;
        PreparedStatement selectPropertiesForProd = null;
        Product product = null;
        try {
            con = dbManager.getConnection();
            selectProd = con.prepareStatement(SQL_SELECT_PRODUCT_BY_ID);
            selectProd.setInt(1, productId);
            ResultSet rsProduct = selectProd.executeQuery();
            EntityMapper<Product> pm = new ProductMapper();
            selectPropertiesForProd = con.prepareStatement(SQL_SELECT_PRODUCT_PROPERTIES);
            if (rsProduct.next()) {
               product = pm.mapRow(rsProduct);
                selectPropertiesForProd.setInt(1, productId);
                ResultSet rsProperties = selectPropertiesForProd.executeQuery();
                while (rsProperties.next()) {
                    String propName = rsProperties.getString(1);
                    String propValue = rsProperties.getString(2);
                    product.getProperties().put(propName, propValue);
                }
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            product = null;
        }
        return product;
    }

    public Map<String, Set<String>> selectProperties(){
        Connection con = null;
        PreparedStatement selectDistPropNames = null;
        Map<String, Set<String>> properties = new HashMap<>();
        try {
            con = dbManager.getConnection();
            selectDistPropNames = con.prepareStatement(SQL_SELECT_DISTINCT_PROPERTY_NAMES);
            ResultSet rs = selectDistPropNames.executeQuery();
            Set<String> propNames = new HashSet<>();
            while (rs.next()) {
                propNames.add(rs.getString(1));
            }

            PreparedStatement selectValues = con.prepareStatement(SQL_SELECT_PROPERTY_VALUES);
            for(String propName : propNames){
                Set<String> propValues = new HashSet<>();
                selectValues.setString(1, propName);
                ResultSet rsValues = selectValues.executeQuery();
                while(rsValues.next()){
                    propValues.add(rsValues.getString(1));
                }
                properties.put(propName, propValues);
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            properties = null;
        }
        return properties;
    }

    public boolean deleteProductById(int productId) {
        Connection con = null;
        PreparedStatement pstmnt = null;
        boolean deleted = false;
        try {
            con = dbManager.getConnection();
            pstmnt = con.prepareStatement(SQL_DELETE_PRODUCT_BY_ID);
            pstmnt.setInt(1, productId);
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

    public boolean insertProductWithProperties(String name, double price, String[] propNames,
                                               String[] propValues) {
        Connection con = null;
        PreparedStatement insertProductStatement = null;
        boolean inserted = false;
        try {
            con = dbManager.getConnection();
            insertProductStatement = con.prepareStatement(SQL_INSERT_PRODUCT);
            insertProductStatement.setString(1, name);
            insertProductStatement.setDouble(2, price);
            insertProductStatement.executeUpdate();
            if (propNames != null) {
                PreparedStatement selectLastInsertIdStatement = con.prepareStatement(SQL_SELECT_LAST_INSERT_ID);
                ResultSet lastInsertIdRS = selectLastInsertIdStatement.executeQuery();
                if (lastInsertIdRS.next()) {
                    int lastInsertId = lastInsertIdRS.getInt(1);
                    PreparedStatement insertProperties = con.prepareStatement(SQL_INSERT_PROPERTY);
                    for (int i = 0; i < propNames.length; i++) {
                        insertProperties.setInt(1, lastInsertId);
                        insertProperties.setString(2, propNames[i]);
                        insertProperties.setString(3, propValues[i]);
                        insertProperties.addBatch();
                    }
                    insertProperties.executeBatch();
                }
            }
            con.commit();
            con.close();
            inserted = true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return inserted;
    }

    public boolean updateProductWithProperties(int productId, String name, double price, String[] propNames,
                                               String[] propValues) {
        Connection con = null;
        PreparedStatement updateProductStatement = null;
        boolean updated = false;
        try {
            con = dbManager.getConnection();
            updateProductStatement = con.prepareStatement(SQL_UPDATE_PRODUCT);
            updateProductStatement.setString(1, name);
            updateProductStatement.setDouble(2, price);
            updateProductStatement.setInt(3, productId);
            updateProductStatement.executeUpdate();

            PreparedStatement deleteProperties = con.prepareStatement(SQL_DELETE_PROPERTIES_BY_PRODUCT_ID);
            deleteProperties.setInt(1, productId);
            deleteProperties.executeUpdate();

            if (propNames != null) {
                PreparedStatement insertProperties = con.prepareStatement(SQL_INSERT_PROPERTY);
                for (int i = 0; i < propNames.length; i++) {
                    insertProperties.setInt(1, productId);
                    insertProperties.setString(2, propNames[i]);
                    insertProperties.setString(3, propValues[i]);
                    insertProperties.addBatch();
                }
                insertProperties.executeBatch();
            }
            con.commit();
            con.close();
            updated = true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
        }
        return updated;
    }

    public List<Product> selectProductsOrderedAndByProperties(String sortOption, Map<String, Set<String>> properties,
                                                              int pageIndex, int recordsPerPage, Integer priceFrom,
                                                              Integer priceTo) {
        List<Product> products = null;
        switch (sortOption) {
            case Parameters.SORT_BY_NAME_ZA:
                products = selectProductsOrderedAndByProperties(Parameters.PRODUCT_NAME,
                        DBConstants.ORDER_DESCENDING, properties, pageIndex,
                        recordsPerPage, priceFrom, priceTo);
                break;
            case Parameters.SORT_PRICE_HIGH_LOW:
                products = selectProductsOrderedAndByProperties(Parameters.PRICE,
                        DBConstants.ORDER_DESCENDING, properties, pageIndex,
                        recordsPerPage, priceFrom, priceTo);
                break;
            case Parameters.SORT_PRICE_LOW_HIGH:
                products = selectProductsOrderedAndByProperties(Parameters.PRICE,
                        DBConstants.ORDER_ASCENDING, properties, pageIndex,
                        recordsPerPage, priceFrom, priceTo);
                break;
            case Parameters.SORT_NEWEST:
                products = selectProductsOrderedAndByProperties(Parameters.PRODUCT_CREATION_DATE,
                        DBConstants.ORDER_DESCENDING, properties, pageIndex,
                        recordsPerPage, priceFrom, priceTo);
                break;
            default:
                products = selectProductsOrderedAndByProperties(Parameters.PRODUCT_NAME,
                        DBConstants.ORDER_ASCENDING, properties, pageIndex,
                        recordsPerPage, priceFrom, priceTo);
                break;
        }
        return products;
    }

    public class ProductMapper implements EntityMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs) {
            try {
                Product product = new Product();
                product.setId(rs.getInt(DBConstants.ENTITY_ID));
                product.setName(rs.getString(DBConstants.PRODUCT_NAME));
                product.setPrice(rs.getDouble(DBConstants.PRODUCT_PRICE));
                product.setCreationDate(rs.getDate(DBConstants.PRODUCT_CREATION_DATE));
                product.setProperties(new HashMap<>());
                return product;
            } catch (SQLException e) {
                log.error(e.getMessage());
                throw new IllegalStateException(e);
            }
        }
    }
}
