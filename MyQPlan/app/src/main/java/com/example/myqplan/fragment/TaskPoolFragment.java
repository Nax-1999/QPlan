package com.example.myqplan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myqplan.R;

public class TaskPoolFragment extends Fragment {

    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;

    View view;

    public TaskPoolFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_task_pool, container, false);
        editText = view.findViewById(R.id.task_name_et);
        button = view.findViewById(R.id.task_submit_btn);
        recyclerView = view.findViewById(R.id.task_rv);

        return view;
    }
}