package com.example.myqplan.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myqplan.R;
import com.example.myqplan.adpter.TaskRvAdapter;
import com.example.myqplan.enity.Task;
import com.example.myqplan.utils.KeyBoardUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TaskPoolFragment extends Fragment {

    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;

    List<Task> list;

    View view;

    Activity context;

    public TaskPoolFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (view == null)
            view = inflater.inflate(R.layout.fragment_task_pool, container, false);
        editText = view.findViewById(R.id.task_name_et);
        button = view.findViewById(R.id.task_submit_btn);
        recyclerView = view.findViewById(R.id.task_rv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(new Task(editText.getText().toString(), false));
                editText.setText("");
                KeyBoardUtils.closeKeyBoard(context);
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

    private void initData() {
        list = new LinkedList<>();
        list.add(new Task("任务1", false));
        list.add(new Task("任务2", false));
        list.add(new Task("任务3", false));
        list.add(new Task("任务4", true));
        list.add(new Task("任务5", true));
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TaskRvAdapter(context, list));
    }

}