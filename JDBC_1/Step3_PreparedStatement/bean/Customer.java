package JDBC_1.Step3_PreparedStatement.bean;

import java.sql.Date;

/**ORM编程思想 (object relational mapping)
 * 一个数据表对应一个java类
 * 表中的一条记录对应java类的对象
 * 表中的一个字段对应java类的属性
 * @author Chaoqun Cheng
 * @date 2021-04-2021/4/18-15:11
 */


public class Customer {

    private int id;
    private String email;
    private String name;
    private Date date;

    public Customer() {
        super();
    }

    public Customer(int id, String email, String name, Date date) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }


}
