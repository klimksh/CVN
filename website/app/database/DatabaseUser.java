package database;

import java.sql.ResultSet;
import java.sql.SQLException;

import models.*;



public class DatabaseUser {
	private static DatabaseInitialization dataInt;
	static{		
		dataInt= new DatabaseInitialization(); 
	}
	public static long userExist(String email)
	{
		String sqlQuery="select id from users where users.email=?";
		try {
			dataInt.pst=dataInt.c.prepareStatement(sqlQuery);
			dataInt.pst.setString(1, email);
		    ResultSet rs=dataInt.pst.executeQuery(sqlQuery);
		    if(rs!=null)		    
		    	 while (rs.next()) 
		    		 return rs.getLong(1);    	
		    
		    	
		    return 0;
		    
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}
	public static void insertUser()
	{
	     User a= new User("kujtim.rahmani@gmail.com", "kujtim", "Kujtim");
	    
		// dataInt.pst = dataInt.con.prepareStatement("INSERT INTO users VALUES(?)");
        // pst.setBinaryStream(1, fin, (int) img.length());
        // pst.executeUpdate();
		
	}
	public static void main(String[] args) {
		DatabaseUser.insertUser();
	}
}