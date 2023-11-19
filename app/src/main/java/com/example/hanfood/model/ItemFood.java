package com.example.hanfood.model;

import java.io.Serializable;

public class ItemFood implements Serializable {
    private String idFood;
    private String idOrder;
    private String productImg;
    private String productName;
    private double productPrice;
    private double productPriceSale;
    private double totalPrice;
    private int totalQuantity;
    private boolean evaluate;


    public ItemFood() {
    }

    public ItemFood(String idFood, String idOrder, String productImg, String productName, double productPrice, double productPriceSale, double totalPrice, int totalQuantity, boolean evaluate) {
        this.idFood = idFood;
        this.idOrder = idOrder;
        this.productImg = productImg;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPriceSale = productPriceSale;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.evaluate = evaluate;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public double getProductPriceSale() {
        return productPriceSale;
    }

    public void setProductPriceSale(double productPriceSale) {
        this.productPriceSale = productPriceSale;
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

    public boolean isEvaluate() {
        return evaluate;
    }

    public void setEvaluate(boolean evaluate) {
        this.evaluate = evaluate;
    }
}
