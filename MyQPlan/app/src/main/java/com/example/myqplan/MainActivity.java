package com.example.myqplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.myqplan.adpter.MainRvAdapter;
import com.example.myqplan.base.BaseActivity;
import com.example.myqplan.cache.TaskPoolCache;
import com.example.myqplan.constants.SpConstants;
import com.example.myqplan.enity.TaskPool;
import com.example.myqplan.utils.SpUtils;


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
        getWindow().setBackgroundDrawableResource(R.drawable.midnight);

    }

    @Override
    protected void initData() {
        taskPools = new LinkedList<>();
        SpUtils.setSp(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        String poolsName = sp.getString(SpConstants.TASK_POOL_NAMES, "");
        if (TextUtils.isEmpty(poolsName)) {
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
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < taskPools.size() - 1; i++) {
                sb.append(taskPools.get(i).getName()).append("&");
            }
            sb.append(taskPools.get(taskPools.size() - 1).getName());
            editor.putString(SpConstants.TASK_POOL_NAMES, sb.toString());
            editor.apply();
        } else {
            String[] strings = poolsName.split("&");
            for (int i = 0; i < strings.length; i++) {
                TaskPool taskPool = new TaskPool(strings[i]);
                taskPools.add(taskPool);
            }
        }

        TaskPoolCache.getInstance().setList(taskPools);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MainRvAdapter(MainActivity.this, taskPools));
    }

}