package JDBC_1.Step3_PreparedStatement;

import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**使用PreparedStatement替换Statement,实现对数据表的增删改操作
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-13:50
 */

public class PreparedStatementUpdateTest {

    @Test
    public void testCommonUpdate(){
        //test delete
//        String sql = "delete from customers where id = ?";
//        update(sql, 3);
        //
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql, "DD", "2");
    }



    //通用的增删改操作
    public void update(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //sql中占位符的个数应该与可变形参的个数一致
            //1.获取连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句 返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for(int i=0; i<args.length; i++){
                //占位符从1开始 数组下标从0开始
                ps.setObject(i+1, args[i]);
            }
            //4.执行
            ps.execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //5.关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
    }


    //修改customer表的一条记录
    @Test
    public void testUpdate() {

        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句 返回PreparedStatement的实例
            String sql = "update customers set name=? where id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1,"莫扎特");
            ps.setObject(2, 18);
            //4.执行
            ps.execute();

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }
    }





    //向customers表中添加一条记录
    @Test
    public void testInster() {
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //1.读取配置文件中的4个基本信息
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties info = new Properties();
            info.load(is);

            String user = info.getProperty("user");
            String password = info.getProperty("password");
            String url = info.getProperty("url");
            String driverClass = info.getProperty("driverClass");

            //2.加载驱动
            Class.forName(driverClass);

            //3.获取连接
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn);

            //4.预编译sql语句 返回PreparedStatement的实例
            String sql = "insert into customers(name, email, birth) values(?,?,?)";//? 占位符
            ps = conn.prepareStatement(sql);

            //5.填充占位符
            ps.setString(1,"哪吒");
            ps.setString(2,"nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");

            //he JDBC specification specifies a standard mapping from
            //Java bject types to SQL types.
            //The given argument will be converted to the corresponding SQL type before being sent to the database.
            ps.setObject(3, new Date(date.getTime()));

            //6.执行操作
            ps.execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //7.资源的关闭
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
