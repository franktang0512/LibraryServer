package Lib.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class hi {

//	   // JDBC driver name and database URL
//	   static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
//	   static final String DB_URL = "jdbc:mysql://localhost/test";
//
//	   //  Database credentials
//	   static final String USER = "root";
//	   static final String PASS = "";
//	   
	   

		static final String JDBC_DRIVER = "org.postgresql.Driver";  

		
		static final String DB_URL = "jdbc:postgresql://ec2-34-233-214-228.compute-1.amazonaws.com:5432/d69u85fv8eq57s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		
		static final String USER ="obnetcgcbbxpbl";

		static final String PASS ="f9962202a36812df8f540b57baa67c68f820eb76d173235711bdf6362ccc4525";
	   
		static  Connection conn = null;
		static   Statement stmt = null;
		

	   public static void main(String[] args) {

	   try{
	      //STEP 2: Register JDBC driver
//	      Class.forName("com.mysql.cj.jdbc.Driver");
	      
	      Class.forName("org.postgresql.Driver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      conn = DriverManager.getConnection(DB_URL,USER,PASS);
	      
	      //STEP 4: Execute a query
	      System.out.println("Creating statement...");
	      stmt = conn.createStatement();
	      String sql;
//	      sql = "SELECT id, first, last, age FROM Employees where";
	      sql = "SELECT * FROM \"Posts\"";
//	      sql+=" id =?";
	      PreparedStatement ps = conn.prepareStatement(sql);
	      String id000 ="403530033";
//	      ps.setString(1, id000);
	      //ps.setInt(2, 23);
	      ResultSet rs = ps.executeQuery();;
	      
//	      ResultSet rs = stmt.executeQuery(sql);
//
	      //STEP 5: Extract data from result set
	      while(rs.next()){
	         //Retrieve by column name
	         int id  = rs.getInt("id");
	         String title = rs.getString("title");
	         String content = rs.getString("content");
	         //String createat = rs.getString("createAt");

	         //Display values
	         System.out.print("ID: " + id);
	         System.out.print(", title: " + title);
	         System.out.println(", content: " + content);
	         //System.out.println(", createAt: " + createat);
	      }
	      
	      //STEP 6: Clean-up environment
	      rs.close();
	      stmt.close();
	      conn.close();
	   }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }finally{
	      //finally block used to close resources
	      try{
	         if(stmt!=null)
	            stmt.close();
	      }catch(SQLException se2){
	      }// nothing we can do
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	   }//end try
	   System.out.println("done!");
	}//end main
}
