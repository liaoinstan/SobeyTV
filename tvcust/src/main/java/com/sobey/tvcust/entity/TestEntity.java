package com.sobey.tvcust.entity;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class TestEntity {
    private int id;
    private String name;


    //bundle
    private String[] pathphotos;
    private String[] pathvideos;
    private String[] pathvoices;

    public TestEntity() {
    }

    public TestEntity(String name) {
        this.name = name;
    }

    public TestEntity(int id, String name) {
        this.id = id;
        this.name = name;
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

    public String[] getPathphotos() {
        return pathphotos;
    }

    public void setPathphotos(String[] pathphotos) {
        this.pathphotos = pathphotos;
    }

    public String[] getPathvideos() {
        return pathvideos;
    }

    public void setPathvideos(String[] pathvideos) {
        this.pathvideos = pathvideos;
    }

    public String[] getPathvoices() {
        return pathvoices;
    }

    public void setPathvoices(String[] pathvoices) {
        this.pathvoices = pathvoices;
    }
}
