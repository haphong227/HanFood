package com.example.hanfood.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String idCate;
    private String imageCate;
    private String nameCate;

    public Category() {
    }

    public Category(String idCate, String imageCate, String nameCate) {
        this.idCate = idCate;
        this.imageCate = imageCate;
        this.nameCate = nameCate;
    }

    public String getIdCate() {
        return idCate;
    }

    public void setIdCate(String idCate) {
        this.idCate = idCate;
    }

    public String getImageCate() {
        return imageCate;
    }

    public void setImageCate(String imageCate) {
        this.imageCate = imageCate;
    }

    public String getNameCate() {
        return nameCate;
    }

    public void setNameCate(String nameCate) {
        this.nameCate = nameCate;
    }
}
