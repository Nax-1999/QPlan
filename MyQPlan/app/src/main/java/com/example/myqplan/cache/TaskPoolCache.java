package com.example.myqplan.cache;


import com.example.myqplan.enity.TaskPool;

import java.util.List;

public class TaskPoolCache {

    private List<TaskPool> list;

    private static TaskPoolCache instance = null;

    public List<TaskPool> getList() {
        return list;
    }

    public void setList(List<TaskPool> list) {
        this.list = list;
    }

    private TaskPoolCache(){}

    public synchronized static TaskPoolCache getInstance() {
        if (instance == null)
            instance = new TaskPoolCache();
        return instance;
    }

}
