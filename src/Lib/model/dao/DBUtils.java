package Lib.model.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

// JDBC���u����
public class DBUtils {
    private static Properties properties = new Properties();
	static final String JDBC_DRIVER = "org.postgresql.Driver";  	
	static final String DB_URL = "jdbc:postgresql://ec2-34-233-214-228.compute-1.amazonaws.com:5432/d69u85fv8eq57s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	static final String USER ="obnetcgcbbxpbl";
	static final String PASS ="f9962202a36812df8f540b57baa67c68f820eb76d173235711bdf6362ccc4525";
    // ��JdbcUtil�����r�`�X�[���b�iJVM�ɡA�ߧY����
    static{
        try {
            //�[���MŪ��db.properties���
//           InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
//           properties.load(inStream);
           Class.forName("org.postgresql.Driver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // �Ыبê�^�@��Connection��H
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

    //���������귽
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
//		// �q�L Properties ����γ\�إ� Connection ����ݭn���޼ƪ���
//        Properties properties = new Properties();
//        properties.load(new FileInputStream(new File("jdbc.properties")));
//        // ���U�X�ʡA���G�{�b�O�i�H�ٲ����A��� Connection ����ɷ|�۰ʵ��U
//        Class.forName(properties.getProperty("driver"));
//        try {
//            // �����Ʈw url�A���B�� "jdbc:mysql://localhost/student", �̫�@�� student �O�b���a��Ʈw�W���@�Ӽƾڮw�W
//            String url = properties.getProperty("url");
//            String user = properties.getProperty("user");
//            String password = properties.getProperty("password");
//            connection = DriverManager.getConnection(url, user, password);
//            statement = connection.createStatement();
//            // �ݭn���檺 SQL �y�y�A���B�O�d�߾ާ@�A�ھڻݭn�s�g���P�y�y
//            String query = "SELECT * FROM student";
//            // �d�߻y�y��������k�O executeQuery ��k�A�t�p update �ާ@�������h�O executeUpdate ��k
//            // ���P����k��^�� rs ����]���P�A�򥻳q�L���@�U . �o�ӲŸ��N���D�ӫ��ާ@�F
//            rs = statement.executeQuery(query);
//			// ���B�u�O��d�ߨ쪺�O�����F²�檺�C�L�B�z
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("stu_name");
//                int age = rs.getInt("age");
//                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ".");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // finally �������귽
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













