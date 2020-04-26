import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.pool.*;


public class DataHandler
{
	public DataHandler() {}
	
	String jdbcUrl = "jdbc:oracle:thin:@//192.168.1.126:1541/XE";
	String userId = "juanca";
	String password = "p22tagomago";
	
	public static Connection conn;
	
	public void getDBConnection() throws SQLException
	{
		OracleDataSource ds = new OracleDataSource();
		ds.setURL(jdbcUrl);
		conn = ds.getConnection(userId, password);
		//Class.forName("oracle.jdbc.driver.OracleDriver");
		//conn = DriverManager.getConnection(jdbcUrl, userId, password);
		System.out.println("Conexi�n con la base de datos establecida...\n\n");
	}
	
	public void closeConnection() throws SQLException {conn.close();}
	
}
