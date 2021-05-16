package JDBC_2.Step1_Transaction;

import JDBC_2.Utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/19-22:09
 */

public class ConnectionTest {

    @Test
    public void testGetConnection() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
