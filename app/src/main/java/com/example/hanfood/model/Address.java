package com.example.hanfood.model;

import java.io.Serializable;

public class Address implements Serializable {
    private String idUser;
    private String idAddress;
    private String nameUser;
    private String phoneNumber;
    private String address;

    public Address() {
    }

    public Address(String idUser, String idAddress, String nameUser, String phoneNumber, String address) {
        this.idUser = idUser;
        this.idAddress = idAddress;
        this.nameUser = nameUser;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }
}
