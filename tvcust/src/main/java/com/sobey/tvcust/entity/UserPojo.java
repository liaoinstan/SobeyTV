package com.sobey.tvcust.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class UserPojo implements Serializable{
    private int id;
    private String name;


    //bundle
    private String pathphoto;
    private String pathvideo;

    public UserPojo() {
    }
}
