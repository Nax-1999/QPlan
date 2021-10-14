package com.example.myqplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myqplan.adpter.MainRvAdapter;
import com.example.myqplan.base.BaseActivity;
import com.example.myqplan.cache.TaskPoolCache;
import com.example.myqplan.enity.TaskPool;


import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private List<TaskPool> taskPools;


    @Override
    protected void getContent() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.main_rv);

    }

    @Override
    protected void initData() {
        taskPools = new LinkedList<>();
        TaskPool taskPool1 = new TaskPool("任务待办池");
        TaskPool taskPool2 = new TaskPool("任务就绪池");
        TaskPool taskPool3 = new TaskPool("任务执行池");
        TaskPool taskPool4 = new TaskPool("任务回顾池");
        TaskPool taskPool5 = new TaskPool("任务完成池");
        TaskPool taskPool6 = new TaskPool("任务阻塞池");
        TaskPool taskPool7 = new TaskPool("碎片任务池");
        taskPools.add(taskPool1);
        taskPools.add(taskPool2);
        taskPools.add(taskPool3);
        taskPools.add(taskPool4);
        taskPools.add(taskPool5);
        taskPools.add(taskPool6);
        taskPools.add(taskPool7);
        TaskPoolCache.getInstance().setList(taskPools);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MainRvAdapter(MainActivity.this, taskPools));
    }

}