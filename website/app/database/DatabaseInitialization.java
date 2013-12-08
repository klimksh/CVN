package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 *  db.url=jdbc:postgresql:postgres
 db.driver=org.postgresql.Driver
 db.user=postgres
 db.pass=root
 */
public class DatabaseInitialization {
	public  Connection c = null;
	public   Statement statment = null;
	PreparedStatement pst = null;
	//public  String sqlStatment=null;
    public  DatabaseInitialization ()
    {   	 
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://localhost:5432/postgres",
	            "postgres", "root");
	         
	         statment=c.createStatement();
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
		// TODO Auto-generated constructor stub
	}
    public  DatabaseInitialization (String sqlStatment)
    {   	 
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://localhost:5432/postgres",
	            "postgres", "root");
	         
	        } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
		// TODO Auto-generated constructor stub
	}
  /*  public ResultSet executeSql(String sql)
    {
    	
    	try {
			ResultSet a=statment.executeQuery(sql);
			statment.close();
			return a;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    	
    }*/
   

}
