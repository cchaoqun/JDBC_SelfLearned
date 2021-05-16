package JDBC_1.Step3_PreparedStatement;

import JDBC_1.Step3_PreparedStatement.bean.Customer;
import JDBC_1.Step3_PreparedStatement.bean.Order;
import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**使用PreparedStatement实现针对于不同表的通用查询操作
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-17:01
 */

public class PreparedStatementQueryTest {

    @Test
    public void testGetForList(){
        String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id <= ?;";
        List<Order> orderList = getForList(Order.class, sql, 4);
        orderList.forEach(System.out::println);

        String sql1 = "select id,name,email from customers where id < ?;";
        List<Customer> custList = getForList(Customer.class, sql1, 3);
        custList.forEach(System.out::println);

        //全部选择 没有可变形参
        String sql2 = "select order_id orderId, order_name orderName, order_date orderDate from `order`";
        List<Order> orderList1 = getForList(Order.class, sql2);
        orderList1.forEach(System.out :: println);
    }


    public <T> List<T> getForList(Class<T> clazz, String sql, Object ...args ){
        List<T> list = new ArrayList<T>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译SQL文件 获取prepareStatement 对象
            ps = conn.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            //执行查询操作获得结果集对象
            rs = ps.executeQuery();

            //获取结果集的元数据 通过元数据获得结果集的列数和列名
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while(rs.next()){
                //创建一个泛型类对象表示一条查询的结果
                T t = clazz.newInstance();
                //获取查询的字段值及字段名并通过反射设置类对象的属性(字段名)为字段值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值: getObject() 获取这一行 第i+1个列的值
                    Object columnValue = rs.getObject(i + 1);
                    //获取列的列名: getColumnLabel() 第i+1个列名 不推荐使用
                    //     这里获取的列名为SQL数据库中表的列名而不是 别名
                    //     这样拿着获取到的列名与对应类中的属性名去查找的时候就会报错
                    //如获取的是order_id(列名), 但是Order类中对应属性为orderId(属性名)
                    //需要sql语句中为查询的列名取别名为类中的对应的属性名
                    //同时这里需要获取别名而不是列名
                    //获取列的别名: getColumnLabel()
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //通过反射设置这一个clazz 类对象的columnName属性为columnValue值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }








    @Test
    public void testGetInstance(){
        String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?;";
        Order order = getInstance(Order.class, sql, 2);
        System.out.println(order);

        String sql1 = "select id,name,email from customers where id = ?;";
        Customer cust = getInstance(Customer.class, sql, 12);
        System.out.println(cust);
    }

    /**
     * 针对于不同表的通用的查询操作 返回一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz, String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译SQL文件 获取prepareStatement 对象
            ps = conn.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            //执行查询操作获得结果集对象
            rs = ps.executeQuery();

            //获取结果集的元数据 通过元数据获得结果集的列数和列名
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                //创建一个泛型类对象表示一条查询的结果
                T t = clazz.newInstance();
                //获取查询的字段值及字段名并通过反射设置类对象的属性(字段名)为字段值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值: getObject() 获取这一行 第i+1个列的值
                    Object columnValue = rs.getObject(i + 1);
                    //获取列的列名: getColumnLabel() 第i+1个列名 不推荐使用
                    //     这里获取的列名为SQL数据库中表的列名而不是 别名
                    //     这样拿着获取到的列名与对应类中的属性名去查找的时候就会报错
                    //如获取的是order_id(列名), 但是Order类中对应属性为orderId(属性名)
                    //需要sql语句中为查询的列名取别名为类中的对应的属性名
                    //同时这里需要获取别名而不是列名
                    //获取列的别名: getColumnLabel()
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //通过反射设置这一个clazz 类对象的columnName属性为columnValue值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

}
