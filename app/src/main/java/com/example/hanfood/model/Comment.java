package com.example.hanfood.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String idComment;
    private String idFood;
    private String idUser;
    private String content;
    private String currentTime;
    private String currentDate;
    private float rate;

    public Comment() {
    }

    public Comment(String idComment, String idFood, String idUser, String content, String currentTime, String currentDate, float rate) {
        this.idComment = idComment;
        this.idFood = idFood;
        this.idUser = idUser;
        this.content = content;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.rate = rate;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
