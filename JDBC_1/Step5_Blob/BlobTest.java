package JDBC_1.Step5_Blob;

import JDBC_1.Step3_PreparedStatement.bean.Customer;
import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/19-20:36
 */

public class BlobTest {


    //向数据表customer中插入Blob类型的字段

    @Test
    public  void testInsert() throws Exception{

        Connection conn = JDBCUtils.getConnection();

        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,"cust2");
        ps.setObject(2,"cust2@qq.com");
        ps.setObject(3,"2021-01-01");
        FileInputStream is = new FileInputStream(new File("I-20chaoqun.pdf"));
        ps.setBlob(4,is);
        ps.execute();
        JDBCUtils.closeResource(conn, ps);
    }

    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?;";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,23);
            rs = ps.executeQuery();

            is = null;
            fos = null;
            if(rs.next()){
                //方式一:
    //            int id = rs.getInt(1);
    //            String name = rs.getString(2);
    //            String email = rs.getString(3);
    //            Date birth = rs.getDate(4);
                //方式二:
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer cust = new Customer(id, name, email, birth);
                System.out.println(cust);

                //将Blob类型的字段下载下来 以文件的方式保存到本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("leetcode400.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while((len=is.read(buffer))!= -1){
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);

            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
