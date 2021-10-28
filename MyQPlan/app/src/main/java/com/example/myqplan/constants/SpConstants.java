package com.example.myqplan.constants;

public class SpConstants {

    public final static String TASK_POOL_NAMES = "TASK_POOL_NAMES";

    //每日代办池
    public final static String TASK_DAILY = "TASK_DAILY";
    public final static String TASK_DAILY_NEXT = "TASK_DAILY_NEXT";
    //待办池任务
    public final static String TASK_UNDO = "TASK_UNDO";
    public final static String TASK_UNDO_NEXT = "TASK_READY";
    //就绪池任务
    public final static String TASK_READY = "TASK_READY";
    public final static String TASK_READY_NEXT = "TASK_EXECUTE";
    //执行池任务
    public final static String TASK_EXECUTE = "TASK_EXECUTE";
    public final static String TASK_EXECUTE_NEXT = "TASK_REVIEW";
    //回顾池任务
    public final static String TASK_REVIEW = "TASK_REVIEW";
    public final static String TASK_REVIEW_NEXT = "TASK_FINISHED";
    //完成池任务
    public final static String TASK_FINISHED = "TASK_FINISHED";
    public final static String TASK_FINISHED_NEXT = "DELETE";
    //阻塞池任务
    public final static String TASK_BLOCKED = "TASK_BLOCKED";
    public final static String TASK_BLOCKED_NEXT = "TASK_REVIEW";
    //碎片池任务
    public final static String TASK_PATCH = "TASK_PATCH";
    public final static String TASK_PATCH_NEXT = "TASK_REVIEW";



//    public final static String TASK_POOL_NAMES = "TASK_POOL_NAMES";

//    public final static String TASK_POOL_NAMES = "TASK_POOL_NAMES";
}
