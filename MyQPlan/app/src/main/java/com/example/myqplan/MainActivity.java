package com.example.myqplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.myqplan.adpter.MainRvAdapter;
import com.example.myqplan.base.BaseActivity;
import com.example.myqplan.cache.TaskPoolCache;
import com.example.myqplan.constants.SpConstants;
import com.example.myqplan.enity.Task;
import com.example.myqplan.enity.TaskPool;
import com.example.myqplan.utils.MyItemTouchHelper;
import com.example.myqplan.utils.SpUtils;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private List<TaskPool> taskPools;

    MainRvAdapter adapter;

    ItemTouchHelper helper;


    @Override
    protected void getContent() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.main_rv);
        getWindow().setBackgroundDrawableResource(R.mipmap.midnight);
    }

    @Override
    protected void initData() {
        taskPools = new LinkedList<>();
        SpUtils.setSp(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        String poolsName = sp.getString(SpConstants.TASK_POOL_NAMES, "");
        if (TextUtils.isEmpty(poolsName)) {
            TaskPool taskPool0 = new TaskPool("每日任务池");
            TaskPool taskPool1 = new TaskPool("任务待办池");
            TaskPool taskPool2 = new TaskPool("任务就绪池");
            TaskPool taskPool3 = new TaskPool("任务执行池");
            TaskPool taskPool4 = new TaskPool("任务回顾池");
            TaskPool taskPool5 = new TaskPool("任务完成池");
            TaskPool taskPool6 = new TaskPool("任务阻塞池");
            TaskPool taskPool7 = new TaskPool("碎片任务池");
            taskPools.add(taskPool0);
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
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new MainRvAdapter(MainActivity.this, taskPools);
//        recyclerView.setAdapter(adapter);
        initRv();

        setHelper();
    }

    private void setHelper() {
        helper = new MyItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int pre = viewHolder.getAdapterPosition();
                int cur = target.getAdapterPosition();
                if (pre < cur) {
                    for (int i = pre; i < cur ; i++) {
                        Collections.swap(taskPools, i, i + 1);
                    }
                } else {
                    for (int i = pre; i > cur; i--) {
                        Collections.swap(taskPools, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(pre, cur);
                //todo 将这个变化，写入SP 还需要通知Rv的adapter list发生了变化
                updateSp();
//                adapter.setList(taskPools);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
                initRv();
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.GRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainRvAdapter(MainActivity.this, taskPools);
        recyclerView.setAdapter(adapter);
    }

    private void updateSp() {
        if (taskPools == null)
            return;
        //todo 把新添加的项写入sp
        StringBuilder sb = new StringBuilder();
        if (taskPools.size() == 0) {
            SpUtils.addToSp(SpConstants.TASK_POOL_NAMES, "");
            return;
        }
        for (int i = 0; i < taskPools.size() - 1; i++) {
            TaskPool taskPool = taskPools.get(i);
            sb.append(taskPool.getName()).append("&");
        }
        TaskPool taskPool = taskPools.get(taskPools.size() - 1);
        sb.append(taskPool.getName());
        //todo 其实sp的apply可以在离开页面时提交？
        SpUtils.addToSp(SpConstants.TASK_POOL_NAMES, sb.toString());
    }

}