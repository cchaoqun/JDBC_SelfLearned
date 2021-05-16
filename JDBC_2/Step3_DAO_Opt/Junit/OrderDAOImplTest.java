package JDBC_2.Step3_DAO_Opt.Junit;

import JDBC_2.Step2_Bean.Order;
import JDBC_2.Step3_DAO_Opt.OrderDAOImpl;
import JDBC_2.Utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-10:43
 */

public class OrderDAOImplTest {

    OrderDAOImpl dao = new OrderDAOImpl();

    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Order order = new Order(1,"OO", new Date(1234531L));
            dao.insert(conn, order);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn, 5);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            Order order = new Order(1,"AAA1", new Date(123456123456L));
            dao.update(conn,order);
            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getOrderById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Order order = dao.getOrderById(conn, 1);
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            List<Order> orderList = dao.getAll(conn);
            orderList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Long count = dao.getCount(conn);
            System.out.println("数据数量: "+count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getMaxDate() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxDate = dao.getMaxDate(conn);
            System.out.println("最大日期: "+maxDate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}