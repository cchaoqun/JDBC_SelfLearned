package JDBC_1.Step3_PreparedStatement.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**操作数据库的工具类
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-14:17
 */

public class JDBCUtils {

    /**
     * 获取数据库的链接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        //1.读取配置文件中的4个基本信息
        InputStream is =ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties info = new Properties();
        info.load(is);

        String user = info.getProperty("user");
        String password = info.getProperty("password");
        String url = info.getProperty("url");
        String driverClass = info.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);

        return conn;
    }

    /**
     * 关闭资源的操作
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn, Statement ps){
        try{
            if(conn!=null){
                conn.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        try{
            if(ps!=null){
                ps.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源多了一个resultSet
     * @param conn
     * @param ps
     * @param resultSet
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet resultSet){
        try{
            if(conn!=null){
                conn.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        try{
            if(ps!=null){
                ps.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        try{
            if(resultSet!=null){
                resultSet.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
