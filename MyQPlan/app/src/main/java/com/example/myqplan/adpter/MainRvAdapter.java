package com.example.myqplan.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myqplan.R;
import com.example.myqplan.TaskPoolActivity;
import com.example.myqplan.constants.Constants;
import com.example.myqplan.enity.TaskPool;

import java.util.List;

public class MainRvAdapter extends RecyclerView.Adapter<MainRvAdapter.ViewHolder> {

    private List<TaskPool> list;
    private Context context;

    public MainRvAdapter(Context context, List<TaskPool> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TaskPool taskPool = list.get(position);
        holder.textView.setText(taskPool.getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "你点击了： " + holder.textView.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, TaskPoolActivity.class);
                intent.putExtra(Constants.MAIN_RV_INDEX, position);
//                intent.putExtra(Constants.CLICKED_POOL_NAME, holder.textView.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.task_pool_item);
        }

    }

}
