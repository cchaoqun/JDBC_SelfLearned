package JDBC_1.Step3_PreparedStatement;

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

/**针对于Order表的通用的查询操作
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-15:55
 */

public class OrderForQuery {
    /**
     * 针对于表的字段名与类的属性名不相同的情况
     * 1. 必须声明sql时, 使用类的属性名来命名字段的别名
     * 2. 使用ResultSetMetaData时 需要使用getColumnLabel() 来替换 getColumnName() 获取列的别名
     * 说明 sql中没有给字段取别名 getColumnLabel()获取的就是列名
     */
    public static void main(String[] args) {
        testQueryPrac();
    }

    @Test
    public static void testQueryPrac(){
        //查询多条结果
        String sql1 = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id <= ?";
        List<Order> order1 = queryPrac(Order.class, sql1, 0);
        System.out.println(order1);
    }

    @Test
    public void testQueryForOrder(){
        //查询一条结果
        String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
        Order order = queryForOrder(sql, true,1);
        System.out.println(order);

        //查询多条结果
        String sql1 = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id <= ?";
        List<Order> order1 = queryForOrder(sql1, 3);
        System.out.println(order1);

    }
    //获取一条查询数据 返回封装结果集数据的类对象
    public Order queryForOrder(String sql, Boolean queryOne, Object ...args){
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
                //创建一个order类对象表示一条查询的结果
                Order order = new Order();
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
                    //通过反射设置这一个order类对象的columnName属性为columnValue值
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }


    //获取多条查询数据 返回多条order类的list
    public List<Order> queryForOrder(String sql, Object ...args){
        List<Order> list = new ArrayList<>();
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
                //创建一个order类对象表示一条查询的结果
                Order order = new Order();
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
                    //通过反射设置这一个order类对象的columnName属性为columnValue值
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return list;
    }

    //===========================练习 JDBC=====================================
    public static <T> List<T> queryPrac(Class<T> clazz, String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<>();

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译SQL 生成PrepareStatement 对象
            ps = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            //执行 获取结果集
            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            while(rs.next()){
                T t = clazz.newInstance();
                //遍历当前条数据的每一列的值与字段别名
                for(int i=0; i<columnCount; i++){
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射设置该类实例的属性为对应的结果集该列的值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return list;
    }

}



