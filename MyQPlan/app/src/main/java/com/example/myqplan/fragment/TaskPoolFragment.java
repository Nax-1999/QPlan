package com.example.myqplan.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myqplan.R;
import com.example.myqplan.adpter.TaskRvAdapter;
import com.example.myqplan.constants.SpConstants;
import com.example.myqplan.enity.Task;
import com.example.myqplan.utils.KeyBoardUtils;
import com.example.myqplan.utils.SpUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TaskPoolFragment extends Fragment {

    private EditText editText;
    private Button addBtn;
    private Button submitBtn;
    private RecyclerView recyclerView;

    List<Task> list;

    View view;

    Activity context;

    String taskType;

    public TaskPoolFragment() {

    }

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
        recyclerView = view.findViewById(R.id.task_rv);
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
                    Toast.makeText(context, "????????????????????????", Toast.LENGTH_SHORT).show();
                }


            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasFinishTask = false;
                List<Task> deleteList = new LinkedList<>();
                //todo 1??????????????????????????? (????????????????????????????????????)
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
                    Toast.makeText(context, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                //todo 2????????????????????????SP??????
                updateSp();
                initRv();

                //todo 3????????????????????????SP??????
                String nextType = "";
                switch (taskType) {
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
                if (!nextType.equals(SpConstants.TASK_FINISHED_NEXT)) {
                    //todo ???????????????????????????????????????
                    //todo 1????????????next???????????????
                    String tasks = SpUtils.getFromSp(nextType);
                    Log.d("TAG", "onClick: ?????????taskPool?????????" + tasks);
                    StringBuilder sb = new StringBuilder(tasks);
                    if (!TextUtils.isEmpty(sb.toString())) {
                        //todo ??????????????????????????????????????????
                        sb.append("&");
                    }
                    //todo 2??????????????? ????????????list?????????????????????????????????????????????
                    for (int i = 0; i < deleteList.size() - 1; i++) {
                        Task task = deleteList.get(i);
                        sb.append(task.getName()).append("%").append(task.isFinished());
                        sb.append("&");
                    }
                    Task task = deleteList.get(deleteList.size() - 1);
                    sb.append(task.getName()).append("%").append(task.isFinished());
                    //todo 3?????????sp
                    Log.d("TAG", "onClick: ??????????????????" + sb.toString());
                    SpUtils.addToSp(nextType, sb.toString());


                    //????????????
                    /*
                    //todo ??????????????????????????????????????? (????????????????????????)
                    //todo 1????????????next???????????????
                    String tasks = SpUtils.getFromSp(nextType);
                    Log.d("TAG", "onClick: ?????????taskPool?????????" + tasks);
                    StringBuilder sb = new StringBuilder(tasks);

                    if (!TextUtils.isEmpty(sb.toString())) {
                        //todo ??????????????????????????????????????????
                        sb = new StringBuilder("&").append(sb);
                    }
                    //todo 2??????????????? ????????????list?????????????????????????????????????????????
                    for (int i = deleteList.size() - 1; i > 0; i--) {
                        Task task = deleteList.get(i);
                        sb = new StringBuilder(task.getName()).append("%").append(task.isFinished()).append("&").append(sb);
//                        sb.append(task.getName()).append("%").append(task.isFinished()).append("&");
                    }
                    Task task = deleteList.get(0);
//                    sb.append(task.getName()).append("%").append(task.isFinished());
                    sb = new StringBuilder(task.getName()).append("%").append(task.isFinished()).append(sb);


                    //todo 3?????????sp
                    Log.d("TAG", "onClick: ??????????????????" + sb.toString());
                    SpUtils.addToSp(nextType, sb.toString());
                     */
                }

            }
        });

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
            //todo ???sp?????????????????????
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
            recyclerView.setAdapter(new TaskRvAdapter(context, list, this));
        }
    }

    public void updateSp() {
        if (list == null)
            return;
        //todo ????????????????????????sp
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
        //todo ??????sp???apply?????????????????????????????????
        SpUtils.addToSp(taskType, sb.toString());
    }

}