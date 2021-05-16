package JDBC_2.Step3_DAO_Opt;

import JDBC_2.Step2_Bean.Order;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-10:15
 */

public class OrderDAOImpl extends BaseDAO<Order> implements OrderDAO{
    @Override
    public void insert(Connection conn, Order order) {
        String sql = "insert into `order`(order_name,order_date)values(?,?)";
        update(conn, sql, order.getOrderName(), order.getOrderDate());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from `order` where order_id = ?;";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, Order order) {
        String sql = "update `order` set order_name=?, order_date=? where order_id = ?;";
        update(conn, sql, order.getOrderName(), order.getOrderDate(),order.getOrderId());
    }

    /**
     * 注意这两个方法查询时,需要给表的字段起别名
     * 否则在BaseDAO getInstance()方法通过反射获取表的label并为Order类的对应字段赋值时会在Order类中找不到对应表列名的字段
     * 进而无法为Order类属性赋值 报错
     *
     * @param conn
     * @param id
     * @return
     */

    @Override
    public Order getOrderById(Connection conn, int id) {
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?;";
        Order order = getInstance(conn, sql, id);
        return order;
    }

    @Override
    public List<Order> getAll(Connection conn) {
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order`;";
        List<Order> orderList = getForList(conn, sql);
        return orderList;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from `order`;";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxDate(Connection conn) {
        String sql = "select MAX(order_date) from `order`;";
        return getValue(conn, sql);
    }
}
