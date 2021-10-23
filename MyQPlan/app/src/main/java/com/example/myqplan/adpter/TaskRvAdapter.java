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

    int num = 0;

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
        final boolean[] flag = {false};
        holder.editText.setText(list.get(position).getName());
        if (list.get(position).isFinished())
            holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.task_item_img_finished_bg));
        else
            holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.task_item_img_bg));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
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
//                flag[0] = TextUtils.isEmpty(holder.editText.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Task task = list.get(position);
                task.setName(holder.editText.getText().toString());
                parent.updateSp();
                flag[0] = TextUtils.isEmpty(holder.editText.getText().toString());
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

        holder.editText.setOnKeyListener(new View.OnKeyListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //TODO 当item的文本为空且用户点击了删除键时
                if (TextUtils.isEmpty(holder.editText.getText().toString()) && i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (!flag[0]) {
                        //todo 删除该item： 修改taskList 并且进行SP的写入和rv的重绘
                        Log.d("TAG", "onKey: 进入删除操作" );
                        list.remove(position);
                        parent.updateSp();
//                        notifyDataSetChanged();  改用下面的方法解决超出屏幕时的闪退问题
                        notifyItemRemoved(position);
                        //fixme 好像这里原来传了position-1，导致删除后光标显示在开头... 现在把后面那个也从list.size() - 1改成了position
                        notifyItemRangeChanged(position, position);
                        //todo 删除完时应该关闭软键盘或自动选中上一个item的输入框
                        if (position > 0) {
                            MainHandlerHelper.getInstance().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getFocusViaPosition(position - 1, false);
                                }
                            }, 300);
                        }
                        flag[0] = true;
                    } else {
                        flag[0] = false;
                    }

                }
                //todo 回车键新增任务项
//                Log.d("TAG", "文本为: "  + viewHolders.get(position).editText.getText().toString());
                Log.d("TAG", "游标位置：" + viewHolders.get(position).editText.getSelectionEnd() + "    文本长度 = " + viewHolders.get(position).editText.getText().toString().length());
//                && viewHolders.get(position).editText.getSelectionEnd() == viewHolders.get(position).editText.getText().toString().length()
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP
                        && viewHolders.get(position).editText.getSelectionStart() == viewHolders.get(position).editText.getText().toString().length()) {
                    //FIXME 这里默认往末尾加载了
                    list.add(position + 1, new Task("", false));
                    //todo 不关键盘下面新增就要闪退？？？
//                    KeyBoardUtils.closeKeyBoard(parent.getActivity());
                    parent.updateSp();
//                    notifyDataSetChanged();
                    notifyItemInserted(position + 1);
                    //TODO 把下面的第二个参数从list.size() + 1 改成了position + 1
//                    int num = Math.min(position + 2, list.size());

                    //如果这里不notify，那么新增的项就不会被adapter记入position
                    notifyItemRangeChanged(position + 1, position + 1);

//                    parent.getRecyclerView().scrollToPosition(list.size() - 1);//fixme 解决新增item超出布局时的崩溃
                    parent.getRecyclerView().scrollToPosition(position + 1);
                    //todo 需要把焦点交给下一个item   这里的更新需要设置一个延迟（不然会因为太早调用闪退）
                    MainHandlerHelper.getInstance().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            if (viewHolders.get(position + 1) != null && viewHolders.get(position + 1).editText != null) {
//                                viewHolders.get(position + 1).editText.requestFocus();
//                                KeyBoardUtils.showSoftKeyboard(viewHolders.get(position + 1).editText, parent.getActivity());
//                            } else {
//                                Log.d("TAG", "新的item为空 " );
//                            }
//                            getFocusViaPosition(position + 1, true);
//                             getFocusViaPosition(holder.getAdapterPosition() + 1, true);
//                             getFocusViaPosition(position + 1, true);
                             getFocusViaPosition(position + 1, true);
//                            parent.getRecyclerView().scrollToPosition(position + 1);
                        }
                    }, 300);
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
        num++;
        Log.d("TAG1", "调用了一次onBindViewHolder: " + num);
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

    //todo 需要控制这个方法的执行在rv的adapter的刷新之后
    //fixme 为什么有时候打印的position没错，但实际插入的position不对呢？
    public void getFocusViaPosition(int position, boolean cursorAtStart) {
        Log.d("TAG", "将要获得光标的position为: " + position);
        Log.d("TAG", "当前list的长度: " + list.size());
        Log.d("TAG", "ViewHolder的数目: " + viewHolders.size());
        EditText et = viewHolders.get(position).editText;
//        position = Math.min(position, viewHolders.size() - 1);
        if (!cursorAtStart)
            et.setSelection(et.getText().toString().length());
        else {
            //todo 新增item时先把rv拉到底部
//            parent.getRecyclerView().scrollToPosition(position);

            position = Math.min(position, viewHolders.size() - 1);
            et = viewHolders.get(position).editText;
            et.requestFocus();

//            parent.getRecyclerView().scrollToPosition(list.size() - 1);
        }

//        et.requestFocus();
//        if (!cursorAtStart) {
//            Log.d("TAG", "当前操作是删除item ");
//            MainHandlerHelper.getInstance().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (et.isFocused()) {
//                        Log.d("TAG", "删除项的前一项已获得焦点 ");
//                        int index = et.getText().toString().length();
//                          //这种方式会闪，而且会丢失焦点
//                        et.setSelection(index);
//                    }
//                }
//            },1000);
//        }

    }



}
