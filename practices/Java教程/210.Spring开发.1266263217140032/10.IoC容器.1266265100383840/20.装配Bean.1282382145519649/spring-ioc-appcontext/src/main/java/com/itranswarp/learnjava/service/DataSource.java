package com.itranswarp.learnjava.service;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataSource {

    public Connection getConnection(){
        Connection con=null;
        String driver="com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/learnSpring?serverTimezone=GMT";
        String user="root";
        String password="123456";

        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url,user,password);
            if(!con.isClosed()){
                System.err.println("数据库连接成功！");
            }
        }catch (Exception e){
            System.err.println("数据库连接失败！"+e.toString());
        }

        return con;

    }

}
