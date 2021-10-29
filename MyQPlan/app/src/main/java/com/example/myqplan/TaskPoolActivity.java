package com.example.myqplan;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myqplan.base.BaseActivity;
import com.example.myqplan.cache.TaskPoolCache;
import com.example.myqplan.constants.Constants;
import com.example.myqplan.constants.SpConstants;
import com.example.myqplan.enity.TaskPool;
import com.example.myqplan.fragment.TaskPoolFragment;
import com.example.myqplan.utils.KeyBoardUtils;
import com.example.myqplan.utils.SpUtils;

import java.util.LinkedList;
import java.util.List;

public class TaskPoolActivity extends BaseActivity {

    private ViewPager viewPager;

    private List<TaskPoolFragment> list;

    private int index = 0;

    String taskPoolName;

    private TextView taskPoolNameTv;

    String currentTaskPool;

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.task_pool_vp);
        taskPoolNameTv = findViewById(R.id.task_pool_name);
        getWindow().setBackgroundDrawableResource(R.mipmap.midnight);
    }

    @Override
    protected void initData() {
        index = getIntent().getIntExtra(Constants.MAIN_RV_INDEX, 0);
//        taskPoolName = TaskPoolCache.getInstance().getList().get(index).getName();

        list = new LinkedList<>();
        String s = SpUtils.getFromSp(SpConstants.TASK_POOL_NAMES);
        if (!TextUtils.isEmpty(s)) {
            String[] pools = s.split("&");
            for (String str : pools) {
                String[] strings = str.split("%");
                TaskPoolFragment fragment = new TaskPoolFragment(strings[0]);
                TaskPool taskPool = new TaskPool(strings[0], Long.parseLong(strings[1]));
                fragment.setTaskPool(taskPool);
                list.add(fragment);
            }
        }
        //TODO 添加任务fragments
//        list.add(new TaskPoolFragment(SpConstants.TASK_DAILY));
//        list.add(new TaskPoolFragment(SpConstants.TASK_UNDO));
//        list.add(new TaskPoolFragment(SpConstants.TASK_READY));
//        list.add(new TaskPoolFragment(SpConstants.TASK_EXECUTE));
//        list.add(new TaskPoolFragment(SpConstants.TASK_REVIEW));
//        list.add(new TaskPoolFragment(SpConstants.TASK_FINISHED));
//        list.add(new TaskPoolFragment(SpConstants.TASK_BLOCKED));
//        list.add(new TaskPoolFragment(SpConstants.TASK_PATCH));

        taskPoolName = list.get(index).getTaskType();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean flag = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
//                taskPoolName = TaskPoolCache.getInstance().getList().get(index).getName();
                taskPoolName = list.get(position).getTaskType();
                if (!TextUtils.isEmpty(taskPoolName))
                    taskPoolNameTv.setText(taskPoolName);

                list.get(position).initData();
                list.get(position).initRv();
                KeyBoardUtils.closeKeyBoard(TaskPoolActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(index);
        if (!TextUtils.isEmpty(taskPoolName))
            taskPoolNameTv.setText(taskPoolName);

    }

    public void updateSp(String taskType, long time) {
        if (list == null)
            return;
        //todo 把新添加的项写入sp
        StringBuilder sb = new StringBuilder();
        if (list.size() == 0) {
            SpUtils.addToSp(SpConstants.TASK_POOL_NAMES, "");
            return;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            TaskPool taskPool = list.get(i).getTaskPool();
            if (taskType.equals(taskPool.getName()))
                sb.append(taskPool.getName()).append("%").append(time).append("&");
            else {
                sb.append(taskPool.getName()).append("%").append(taskPool.getUpdateTime()).append("&");
            }
        }
        TaskPool taskPool = list.get(list.size() - 1).getTaskPool();
        if (taskType.equals(taskPool.getName()))
            sb.append(taskPool.getName()).append("%").append(time);
        else
            sb.append(taskPool.getName()).append("%").append(taskPool.getUpdateTime());

        //todo 其实sp的apply可以在离开页面时提交？
        SpUtils.addToSp(SpConstants.TASK_POOL_NAMES, sb.toString());
    }

    @Override
    protected void getContent() {
        setContentView(R.layout.activity_task_pool);
    }
}