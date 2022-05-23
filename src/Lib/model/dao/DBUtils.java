package Lib.model.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

// JDBC的工具類
public class DBUtils {
    private static Properties properties = new Properties();
	static final String JDBC_DRIVER = "org.postgresql.Driver";  	
	static final String DB_URL = "jdbc:postgresql://ec2-34-233-214-228.compute-1.amazonaws.com:5432/d69u85fv8eq57s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	static final String USER ="obnetcgcbbxpbl";
	static final String PASS ="f9962202a36812df8f540b57baa67c68f820eb76d173235711bdf6362ccc4525";
    // 當JdbcUtil類的字節碼加載在進JVM時，立即執行
    static{
        try {
            //加載和讀取db.properties文件
//           InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
//           properties.load(inStream);
           Class.forName("org.postgresql.Driver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 創建並返回一個Connection對象
    public static Connection getConnection(){
        try {
//            return DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("username"),
//                    properties.getProperty("password"));
            return DriverManager.getConnection(DB_URL,USER,PASS);
            
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //釋放關閉資源
    public  static void close(Connection conn, Statement st, ResultSet rs){
        try {
            if(rs != null){
                rs.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(st != null){
                    st.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if(conn != null){
                        conn.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}

//import org.junit.Test;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.sql.*;
//import java.util.Properties;
	
//public class DBUtils {
//
////    @Test
//    public void findAll() throws IOException, ClassNotFoundException {
//
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet rs = null;
//		// 通過 Properties 物件或許建立 Connection 物件需要的引數的值
//        Properties properties = new Properties();
//        properties.load(new FileInputStream(new File("jdbc.properties")));
//        // 註冊驅動，似乎現在是可以省略的，獲取 Connection 物件時會自動註冊
//        Class.forName(properties.getProperty("driver"));
//        try {
//            // 獲取資料庫 url，此處為 "jdbc:mysql://localhost/student", 最後一個 student 是在本地資料庫上的一個數據庫名
//            String url = properties.getProperty("url");
//            String user = properties.getProperty("user");
//            String password = properties.getProperty("password");
//            connection = DriverManager.getConnection(url, user, password);
//            statement = connection.createStatement();
//            // 需要執行的 SQL 語句，此處是查詢操作，根據需要編寫不同語句
//            String query = "SELECT * FROM student";
//            // 查詢語句對應的方法是 executeQuery 方法，另如 update 操作對應的則是 executeUpdate 方法
//            // 不同的方法返回的 rs 物件也不同，基本通過打一下 . 這個符號就知道該怎麼操作了
//            rs = statement.executeQuery(query);
//			// 此處只是對查詢到的記錄做了簡單的列印處理
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("stu_name");
//                int age = rs.getInt("age");
//                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ".");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // finally 中關閉資源
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//
//
//
//
//}













