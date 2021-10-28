package com.example.myqplan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myqplan.R;
import com.example.myqplan.adpter.TaskRvAdapter;
import com.example.myqplan.constants.SpConstants;
import com.example.myqplan.enity.Task;
import com.example.myqplan.utils.KeyBoardUtils;
import com.example.myqplan.utils.MyItemTouchHelper;
import com.example.myqplan.utils.SpUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TaskPoolFragment extends Fragment {

    private EditText editText;
    private Button addBtn;
    private Button submitBtn;
//    private Button resetBtn;
    private RecyclerView recyclerView;

    List<Task> list;

    View view;

    Activity context;

    String taskType;

    TaskRvAdapter adapter;

    public TaskRvAdapter getAdapter() {
        return adapter;
    }

    MyItemTouchHelper helper;

    public void setAdapter(TaskRvAdapter adapter) {
        this.adapter = adapter;
    }

//    public TaskPoolFragment() {
//
//    }

    public TaskPoolFragment(String taskType) {
        this.taskType = taskType;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (view == null)
            view = inflater.inflate(R.layout.fragment_task_pool, container, false);
        editText = view.findViewById(R.id.task_name_et);
        addBtn = view.findViewById(R.id.task_add_btn);
        submitBtn = view.findViewById(R.id.task_submit_btn);
//        resetBtn = view.findViewById(R.id.task_reset_btn);
        recyclerView = view.findViewById(R.id.task_rv);
        setHelper();
        if (taskType == SpConstants.TASK_DAILY) {
            submitBtn.setText("重置");
        } else if (taskType == SpConstants.TASK_FINISHED) {
            submitBtn.setText("清除");
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTaskName = editText.getText().toString();
                if (!TextUtils.isEmpty(newTaskName)) {
                    list.add(new Task(newTaskName, false));
                    editText.setText("");
                    KeyBoardUtils.closeKeyBoard(context);
                    updateSp();
                } else {
                    Toast.makeText(context, "输入框不能为空！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (taskType.equals(SpConstants.TASK_DAILY)) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setFinished(false);
                    }
                    updateSp();
                    //
//                    adapter.notifyDataSetChanged();
                    initRv();
                    return;
//                    StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i < list.size() - 1; i++) {
//                        Task task = list.get(i);
//                        sb.append(task.getName()).append("%").append(task.isFinished());
//                        sb.append("&");
//                    }
//                    Task task = list.get(list.size() - 1);
//                    sb.append(task.getName()).append("%").append(task.isFinished());
//                    SpUtils.addToSp(SpConstants.TASK_DAILY, sb.toString());
//                    return;
                }
                boolean hasFinishTask = false;
                List<Task> deleteList = new LinkedList<>();
                //todo 1、删除当前列表内容 (先找出全部完成了的列表项)
                Iterator<Task> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.isFinished()) {
                        deleteList.add(task);
                        iterator.remove();
                        hasFinishTask = true;
                    }
                    task.setFinished(false);
                }
                if (!hasFinishTask) {
                    Toast.makeText(context, "当前任务池没有已完成的任务项！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //todo 2、更新当前任务池SP内容
                updateSp();
                adapter.notifyDataSetChanged();
//                initRv();

                //todo 3、写入下一任务池SP内容
                String nextType = "";
                switch (taskType) {
                    case SpConstants.TASK_DAILY:
                        nextType = SpConstants.TASK_DAILY_NEXT;
                        break;
                    case SpConstants.TASK_UNDO:
                        nextType = SpConstants.TASK_UNDO_NEXT;
                        break;
                    case SpConstants.TASK_READY:
                        nextType = SpConstants.TASK_READY_NEXT;
                        break;
                    case SpConstants.TASK_EXECUTE:
                        nextType = SpConstants.TASK_EXECUTE_NEXT;
                        break;
                    case SpConstants.TASK_REVIEW:
                        nextType = SpConstants.TASK_REVIEW_NEXT;
                        break;
                    case SpConstants.TASK_FINISHED:
                        nextType = SpConstants.TASK_FINISHED_NEXT;
                        break;
                    case SpConstants.TASK_BLOCKED:
                        nextType = SpConstants.TASK_BLOCKED_NEXT;
                        break;
                    case SpConstants.TASK_PATCH:
                        nextType = SpConstants.TASK_PATCH_NEXT;
                        break;
                }
                if (!nextType.equals(SpConstants.TASK_FINISHED_NEXT) && !nextType.equals(SpConstants.TASK_DAILY_NEXT)) {
                    //todo 当提交的任务池不是完成池时
                    //todo 1、获取到next的任务列表
                    String tasks = SpUtils.getFromSp(nextType);
                    Log.d("TAG", "onClick: 下一个taskPool内容：" + tasks);
                    StringBuilder sb = new StringBuilder(tasks);
                    if (!TextUtils.isEmpty(sb.toString())) {
                        //todo 别忘了处理前面的分隔符！！！
                        sb.append("&");
                    }
                    //todo 2、更新列表 （这里的list应当是前一个任务池删除的任务）
                    for (int i = 0; i < deleteList.size() - 1; i++) {
                        Task task = deleteList.get(i);
                        sb.append(task.getName()).append("%").append(task.isFinished());
                        sb.append("&");
                    }
                    Task task = deleteList.get(deleteList.size() - 1);
                    sb.append(task.getName()).append("%").append(task.isFinished());
                    //todo 3、更新sp
                    Log.d("TAG", "onClick: 更新后内容：" + sb.toString());
                    SpUtils.addToSp(nextType, sb.toString());


                    //逆序展示
                    /*
                    //todo 当提交的任务池不是完成池时 (现在改成逆序展示)
                    //todo 1、获取到next的任务列表
                    String tasks = SpUtils.getFromSp(nextType);
                    Log.d("TAG", "onClick: 下一个taskPool内容：" + tasks);
                    StringBuilder sb = new StringBuilder(tasks);

                    if (!TextUtils.isEmpty(sb.toString())) {
                        //todo 别忘了处理前面的分隔符！！！
                        sb = new StringBuilder("&").append(sb);
                    }
                    //todo 2、更新列表 （这里的list应当是前一个任务池删除的任务）
                    for (int i = deleteList.size() - 1; i > 0; i--) {
                        Task task = deleteList.get(i);
                        sb = new StringBuilder(task.getName()).append("%").append(task.isFinished()).append("&").append(sb);
//                        sb.append(task.getName()).append("%").append(task.isFinished()).append("&");
                    }
                    Task task = deleteList.get(0);
//                    sb.append(task.getName()).append("%").append(task.isFinished());
                    sb = new StringBuilder(task.getName()).append("%").append(task.isFinished()).append(sb);


                    //todo 3、更新sp
                    Log.d("TAG", "onClick: 更新后内容：" + sb.toString());
                    SpUtils.addToSp(nextType, sb.toString());
                     */
                }

            }
        });
