package com.sobey.common.view.bundle;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class BundleEntity {
    public enum Type {PHOTE, VIDEO, VOICE}

    private String path;
    private Type type;

    public BundleEntity() {
    }

    public BundleEntity(Type type) {
        this.type = type;
    }

    public BundleEntity(Type type, String path) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public Type getType() {
        return type;
    }
}
