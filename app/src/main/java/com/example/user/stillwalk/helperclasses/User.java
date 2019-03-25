package com.example.user.stillwalk.helperclasses;

import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private int age;
    private String personalInfo;
    private String username;
    private ArrayList<String> contacts = new ArrayList<>();
    private String message;


    public User(String username, int age, String firstName, String lastName, String personalInfo, ArrayList<String> contacts, String message) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.personalInfo = personalInfo;
        this.contacts = contacts;
        this.message = message;
        this.username = username;
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public int getAge() {
        return age;
    }


    public String getPersonalInfo() {
        return personalInfo;
    }


    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", personalInfo='" + personalInfo + '\'' +
                ", username='" + username + '\'' +
                ", contacts=" + contacts +
                ", message='" + message + '\'' +
                '}';
    }
}
