package model;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String JDBC_DB_URL = "jdbc:mysql://localhost:3306/store";
    static final String JDBC_USER = "root";
    static final String JDBC_PASS = "Sarahman10";

    private static volatile DBManager instance;

    private PoolingDataSource poolingDataSource;

    public static DBManager getInstance() {
        DBManager localInstance = instance;
        if (localInstance == null) {
            synchronized (DBManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    localInstance = new DBManager();
                    localInstance.setUpPool();
                    instance = localInstance;
                }
            }
        }
        return localInstance;
    }

    private DBManager() {
    }

    private void setUpPool() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Oops", e);
        }

        GenericObjectPool gPool = new GenericObjectPool();
        gPool.setMaxActive(100);
        gPool.setMaxIdle(30);
        gPool.setMaxWait(10000);

        ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
        @SuppressWarnings("unused") PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null,
                null, false, false);
        poolingDataSource = new PoolingDataSource(gPool);

    }

    public Connection getConnection() throws SQLException {
        return poolingDataSource.getConnection();
    }
//
//    public void commitAndClose(Connection con) {
//        try {
//            con.commit();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        try {
//            con.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }

    public void rollbackAndClose(Connection con) {
        if (con != null) {
            try {
                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

//    public void closeStatement(Statement st) {
//        if (st != null) {
//            try {
//                st.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    public void closeConnection(Connection con) {
//        if (con != null) {
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
}
