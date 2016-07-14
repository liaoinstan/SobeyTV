package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CountEntity {

    private int id;

    private String name;

    private int value;

    private int color;

    public CountEntity() {
    }

    public CountEntity(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public CountEntity(String name, int value, int color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CountEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", color=" + color +
                '}';
    }
}
