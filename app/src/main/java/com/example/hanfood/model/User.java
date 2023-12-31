package com.example.hanfood.model;

public class User {
    private String birthday;
    private String email;
    private String password;
    private String name;
    private String phone;
    String idUser;
    private String image;

    public User() {
    }

    public User(String birthday, String email, String password, String name, String phone, String idUser, String image) {
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.idUser = idUser;
        this.image = image;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
