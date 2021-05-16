package JDBC_2.Step5_dbutils;

import JDBC_2.Step2_Bean.Customer;
import JDBC_2.Step4_JDBCUtils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-15:12
 */

public class QueryRunnerTest {

    //测试插入
    @Test
    public void testInsert()  {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth)values(?,?,?);";
            int insertCount = runner.update(conn, sql, "cxk", "cxk@qq.com", "2000-01-01");
            System.out.println("添加了"+insertCount+"条记录");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    //测试查询一条语句
    /**
     * BeanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录。
     *
     * @throws Exception
     */
    @Test
    public void testQuerry1() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?;";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 10);
            System.out.println(customer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    //测试查询多条语句
    /**
     * BeanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录。
     *
     * @throws Exception
     */
    @Test
    public void testQuerry2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id < ?;";
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> custList = runner.query(conn, sql, handler, 10);
            custList.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * MapHander:是ResultSetHandler接口的实现类，对应表中的一条记录。
     * 将字段及相应字段的值作为map中的key和value
     */
    @Test
    public void testQuerry3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?;";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 10);
            System.out.println(map);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * MapListHander:是ResultSetHandler接口的实现类，对应表中的多条记录。
     * 将字段及相应字段的值作为map中的key和value。将这些map添加到List中
     */
    @Test
    public void testQuerry4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id < ?;";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 10);
            mapList.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * ScalarHandler:用于查询特殊值
     */
    @Test
    public void testQuerry5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select count(*) from customers;";
            ScalarHandler handler = new ScalarHandler();
            Long value = (Long)runner.query(conn, sql, handler);
            System.out.println(value);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /**
     * ScalarHandler:用于查询特殊值
     */
    @Test
    public void testQuerry6() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select max(birth) from customers;";
            ScalarHandler handler = new ScalarHandler();
            Date date = (Date)runner.query(conn, sql, handler);
            System.out.println(date);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }


    @Test
    public void testQuerry7() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?;";

            //重写ResultSetHandler接口的匿名内部类实现自己想要实现的实现类
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet rs) throws SQLException {

                    if(rs.next()){
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        Date birth = rs.getDate("birth");
                        Customer cust = new Customer(id,name,email,birth);
                        return cust;
                    }
                    return null;
                }
            };
            Customer cust = runner.query(conn, sql, handler,10);
            System.out.println(cust);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

}
