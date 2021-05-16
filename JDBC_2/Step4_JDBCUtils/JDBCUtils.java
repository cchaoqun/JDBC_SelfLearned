package JDBC_2.Step4_JDBCUtils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-12:42
 */

public class JDBCUtils {

    /**
     *  使用c3p0数据库连接池技术
     * @return
     * @throws SQLException
     */
    //数据库连接池只需提供一个即可
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloC3P0");

    public static Connection getConnection1() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }

    /**
     * 使用dbcp数据库连接池技术获取数据库连接
     * @return
     * @throws Exception
     */

    //创建一个DBCP数据库连接池
    private static DataSource source;
    static{
        try {
            Properties pros = new Properties();
            //方式1:
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);

            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }

    //创建一个Druid数据库连接池
    private static DataSource source3;
    static{
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            Properties pros = new Properties();
            pros.load(is);
            source3 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用Druid数据库连接池技术连接数据库
     * @throws SQLException
     */
    public static Connection getConnection3() throws SQLException {
        Connection conn = source.getConnection();
        return conn;

    }

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

    /**
     * 使用dbutils.jar中提供的DbUtils工具类实现资源的关闭
     * @param conn
     * @param ps
     * @param resultSet
     */
    public static void closeResource3(Connection conn, Statement ps, ResultSet resultSet){
//        try {
//            DbUtils.close(conn);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(resultSet);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }


        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(resultSet);
    }
}
