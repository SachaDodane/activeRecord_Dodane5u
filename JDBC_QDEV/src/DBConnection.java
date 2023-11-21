import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection  {

    private static Connection instance;
    private String userName = "root";
    private String password = "";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String tableName = "personne";
    private static String dbName = "testpersonne";


    private DBConnection() throws SQLException{
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        String urlDB = "jdbc:mysql://" + serverName + ":";
        urlDB += portNumber + "/" + dbName;
        instance = DriverManager.getConnection(urlDB, connectionProps);
    }

    public static synchronized Connection getConnection(){
        if (instance==null){
            try{
                new DBConnection();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static synchronized void setNomDB(String newNom){
        if (newNom!=null && newNom!=dbName){
            dbName = newNom;
            instance = null;
        }
    }
}
