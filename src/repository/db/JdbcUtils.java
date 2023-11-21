package repository.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private Properties jdbcProps;

    public JdbcUtils(Properties properties){
        this.jdbcProps=properties;
    }

    private static Connection instance=null;

    private Connection getNewConnection(){
        String driver=jdbcProps.getProperty("jdbc.driver");
        String url=jdbcProps.getProperty("jdbc.url");
        String user= jdbcProps.getProperty("jdbc.user");
        String password=jdbcProps.getProperty("jdbc.password");
        Connection con=null;
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return instance;
    }
}
