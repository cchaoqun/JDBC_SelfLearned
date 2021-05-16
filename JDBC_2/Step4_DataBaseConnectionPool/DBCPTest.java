package JDBC_2.Step4_DataBaseConnectionPool;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
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
 * @date 2021-04-2021/4/20-13:18
 */

public class DBCPTest {

    /**
     * 测试DBCP的数据库连接池技术
     */
    //方式一
    @Test
    public void testGetConnection() throws SQLException {
        //创建DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true");
        source.setUsername("root");
        source.setPassword("123");

        //设置其他涉及数据库连接池管理的相关属性
        source.setInitialSize(10);

        //获取连接
        Connection conn = source.getConnection();
        System.out.println(conn);
    }


    //方式二: 推荐使用配置文件
    @Test
    public void testGetConnection2() throws Exception {
        Properties pros = new Properties();

        //方式1:
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);

    }
}
