package com.sobey.tvcust.entity;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class TestEntity {
    private int id;
    private String name;


    //bundle
    private String pathphoto;
    private String pathvideo;
    private String pathvoice;

    public TestEntity() {
    }

    public TestEntity(String name) {
        this.name = name;
    }

    public TestEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getPathphoto() {
        return pathphoto;
    }

    public void setPathphoto(String pathphoto) {
        this.pathphoto = pathphoto;
    }

    public String getPathvideo() {
        return pathvideo;
    }

    public void setPathvideo(String pathvideo) {
        this.pathvideo = pathvideo;
    }

    public String getPathvoice() {
        return pathvoice;
    }

    public void setPathvoice(String pathvoice) {
        this.pathvoice = pathvoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
