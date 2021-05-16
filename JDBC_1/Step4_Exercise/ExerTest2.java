package JDBC_1.Step4_Exercise;

import JDBC_1.Step3_PreparedStatement.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-19:59
 */

public class ExerTest2 {
    public static void main(String[] args) {
//        testInsert();
//        queryWithIDCardOrExamCard();
//        testDeleteByExamCard();
        testDeleteByExamCard1();

    }




    //问题1 向examstudent表中添加一条记录
    @Test
    public static void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("四级/六级: ");
        int type = scanner.nextInt();
        System.out.print("身份证号: ");
        String IDCard = scanner.next();
        System.out.print("准考证号: ");
        String examCard = scanner.next();
        System.out.print("学生姓名: ");
        String studentName = scanner.next();
        System.out.print("所在城市: ");
        String location = scanner.next();
        System.out.print("考试成绩: ");
        int grade = scanner.nextInt();

        String sql = "insert into examstudent(type,IDCard,examCard,studentName,location,grade) values(?,?,?,?,?,?);";
        int insertCount = update(sql, type, IDCard, examCard, studentName, location,grade);
        if(insertCount>0){
            System.out.println("添加成功");
        }else{
            System.out.println("添加失败");
        }
    }


    //问题2: 根据身份证号或者准考证号查询
    @Test
    public static void queryWithIDCardOrExamCard(){
        System.out.println("请选择您要输入的类型: ");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        if("a".equalsIgnoreCase(selection)){
            System.out.println("请输入准考证号");
            String examCard = scanner.next();
            String sql = "select FlowID flowId, Type type, IDCard, ExamCard examCard,StudentName name, Location location, Grade grade from examstudent where examCard = ?;";
            Student student = getInstance(Student.class, sql, examCard);
            if(student!=null){
                System.out.println(student);
            }else{
                System.out.println("输入的准考证号有误");
            }
        }else if("b".equalsIgnoreCase(selection)){
            System.out.println("请输入身份证号");
            String IDCard = scanner.next();
            String sql = "select FlowID flowId, Type type, IDCard, ExamCard examCard,StudentName name, Location location, Grade grade from examstudent where IDCard = ?;";
            Student student = getInstance(Student.class, sql, IDCard);
            if(student!=null){
                System.out.println(student);
            }else{
                System.out.println("输入的身份证号有误");
            }
        }else{
            System.out.println("您的输入有误.请重新进入程序");
        }
    }

    //问题3: 删除指定学生信息
    @Test
    public static void testDeleteByExamCard(){
        System.out.println("请输入学生的考号: ");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        //查询指定准考证号的学生
        String sql = "select FlowID flowId, Type type, IDCard, ExamCard examCard,StudentName name, Location location, Grade grade from examstudent where examCard = ?;";
        Student student = getInstance(Student.class, sql, examCard);
        if(student==null){
            System.out.println("查无此人,请重新输入");
        }else{
            String sql1 = "delete from examstudent where examCard = ?;";
            int deleteCount = update(sql1, examCard);
            if(deleteCount>0){
                System.out.println("删除成功");
            }
        }
    }

    @Test
    public static void testDeleteByExamCard1(){
        System.out.println("请输入学生的考号: ");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        String sql = "delete from examstudent where examCard = ?;";
        int deleteCount = update(sql, examCard);
        if(deleteCount>0){
            System.out.println("删除成功");
        }else{
            System.out.println("查无此人,请重新输入");
        }
    }



    public static <T> T getInstance(Class<T> clazz, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rmsd = rs.getMetaData();
            int columnCount = rmsd.getColumnCount();
            if(rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rmsd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    //通用的增删改操作
    public static int update(String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }

}
