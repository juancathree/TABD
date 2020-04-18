import java.sql.*;
import oracle.jdbc.pool.*;


public class DataHandler
{
	public DataHandler() {}
	
	String jdbcUrl = "jdbc:oracle:thin:@//localhost:1521/XE";
	String userId = "german";
	String password = "root";
	
	public static Connection conn;
	
	public void getDBConnection() throws SQLException
	{
		OracleDataSource ds = new OracleDataSource();
		ds.setURL(jdbcUrl);
		conn = ds.getConnection(userId, password);
		System.out.println("Conexión con la base de datos establecida...\n\n");
	}
	
	public void closeConnection() throws SQLException {conn.close();}
	
}
