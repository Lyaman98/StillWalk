package com.example.user.stillwalk.helperclasses;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserData {

    private String url = "jdbc:mysql://35.241.147.40:3306/users?";
    private String username = "lyaman";
    private String password = "allingoodmood";

    public UserData() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public boolean checkUser(String username,String password){


        String sql = "SELECT username FROM userdata WHERE username=? AND password=?";

        try(Connection conn = DriverManager.getConnection(url,this.username,this.password);
            PreparedStatement p = conn.prepareStatement(sql)){

            p.setString(1,username);
            p.setString(2,password);

            ResultSet resultSet = p.executeQuery();

            if (resultSet.next()){
                return true;

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public User getUserData(String username) {

        String sql = "SELECT firstName,lastName,personal_info,age" +
                " FROM userdata WHERE username=?";

        try (Connection conn = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1,username);

            ResultSet resultSet = p.executeQuery();


            if(resultSet.next()){

                User user = new User();
                user.setFirstName(resultSet.getString(1));
                user.setLastName(resultSet.getString(2));
                user.setPersonalInfo(resultSet.getString(3));
                user.setAge(resultSet.getInt(4));


                return user;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean addUserData(User user) {


        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String personal_info = user.getPersonalInfo();
        int age = user.getAge();
        String username = user.getUsername();
        Log.i("mytag",username);

        String sqlScript = "UPDATE userdata\n" +
                    "SET firstName = ?, lastName = ?, age=?, personal_info=?\n" +
                    "WHERE username=?";


        try (Connection conn = DriverManager.getConnection(url, this.username, password);
             PreparedStatement p = conn.prepareStatement(sqlScript)){


                p.setString(1, firstName);
                p.setString(2, lastName);
                p.setInt(3,age);
                p.setString(4, personal_info);
                p.setString(5,username);


                int affectedRows = p.executeUpdate();


                if (affectedRows > 0) {
                    return true;
                }


        }catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }
    public User getContacts( String username){

        String sql = "SELECT contact1,contact2,message" +
                " from  userdata WHERE username=?";

        try(Connection conn = DriverManager.getConnection(url,this.username,this.password);
            PreparedStatement p = conn.prepareStatement(sql)){

            p.setString(1,username);

            ResultSet resultSet = p.executeQuery();


            if (resultSet.next()){
                User user = new User();
                ArrayList<String> contacts= new ArrayList<>();


                contacts.add(resultSet.getString(1));
                contacts.add(resultSet.getString(2));

                user.setContacts(contacts);
                user.setMessage(resultSet.getString(3));


                return user;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


            return null;
    }
    public boolean addContacts(User user) {


        String contact1 = user.getContacts().get(0);
        String contact2 = user.getContacts().get(1);
        String message = user.getMessage();
        String username = user.getUsername();

        String sql =  "UPDATE userdata " +
                    "SET contact1 = ?, contact2 = ?,  message=? " +
                    "WHERE username=?";


        try (Connection conn = DriverManager.getConnection(url, this.username, this.password);
            PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1,contact1);
            p.setString(2,contact2);
            p.setString(3,message);
            p.setString(4,username);


            int affectedRows = p.executeUpdate();

            if (affectedRows > 0){
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean registerUser(String username,String password){

        String sql = "SELECT username FROM userdata WHERE username=?";

        try(Connection conn = DriverManager.getConnection(url,this.username,this.password);
            PreparedStatement p = conn.prepareStatement(sql)){


            p.setString(1,username);

            ResultSet resultSet = p.executeQuery();

            if (!resultSet.next()){

                String addUser = "INSERT INTO userdata(username,password) VALUES(?,?)";

                try (PreparedStatement prepare = conn.prepareStatement(addUser)){

                    prepare.setString(1,username);
                    prepare.setString(2,password);
                    prepare.execute();
                    return true;
                }

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
