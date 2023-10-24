package com.example.hanfood.model;

import java.io.Serializable;

public class Cart implements Serializable {
    String idFood;
    String productImg;
    String productName;
    double productPrice;
    double productPriceSalse;
    double totalPrice;
    int totalQuantity;


    public Cart() {
    }

    public Cart(String idFood, String productImg, String productName, double productPrice, double productPriceSalse, double totalPrice, int totalQuantity) {
        this.idFood = idFood;
        this.productImg = productImg;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPriceSalse = productPriceSalse;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public double getProductPriceSalse() {
        return productPriceSalse;
    }

    public void setProductPriceSalse(double productPriceSalse) {
        this.productPriceSalse = productPriceSalse;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }


}
