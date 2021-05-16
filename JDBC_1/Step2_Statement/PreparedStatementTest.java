package JDBC_1.Step2_Statement;

import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**使用PreparedStatement替换Statement解决sql注入问题
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-17:42
 */

public class PreparedStatementTest {

    @Test
    public void testLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名: ");
        String user = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        //"SELECT user,password FROM user_table WHERE user = '1' or ' AND password = '=1 or '1' = '1'
        //实际上是
        /**
         * SELECT user,password FROM user_table
         * WHERE user = '1'
         * OR 'AND password ='=1
         * OR '1' = '1'
         * 最后一个条件恒为真
         */
        String sql = "SELECT user,password FROM user_table WHERE user = ? AND password = ?;";

        User returnUser = getInstance(User.class, sql, user, password);
        if(returnUser!=null){
            System.out.println("登录成功");
        }else{
            System.out.println("用户名不存在或密码错误");
        }
    }



    /**
     * 针对于不同表的通用的查询操作 返回一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     *
     * 除了解决Statement的拼串 sql注入问题之外 PreparedStatement还有哪些好处?
     * 1. PreparedStatement 操作Blob数据 Statement做不到
     * 2. PreparedStatement 可以实现高效的批量操作
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