//        resetBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        initData();
        initRv();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        KeyBoardUtils.closeKeyBoard(Objects.requireNonNull(context));
//        initData();
    }

    public void initData() {
        list = new LinkedList<>();

        String tasks = SpUtils.getFromSp(taskType);
        if (!TextUtils.isEmpty(tasks)) {
            //todo 从sp中读取任务列表
            String[] strings = tasks.split("&");
            for (int i = 0; i < strings.length; i++) {
                String[] s = strings[i].split("%");
                list.add(new Task(s[0], Boolean.parseBoolean(s[1])));
            }
        }

    }

    public void initRv() {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new TaskRvAdapter(context, list, this);
            recyclerView.setAdapter(adapter);
//            recyclerView.setAdapter(new TaskRvAdapter(context, list, this));
        }
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
                        Collections.swap(list, i, i + 1);
                    }
                } else {
                    for (int i = pre; i > cur; i--) {
                        Collections.swap(list, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(pre, cur);
                updateSp();
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
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

    //todo 每次更新数据时，需要把变化内容写入SP
    public void updateSp() {
        if (list == null)
            return;
        //todo 把新添加的项写入sp
        StringBuilder sb = new StringBuilder();
        if (list.size() == 0) {
            SpUtils.addToSp(taskType, "");
            return;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            Task task = list.get(i);
            sb.append(task.getName()).append("%").append(task.isFinished()).append("&");
        }
        Task task = list.get(list.size() - 1);
        sb.append(task.getName()).append("%").append(task.isFinished());
        //todo 其实sp的apply可以在离开页面时提交？
        SpUtils.addToSp(taskType, sb.toString());
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

}