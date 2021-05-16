package JDBC_2.Step4_DataBaseConnectionPool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-11:44
 */

public class C3P0Test {

    //方式一:
    @Test
    public void testGetConnection() throws Exception {
        //获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true" );
        cpds.setUser("root");
        cpds.setPassword("123");

        //通过设置相关的参数,对数据连接池进行管理
        //设置初始时数据库连接池的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁c3p0数据库连接池
        DataSources.destroy(cpds);
    }

    //方式二:使用配置文件
    @Test
    public void testGetConnection2() throws Exception{
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloC3P0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

}
