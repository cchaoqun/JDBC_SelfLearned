package JDBC_1.Step4_Exercise;

import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-19:37
 */

public class ExerTest1 {
    public static void main(String[] args) {
        testInsert();
    }

    @Test
    public static void testInsert(){
        String sql = "insert into customers(name,email,birth)values(?,?,?) ;";
//        String name = "customer1";
//        String email = "cust1@gmail.com";
//        String birth = "2021-01-01";
        Scanner input = new Scanner(System.in);
        System.out.print("请输入姓名:");
        String name = input.next();
        System.out.print("请输入邮箱:");
        String email = input.next();
        System.out.print("请输入生日:");
        String birth = input.next();
        int insertCount = update(sql, name, email, birth);
        if(insertCount>0){
            System.out.println("添加成功!");
        }else{
            System.out.println("添加失败");
        }
    }

    public static int update(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql 获取preparedStatement对象
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            //4.执行
            /**
             * ps。execute()
             * 如果执行的是查询操作， 有返回值 返回true
             * 如果是增删改操作,无返回值 返回false
             */
            //方式一
//            return ps.execute();
            //方式二: 返回影响的行数
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;

    }
}
