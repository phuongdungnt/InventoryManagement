package com.nat.inventorymanagement.module;

public class user {
    private String fullName;
    private String email;
    private String uid;
    private String password;
    private String address;
    private String nameshop;
    private String phone;

    public user(String address, String nameshop, String phone) {
        this.address = address;
        this.nameshop = nameshop;
        this.phone = phone;
    }

    public user(String fullName, String email, String uid, String password) {
        this.fullName = fullName;
        this.email = email;
        this.uid = uid;
        this.password = password;
    }


    public user() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNameshop() {
        return nameshop;
    }

    public void setNameshop(String nameshop) {
        this.nameshop = nameshop;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "user{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", nameshop='" + nameshop + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
