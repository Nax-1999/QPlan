package com.example.myqplan.enity;

public class Task {

    private String name;
    private boolean finished;

    String taskType;

    public Task(String name, boolean finished) {
        this.name = name;
        this.finished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", finished=" + finished +
                '}';
    }
}
