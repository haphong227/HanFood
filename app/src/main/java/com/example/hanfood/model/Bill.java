package com.example.hanfood.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Serializable {
    String address;
    String CurrentTime;
    String CurrentDate;
    String email;
    String idBill;
    String idUser;
    String name;
    String note;
    String phone;
    double price;
    int quantity;
    String stateOrder;
    ArrayList<ItemFood> itemFoodArrayList;

    public Bill() {
    }

    public Bill(String address, String currentTime, String currentDate, String email, String idBill, String idUser, String name, String note, String phone, double price, int quantity, String stateOrder, ArrayList<ItemFood> itemFoodArrayList) {
        this.address = address;
        CurrentTime = currentTime;
        CurrentDate = currentDate;
        this.email = email;
        this.idBill = idBill;
        this.idUser = idUser;
        this.name = name;
        this.note = note;
        this.phone = phone;
        this.price = price;
        this.quantity = quantity;
        this.stateOrder = stateOrder;
        this.itemFoodArrayList = itemFoodArrayList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStateOrder() {
        return stateOrder;
    }

    public void setStateOrder(String stateOrder) {
        this.stateOrder = stateOrder;
    }

    public ArrayList<ItemFood> getItemFoodArrayList() {
        return itemFoodArrayList;
    }

    public void setItemFoodArrayList(ArrayList<ItemFood> itemFoodArrayList) {
        this.itemFoodArrayList = itemFoodArrayList;
    }
}
