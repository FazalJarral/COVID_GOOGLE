package com.example.covid_admin.bean;

import java.io.Serializable;

public class User implements Serializable {
    String name;
    String phoneNum;
    String id;
    String address;
    public User(String name, String phoneNum , String address) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

