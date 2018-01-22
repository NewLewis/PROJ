package com.example.pjr.proj.Items;

/**
 * Created by PJR on 2018/1/1.
 */

public class Item {
    private int inOrOut;
    private int color;
    private String kind;
    private double price;

    public Item(int color,String kind,double price,int inOrOut){
        this.color = color;
        this.kind = kind;
        this.price = price;
        this.inOrOut = inOrOut;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
    }

}
