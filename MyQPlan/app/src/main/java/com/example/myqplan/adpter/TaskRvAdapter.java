package com.example.myqplan.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myqplan.R;
import com.example.myqplan.enity.Task;

import java.util.List;


public class TaskRvAdapter extends RecyclerView.Adapter<TaskRvAdapter.ViewHolder> {

    private Context context;
    private List<Task> list;

    private View view;


    public TaskRvAdapter(Context context, List<Task> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (view == null)
        view = LayoutInflater.from(context).inflate(R.layout.task_pool_framgnt_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.editText.setText(list.get(position).getName());
        if (list.get(position).isFinished())
            holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.task_item_img_finished_bg));
        else
            holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.task_item_img_bg));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!list.get(position).isFinished()) {
                    holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.task_item_img_finished_bg));
                    list.get(position).setFinished(true);
                }
                else {
                    holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.task_item_img_bg));
                    list.get(position).setFinished(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        EditText editText;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.task_img);
            editText = itemView.findViewById(R.id.task_content);
            linearLayout = itemView.findViewById(R.id.task_item_ll);
        }
    }
}
