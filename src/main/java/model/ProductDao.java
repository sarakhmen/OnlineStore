package model;

import model.entity.ExtendedProduct;
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
    private final DBManager dbManager = DBManager.getInstance();


    private static final String SQL_SELECT_COUNT_OF_RECORDS = "SELECT COUNT(*) FROM product";
    private static final String SQL_SELECT_PRODUCT_ID_WITH_SPECIFIED_PROPS = "SELECT productId FROM property WHERE ";
    private static final String SQL_INSERT_PROPERTY = "INSERT INTO property(productId, propertyNameEn, " +
            "propertyValueEn, propertyNameUk, propertyValueUk) VALUES(?,?,?,?,?)";
    private static final String SQL_INSERT_PRODUCT = "INSERT INTO product(nameEn, nameUk, price, creationDate) " +
            "VALUES (?, ?, ?, NOW())";
    private static final String SQL_SELECT_LAST_INSERT_ID = "SELECT last_insert_id()";
    private static final String SQL_DELETE_PRODUCT_BY_ID = "DELETE FROM product WHERE id=?";
    private static final String SQL_SELECT_EX_PRODUCT_BY_ID = "SELECT id, nameEn, nameUk, price FROM product WHERE id=?";
    private static final String SQL_SELECT_EX_PRODUCT_PROPERTIES = "SELECT propertyNameEn, propertyValueEn, " +
            "propertyNameUk, propertyValueUk FROM property WHERE productId = ?";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE product SET nameEn=?, nameUk=?, price=? WHERE id=?";
    private static final String SQL_DELETE_PROPERTIES_BY_PRODUCT_ID = "DELETE FROM property WHERE productId=?";

    private final String sqlSelectAllProducts;
    private final String sqlSelectProductsIn;
    private final String sqlSelectProductProperties;
    private final String sqlSelectDistinctPropertyNames;
    private final String sqlSelectPropertyValues;
    private final String propertyName;
    private final String propertyValue;

    private final double currencyRatio;

    private int numberOfRecords;

    public ProductDao(String locale) {
        switch (locale) {
            case "uk":
                propertyName = "propertyNameUk";
                propertyValue = "propertyValueUk";
                sqlSelectAllProducts = "SELECT id, nameUk as name, price, creationDate FROM product ORDER BY ";
                sqlSelectProductProperties = "SELECT propertyNameUk as propertyName, propertyValueUk " +
                        "as propertyValue FROM property WHERE productId = ?";
                sqlSelectDistinctPropertyNames = "SELECT DISTINCT propertyNameUk as propertyName FROM property";
                sqlSelectPropertyValues = "SELECT DISTINCT propertyValueUk as propertyValue FROM property " +
                        "WHERE propertyNameUk=?";
                sqlSelectProductsIn = "SELECT id, nameUk as name, price, creationDate FROM product " +
                        "WHERE id IN ^ ORDER BY ";
                currencyRatio = 27;
                break;
            default:
                propertyName = "propertyNameEn";
                propertyValue = "propertyValueEn";
                sqlSelectAllProducts = "SELECT id, nameEn as name, price, creationDate FROM product ORDER BY ";
                sqlSelectProductProperties = "SELECT propertyNameEn as propertyName, propertyValueEn " +
                        "as propertyValue FROM property WHERE productId = ?";
                sqlSelectDistinctPropertyNames = "SELECT DISTINCT propertyNameEn as propertyName FROM property";
                sqlSelectPropertyValues = "SELECT DISTINCT propertyValueEn as propertyValue FROM property " +
                        "WHERE propertyNameEn=?";
                sqlSelectProductsIn = "SELECT id, nameEn as name, price, creationDate FROM product " +
                        "WHERE id IN ^ ORDER BY ";
                currencyRatio = 1;
                break;
        }
    }

    public List<Product> selectProductsOrderedAndByProperties(String column, String order, Map<String,
            Set<String>> properties, int offset, int rowcount){

        Connection con = null;
        List<Product> products = null;
        try {
            con = dbManager.getConnection();
            if(properties.isEmpty()){
                PreparedStatement selectCountOfRecords = con.prepareStatement(SQL_SELECT_COUNT_OF_RECORDS);
                ResultSet rsCount = selectCountOfRecords.executeQuery();
                if (rsCount.next()) {
                    numberOfRecords = rsCount.getInt(1);
                }
                products = selectProductsWithProperties(con, sqlSelectAllProducts + column + " " +
                        order + " LIMIT " + offset + ", " + rowcount);
            }
            else{
                StringBuilder sb1 = new StringBuilder();
                for(Map.Entry<String, Set<String>> property : properties.entrySet()) {
                    System.out.println("HERE");
                    sb1.append(" && ")
                            .append('(');
                    StringBuilder sb2 = new StringBuilder();
                    for (String value : property.getValue()) {
                        sb2.append(" || ")
                                .append('(')
                                .append(propertyName)
                                .append('=')
                                .append("'")
                                .append(property.getKey())
                                .append("'")
                                .append(" && ")
                                .append(propertyValue)
                                .append('=')
                                .append("'")
                                .append(value)
                                .append("'")
                                .append(')');
                    }
                    sb1.append(sb2.substring(4));
                    sb1.append(')');
                }
                String endOfSelectQuery = sb1.substring(4) + " LIMIT " + offset + ", " + rowcount;
                System.out.println(endOfSelectQuery);
                PreparedStatement selectProductIdWithSpecifiedProps =
                        con.prepareStatement(SQL_SELECT_PRODUCT_ID_WITH_SPECIFIED_PROPS + endOfSelectQuery);
                ResultSet rsProductIds = selectProductIdWithSpecifiedProps.executeQuery();
                List<Integer> productIds = new LinkedList<>();
                while(rsProductIds.next()){
                    productIds.add(rsProductIds.getInt(1));
                }
                numberOfRecords = (int)productIds.stream().distinct().count();
                if(!productIds.isEmpty()){
                    String enclosedInParenthesisIds = productIds.stream().map(x->x.toString()).collect(Collectors
                            .joining(", ", "(", ")"));
                    String sqlQuery = sqlSelectProductsIn.replace("^", enclosedInParenthesisIds) +
                            column + " " + order;
                    products = selectProductsWithProperties(con, sqlQuery);
                }
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            products = null;
            //throw custom exception
        }
        return products;
    }

    private List<Product> selectProductsWithProperties(Connection con, String sqlSelectProductQuery) throws SQLException{
        List<Product> products = new LinkedList<>();
        PreparedStatement selectAllProd = con.prepareStatement(sqlSelectProductQuery);
        ResultSet rsProducts = selectAllProd.executeQuery();
        EntityMapper<Product> pm = new ProductMapper();
        PreparedStatement selectPropertiesForProd = con.prepareStatement(sqlSelectProductProperties);
        while (rsProducts.next()) {
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

    public ExtendedProduct selectExProductById(int productId){
        Connection con = null;
        PreparedStatement selectExProd = null;
        PreparedStatement selectPropertiesForExProd = null;
        ExtendedProduct exProduct = null;
        try {
            con = dbManager.getConnection();
            selectExProd = con.prepareStatement(SQL_SELECT_EX_PRODUCT_BY_ID);
            selectExProd.setInt(1, productId);
            ResultSet rsExProduct = selectExProd.executeQuery();
            EntityMapper<ExtendedProduct> expm = new ExtendedProductMapper();
            selectPropertiesForExProd = con.prepareStatement(SQL_SELECT_EX_PRODUCT_PROPERTIES);
            if (rsExProduct.next()) {
                exProduct = expm.mapRow(rsExProduct);
                selectPropertiesForExProd.setInt(1, productId);
                ResultSet rsProperties = selectPropertiesForExProd.executeQuery();
                while (rsProperties.next()) {
                    String propNameEn = rsProperties.getString(1);
                    String propValueEn = rsProperties.getString(2);
                    String propNameUk = rsProperties.getString(3);
                    String propValueUk = rsProperties.getString(4);
                    exProduct.getProperties().put(propNameEn, propValueEn);
                    exProduct.getPropertiesUk().put(propNameUk, propValueUk);
                }
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            dbManager.rollbackAndClose(con);
            exProduct = null;
            //throw custom exception
        }
        return exProduct;
    }

    public Map<String, Set<String>> selectProperties(){
        Connection con = null;
        PreparedStatement selectDistPropNames = null;
        Map<String, Set<String>> properties = new HashMap<>();
        try {
            con = dbManager.getConnection();
            selectDistPropNames = con.prepareStatement(sqlSelectDistinctPropertyNames);
            ResultSet rs = selectDistPropNames.executeQuery();
            Set<String> propNames = new HashSet<>();
            while (rs.next()) {
                propNames.add(rs.getString(1));
            }

            PreparedStatement selectValues = con.prepareStatement(sqlSelectPropertyValues);
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
            //throw custom exception
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
            //throw custom exception
        }
        return deleted;
    }

    public boolean insertProductWithProperties(String nameEn, String nameUk, double price, String[] propNamesEn,
                                               String[] propValuesEn, String[] propNamesUk, String[] propValuesUk) {
        Connection con = null;
        PreparedStatement insertProductStatement = null;
        boolean inserted = false;
        try {
            con = dbManager.getConnection();
            insertProductStatement = con.prepareStatement(SQL_INSERT_PRODUCT);
            insertProductStatement.setString(1, nameEn);
            insertProductStatement.setString(2, nameUk);
            insertProductStatement.setDouble(3, price);
            insertProductStatement.executeUpdate();
            if (propNamesEn != null) {
                PreparedStatement selectLastInsertIdStatement = con.prepareStatement(SQL_SELECT_LAST_INSERT_ID);
                ResultSet lastInsertIdRS = selectLastInsertIdStatement.executeQuery();
                if (lastInsertIdRS.next()) {
                    int lastInsertId = lastInsertIdRS.getInt(1);
                    PreparedStatement insertProperties = con.prepareStatement(SQL_INSERT_PROPERTY);
                    for (int i = 0; i < propNamesEn.length; i++) {
                        insertProperties.setInt(1, lastInsertId);
                        insertProperties.setString(2, propNamesEn[i]);
                        insertProperties.setString(3, propValuesEn[i]);
                        insertProperties.setString(4, propNamesUk[i]);
                        insertProperties.setString(5, propValuesUk[i]);
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
            //throw custom exception
        }
        return inserted;
    }

    public boolean updateProductWithProperties(int productId, String nameEn, String nameUk, double price, String[] propNamesEn,
                                               String[] propValuesEn, String[] propNamesUk, String[] propValuesUk) {
        Connection con = null;
        PreparedStatement updateProductStatement = null;
        boolean updated = false;
        try {
            con = dbManager.getConnection();
            updateProductStatement = con.prepareStatement(SQL_UPDATE_PRODUCT);
            updateProductStatement.setString(1, nameEn);
            updateProductStatement.setString(2, nameUk);
            updateProductStatement.setDouble(3, price);
            updateProductStatement.setInt(4, productId);
            updateProductStatement.executeUpdate();

            PreparedStatement deleteProperties = con.prepareStatement(SQL_DELETE_PROPERTIES_BY_PRODUCT_ID);
            deleteProperties.setInt(1, productId);
            deleteProperties.executeUpdate();

            if (propNamesEn != null) {
                PreparedStatement insertProperties = con.prepareStatement(SQL_INSERT_PROPERTY);
                for (int i = 0; i < propNamesEn.length; i++) {
                    insertProperties.setInt(1, productId);
                    insertProperties.setString(2, propNamesEn[i]);
                    insertProperties.setString(3, propValuesEn[i]);
                    insertProperties.setString(4, propNamesUk[i]);
                    insertProperties.setString(5, propValuesUk[i]);
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
            //throw custom exception
        }
        return updated;
    }

    public class ProductMapper implements EntityMapper<Product> {
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
                log.error(e.getMessage());
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
    }

    public class ExtendedProductMapper implements EntityMapper<ExtendedProduct> {
        @Override
        public ExtendedProduct mapRow(ResultSet rs) {
            try {
                ExtendedProduct exProduct = new ExtendedProduct();
                exProduct.setId(rs.getInt(DBConstants.ENTITY_ID));
                exProduct.setName(rs.getString(DBConstants.PRODUCT_NAME_EN));
                exProduct.setNameUk(rs.getString(DBConstants.PRODUCT_NAME_UK));
                exProduct.setPrice(rs.getDouble(DBConstants.PRODUCT_PRICE));
                exProduct.setProperties(new HashMap<>());
                exProduct.setPropertiesUk(new HashMap<>());
                return exProduct;
            } catch (SQLException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
    }
}
