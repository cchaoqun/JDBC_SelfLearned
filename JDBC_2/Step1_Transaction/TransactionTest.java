package JDBC_2.Step1_Transaction;

import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 *
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/19-22:30
 */
/**
 * 1.什么叫数据库事务？
 * 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 * 		> 一组逻辑操作单元：一个或多个DML操作。
 *
 * 2.事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
 * 下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 * 3.数据一旦提交，就不可回滚
 *
 * 4.哪些操作会导致数据的自动提交？
 * 		>DDL操作一旦执行，都会自动提交。
 * 			>set autocommit = false 对DDL操作失效
 * 		>DML默认情况下，一旦执行，就会自动提交。
 * 			>我们可以通过set autocommit = false的方式取消DML操作的自动提交。
 * 		>默认在关闭连接时，会自动的提交数据
 */
public class TransactionTest {


    //******************未考虑数据库事务情况下的转账操作**************************
    /**
     * 针对于数据表user_table来说：
     * AA用户给BB用户转账100
     *
     * update user_table set balance = balance - 100 where user = 'AA';
     * update user_table set balance = balance + 100 where user = 'BB';
     */
    @Test
    public void testUpdate(){
        String sql1 = "update user_table set balance = balance - 100 where user = ?;";
        String sql2 = "update user_table set balance = balance + 100 where user = ?;";
        update(sql1, "AA");
        //模拟网络异常
        System.out.println(10/0);
        update(sql2, "BB");
        System.out.println("转账成功");
    }

    //通用的增删改操作 --version 1.0
    public int update(String sql, Object ...args) {
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
            return ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //5.关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }

    //******************未考虑数据库事务情况下的转账操作**************************
    @Test
    public void testUpdateWithTx() throws Exception {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //1.取消数据的自动提交false
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?;";
            String sql2 = "update user_table set balance = balance + 100 where user = ?;";
            update(conn, sql1, "AA");
            //模拟网络异常
            System.out.println(10/0);
            update(conn, sql2, "BB");
            System.out.println("转账成功");

            //2.提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //3.发生异常后数据回滚到连接前
            try{
                conn.rollback();
            }catch (SQLException e1){
                e1.printStackTrace();;
            }
        } finally {
            //关闭连接前恢复true
            conn.setAutoCommit(true);
            JDBCUtils.closeResource(conn, null);
        }


    }



    //通用的增删改操作 --version 2.0
    public int update(Connection conn, String sql, Object ...args) {
        PreparedStatement ps = null;
        try{
            //1.预编译sql语句 返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for(int i=0; i<args.length; i++){
                //占位符从1开始 数组下标从0开始
                ps.setObject(i+1, args[i]);
            }
            //3.执行
            return ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //4.关闭资源
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }


    //*****************************************************

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //设置当前连接隔离界别 read committed
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交
        conn.setAutoCommit(false);
        String sql = "select user,password,balance from user_table where user = ?;";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        //取消自动提交
        conn.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?;";
        update(conn,  sql, 6666, "CC");
        Thread.sleep(15000);
        System.out.println("修改结束");
    }
    /**
     * 针对于不同表的通用的查询操作 返回一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    //通用的查询操作，用于返回数据表中的一条记录（version 2.0：考虑上事务）
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
