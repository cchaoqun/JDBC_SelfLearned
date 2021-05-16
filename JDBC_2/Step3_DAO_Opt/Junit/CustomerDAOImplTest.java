package JDBC_2.Step3_DAO_Opt.Junit;

import JDBC_2.Step2_Bean.Customer;
import JDBC_2.Step3_DAO_Opt.CustomerDAOImpl;
import JDBC_2.Step4_JDBCUtils.JDBCUtils;
import JDBC_2.Step4_JDBCUtils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-0:57
 */

public class CustomerDAOImplTest {

    private CustomerDAOImpl dao = new CustomerDAOImpl();
    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer cust = new Customer(1, "cust11", "cust11@qq.com", new Date(1234531L));
            dao.insert(conn, cust);
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
            dao.deleteById(conn, 26);
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
            Customer cust = new Customer(18, "贝多芬", "betofe@qq.com", new Date(1234563123L));
            dao.update(conn, cust);
            System.out.println("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getCustomerById() {
        Connection conn = null;
        try {
            //使用了更新的数据库c3p0数据库连接池提供的链接
//            conn = JDBCUtils.getConnection1();
            //使用了更新的数据库dbcp数据库连接池提供的链接
//            conn = JDBCUtils.getConnection2();
            //使用了更新的数据库druid数据库连接池提供的链接
            conn = JDBCUtils.getConnection3();
            Customer cust = dao.getCustomerById(conn, 19);
            System.out.println(cust);
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
            List<Customer> custList = dao.getAll(conn);
            custList.forEach(System.out::println);
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
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}