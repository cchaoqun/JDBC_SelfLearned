package JDBC_1.Step3_PreparedStatement;

import JDBC_1.Step3_PreparedStatement.bean.Customer;
import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**针对于customer表的查询操作
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-14:56
 */

public class CustomerForQuery {

    @Test
    public void testQueryForCustomers(){
        String sql = "select id,name,email from customers where id = ?;";
        Customer cust = quertForCustomers(sql, 13);
        System.out.println(cust);

        String sql1 = "select name,email from customers where name = ?;";
        Customer cust1 = quertForCustomers(sql1, "周杰伦");
        System.out.println(cust1);

    }


    /**
     * 针对customers表的通用查询操作
     */
    public Customer quertForCustomers(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql语句
            ps = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            //执行
            rs = ps.executeQuery();
            //获取结果集的元数据:ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过结果集的元数据获取列数
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                Customer cust = new Customer();
                //处理结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //给cust对象的指定的columnName属性赋值为columnValue 通过反射
                    //根据列名获取Customer类声明的属性
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    //防止属性为私有 设置成可以访问
                    field.setAccessible(true);
                    //设置cust类的属性为columnValue
                    field.set(cust, columnValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }


    /**
     * 针对customers表查询一条语句
     */
    @Test
    public void testQuery1()  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql语句
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = conn.prepareStatement(sql);

            //填充占位符
            ps.setObject(1,1);

            //执行并返回结果集
            resultSet = ps.executeQuery();
            //处理结果集
            //next():判断结果集的下一条是否有数据, 如果有数据返回true 并指针下移, 如果返回false,指针不会下移
            if(resultSet.next()){
                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

//                //方式一:
//                System.out.println("id="+id+", name="+name+", email="+email+", birth="+birth);
//                //方式二:
//                Object[] data = new Object[]{id, name, email, birth};
                //方式三:将数据封装为一个对象(推荐)
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(conn, ps, resultSet);
        }


    }

}
