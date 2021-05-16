package JDBC_2.Step3_DAO_Opt;

import JDBC_2.Step2_Bean.Order;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-10:15
 */

public interface OrderDAO {

    /**
     * 将order对象添加到数据库中
     * @param conn
     * @param order
     */
    void insert(Connection conn, Order order);

    /**
     * 针对指定的id,删除表中的一条记录
     * @param conn
     * @param id
     */
    void deleteById(Connection conn, int id);

    /**
     * 针对内存中的order对象 去修改数据表中指定的记录
     * @param conn
     * @param order
     */
    void update(Connection conn, Order order);

    /**
     * 针对指定的id查询得到对应的Order对象
     * @param conn
     * @param id
     */
    Order getOrderById(Connection conn, int id);

    /**
     * 查询表中所有记录构成的集合
     * @param conn
     * @return
     */
    List<Order> getAll(Connection conn);

    /**
     * 返回数据表中数据的条目数
     * @param conn
     * @return
     */
    Long getCount(Connection conn);

    /**
     * 返回数据表中最近的订单日期
     * @param conn
     * @return
     */
    Date getMaxDate(Connection conn);
}
