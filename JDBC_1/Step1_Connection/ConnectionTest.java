package JDBC_1.Step1_Connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/17-21:40
 */

public class ConnectionTest {

    public static void main(String[] args) throws Exception {
        testConnection1();
        testConnection2();
        testConnection3();
        testConnection4();
        getConnection5();
    }

    @Test
    public static void testConnection1() throws SQLException {
        //获取driver的实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        //驱动连接所需要的 地址 url  和账户密码 user password

        //jdbc:mysql: 协议
        //localhost ip地址
        //3306 端口号
        //test test数据库
        String url = "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";

        //将用户名和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123");

        //驱动连接对象
        Connection conn= driver.connect(url, info);

        System.out.println(conn);
    }

    //方式二: 对方式一迭代: 在下面的程序中不出现第三方的API 具有更好的可移植性
    @Test
    public static void testConnection2() throws Exception{
        //1.获取Driver实现类对象: 使用反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";

        //3.提供连接需要的用户名
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123");

        //4.获取连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //方式三: 使用DriverManager替换Driver
    @Test
    public static void testConnection3() throws Exception{
        //1.获取Driver的实现类对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //Driver driver = (Driver) clazz.newInstance();
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //2.提供另外三个基本信息
        String url = "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
        String user = "root";
        String password = "123";

        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式四: 只是加载驱动 不用显示的注册驱动
    @Test
    public static void testConnection4() throws Exception{
        //1.提供另外三个基本信息
        String url = "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
        String user = "root";
        String password = "123";


        //2.加载Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        //相较于方式三省略如下操作

//        //Driver driver = (Driver) clazz.newInstance();
//        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);
        //为什么省略上述操作?
        /**
         *static {
         *         System.err.println("Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. "
         *                 + "The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.");
         *     }
         */

        //3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式五:将连接数据库需要的基本信息声明在配置文件中, 通过读取配置文件的方式获取连接

    /**
     * 此种方式的好处?
     * 1.实现了数据和代码的分离, 实现了解耦
     * 2.如果需要修改配置文件信息 可以避免程序重新打包
     */
    @Test
    public static void getConnection5() throws Exception {
        //1.读取配置文件中的4个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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



    }
}
