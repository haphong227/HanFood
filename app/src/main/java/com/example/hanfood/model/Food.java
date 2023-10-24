package com.example.hanfood.model;

import java.io.Serializable;

public class Food implements Serializable {
    private String desFood;
    private String idFood;
    private String idCate;
    private String imageFood;
    private String nameFood;
    private double priceFood;
    private double percentSale;
    private int quantityFood;

    public Food() {
    }

    public Food(String desFood, String idFood, String idCate, String imageFood, String nameFood, double priceFood, double percentSale, int quantityFood) {
        this.desFood = desFood;
        this.idFood = idFood;
        this.idCate = idCate;
        this.imageFood = imageFood;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.percentSale = percentSale;
        this.quantityFood = quantityFood;
    }

    public String getDesFood() {
        return desFood;
    }

    public void setDesFood(String desFood) {
        this.desFood = desFood;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public String getIdCate() {
        return idCate;
    }

    public void setIdCate(String idCate) {
        this.idCate = idCate;
    }

    public String getImageFood() {
        return imageFood;
    }

    public void setImageFood(String imageFood) {
        this.imageFood = imageFood;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public double getPriceFood() {
        return priceFood;
    }

    public void setPriceFood(double priceFood) {
        this.priceFood = priceFood;
    }

    public void setPercentSale(double percentSale) {
        this.percentSale = percentSale;
    }

    public double getPercentSale() {
        return percentSale;
    }

    public int getQuantityFood() {
        return quantityFood;
    }

    public void setQuantityFood(int quantityFood) {
        this.quantityFood = quantityFood;
    }
}