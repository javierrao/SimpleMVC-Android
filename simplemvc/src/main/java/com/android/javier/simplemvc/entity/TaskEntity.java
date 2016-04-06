package com.android.javier.simplemvc.entity;

/**
 * Created by javier on 2016/3/27.
 * <p>
 * 描述Task的实体
 */
public final class TaskEntity {
    private int id;
    private String name;
    private String type;
    private String metaData;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }
}
