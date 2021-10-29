package com.example.myqplan.enity;

import java.util.Date;

public class TaskPool {

    private String name;
    private long updateTime = 0;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskPool(String name) {
        this.name = name;
    }

    public TaskPool(String name, long updateTime) {
        this.name = name;
        this.updateTime = updateTime;
    }
}
