package JDBC_1.Step5_Blob;

import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 使用PrepareStatement实现批量数据的操作
 *
 * update delete 本身就具有批量操作的效果
 * 此时的批量操作, 主要值的是批量插入  使用insert如何实现更高效的批量插入
 *
 * 题目: 向goods表中插入20000条数据
 * CREATE TABLE goods(
 * 	id INT PRIMARY KEY AUTO_INCREMENT,
 * 	NAME VARCHAR(25)
 * );
 * 方式一:使用statement
 * Connection conn = JDBCUtils.getConnection();
 * Statement st = conn.createStatement();
 * for(int i=1; i<=2000; i++){
 *     String sql = "insert into goods(name)values('name_"+i+"')";
 *     st.execute(sql);
 * }
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/19-21:20
 */

public class InsertTest {

    //批量插入的方式二:使用PrepareStatement
    @Test
    public void testInsert1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for(int i=1; i<=20000; i++){
                ps.setObject(1, "name_"+i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为: "+(end-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 批量插入的方式三:
     * 1. addBatch(), executeBatch(), clearBatch()
     *  mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
     *		 ?rewriteBatchedStatements=true 写在配置文件的url后面
     */
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for(int i=1; i<=1000000; i++){
                ps.setObject(1, "name_"+i);

                //攒sql
                ps.addBatch();
                if(i%500==0){
                    //2.执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为: "+(end-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //批量插入的方式四: 设置自动提交为false, 一起写完后最后一起提交
    //80s
    @Test
    public void testInsert3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();

            //设置自动提交数据为false
            conn.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for(int i=1; i<=1000000; i++){
                ps.setObject(1, "name_"+i);

                //攒sql
                ps.addBatch();
                if(i%500==0){
                    //2.执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为: "+(end-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }



}
