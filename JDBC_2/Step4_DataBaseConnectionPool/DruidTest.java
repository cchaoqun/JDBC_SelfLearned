package JDBC_2.Step4_DataBaseConnectionPool;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import javafx.beans.property.Property;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/20-14:42
 */

public class DruidTest {


    @Test
    public void testGetConnection() throws Exception {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        Properties pros = new Properties();
        pros.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);

    }

}
