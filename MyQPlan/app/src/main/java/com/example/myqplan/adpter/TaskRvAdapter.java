package com.example.myqplan.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myqplan.R;
import com.example.myqplan.enity.Task;
import com.example.myqplan.fragment.TaskPoolFragment;
import com.example.myqplan.utils.KeyBoardUtils;
import com.example.myqplan.utils.MainHandlerHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class TaskRvAdapter extends RecyclerView.Adapter<TaskRvAdapter.ViewHolder> {

    private Context context;
    private List<Task> list;

    private TaskPoolFragment parent;

    private View view;

    List<ViewHolder> viewHolders = new LinkedList<>();

    public TaskRvAdapter(Context context, List<Task> list, Fragment fragment) {
        this.context = context;
        this.list = list;
        parent = (TaskPoolFragment) fragment;
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
                parent.updateSp();
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Task task = list.get(position);
                task.setName(holder.editText.getText().toString());
                parent.updateSp();
            }
        };
        //解决rv中的item文本被复用机制覆盖的问题
        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    holder.editText.addTextChangedListener(textWatcher);
                } else {
                    holder.editText.removeTextChangedListener(textWatcher);
                }
            }
        });
        final boolean[] flag = {false};
        final int index = position;
        holder.editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("TAG", "onKey: 键盘事件为：" + keyEvent.getAction() + "i = " + i);
                //TODO 当item的文本为空且用户点击了删除键时
                if (TextUtils.isEmpty(holder.editText.getText().toString()) && i == KeyEvent.KEYCODE_DEL ) {
                    if (flag[0]) {
                        //todo 删除该item： 修改taskList 并且进行SP的写入和rv的重绘
                        Log.d("TAG", "onKey: 进入删除操作" );
                        list.remove(position);
                        parent.updateSp();
                        parent.initRv();
                        //todo 删除完时应该关闭软键盘或自动选中上一个item的输入框
                        KeyBoardUtils.closeKeyBoard(Objects.requireNonNull(parent.getActivity()));

//                        if (index > 0 ) {
//                            //前一个item获取焦点
//                            Log.d("TAG", "run: delete");
//                            viewHolders.get(index - 1).editText.requestFocus();
////                            KeyBoardUtils.showSoftKeyboard(viewHolders.get(index - 1).editText, parent.getActivity());
//                        }

                    } else
                        flag[0] = true;
                }
                //todo 回车键新增任务项
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER) {
                    list.add(new Task("", false));
                    //todo 不关键盘下面新增就要闪退？？？
                    KeyBoardUtils.closeKeyBoard(parent.getActivity());
                    parent.updateSp();
                    //todo 需要把焦点交给下一个item   这里的更新需要设置一个延迟（不然会因为太早调用闪退）
                    MainHandlerHelper.getInstance().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (viewHolders.get(position + 1) != null && viewHolders.get(position + 1).editText != null) {
                                viewHolders.get(position + 1).editText.requestFocus();
                                KeyBoardUtils.showSoftKeyboard(viewHolders.get(position + 1).editText, parent.getActivity());
                            } else {
                                Log.d("TAG", "新的item为空 " );
                            }
                        }
                    }, 200);
                }
                return false;
            }
        });
        holder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        viewHolders.add(holder);
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
