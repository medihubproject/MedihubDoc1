package com.example.medihubdoc;

public class UserHelperClass {

    String name, email, phoneNo, password,spec,dest,status,veri;
    public static String sKey="Buh@61M";
    public UserHelperClass() {
    }

    public UserHelperClass(String name, String email, String phoneNo, String password, String spec, String dest, String status, String veri) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password= password;
        this.spec = spec;
        this.dest = dest;
        this.status = status;
        this.veri = veri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVeri() {
        return veri;
    }

    public void setVeri(String veri) {
        this.veri = veri;
    }
}
