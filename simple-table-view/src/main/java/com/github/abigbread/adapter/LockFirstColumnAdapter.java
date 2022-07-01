package com.github.abigbread.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.abigbread.DisplayUtil;
import com.github.abigbread.R;
import com.github.abigbread.bean.ParamsBean;
import com.github.abigbread.callback.OnItemClickListener;
import com.github.abigbread.callback.OnItemLongClickListener;
import com.github.abigbread.callback.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * @author abigbread
 * 时间：2022/6/29 14:56
 * 包名：com.abigbread.simpletableview
 * 简述：<锁定第一列的视图适配器>
 */

public class LockFirstColumnAdapter extends RecyclerView.Adapter<LockFirstColumnAdapter.ViewHolder> {


    private Context context;
    /**
     * 第一列的数据（不包含第一行）
     */
    private ArrayList<String> firstColumnData;

    /**
     * 表格所需的参数
     */
    private ParamsBean paramsBean;


    /**
     * Item点击事件
     */
    private OnItemClickListener onItemClickListener;

    /**
     * Item长按事件
     */
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * Item项被选中监听(处理被选中的效果)
     */
    private OnItemSelectedListener onItemSelectedListener;


    public LockFirstColumnAdapter(Context context, ArrayList<String> firstColumnData) {
        this.context = context;
        this.firstColumnData = firstColumnData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lock_first_column_item,null));
    }


    @Override
    public int getItemCount() {
        return firstColumnData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.item.setText(firstColumnData.get(position));
        holder.item.setTextSize(paramsBean.getTextSize());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.item.getLayoutParams();
        layoutParams.width = DisplayUtil.dip2px(context, paramsBean.getColumnMaxWidths().get(0));
        if (paramsBean.isLockFirstRow()) {
            layoutParams.height = DisplayUtil.dip2px(context, paramsBean.getRowMaxHeights().get(position + 1));
        } else {
            layoutParams.height = DisplayUtil.dip2px(context, paramsBean.getRowMaxHeights().get(position));
        }
        layoutParams.setMargins(paramsBean.getCellPadding(), paramsBean.getCellPadding(), paramsBean.getCellPadding(), paramsBean.getCellPadding());
        holder.item.setLayoutParams(layoutParams);
        //设置颜色
        if (!paramsBean.isLockFirstRow()) {
            if (position == 0) {
                holder.itemLl.setBackgroundColor(ContextCompat.getColor(context, paramsBean.getFirstRowBgColor()));
                holder.item.setTextColor(ContextCompat.getColor(context, paramsBean.getHeadTextColor()));
            } else {
                holder.item.setTextColor(ContextCompat.getColor(context, paramsBean.getContentTextColor()));
            }
        } else {
            holder.item.setTextColor(ContextCompat.getColor(context, paramsBean.getContentTextColor()));
        }
        //添加事件
        if(onItemClickListener!=null){
            holder.itemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemSelectedListener!=null){
                        onItemSelectedListener.onItemSelected(v,position);
                    }
                    if(paramsBean.isLockFirstRow()){
                        onItemClickListener.onItemClick(v,position+1);
                    }else{
                        if(position!=0){
                            onItemClickListener.onItemClick(v,position);
                        }
                    }
                }
            });
        }
        if(onItemLongClickListener!=null){
            holder.itemLl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemSelectedListener!=null){
                        onItemSelectedListener.onItemSelected(v,position);
                    }
                    if (paramsBean.isLockFirstRow()){
                        onItemLongClickListener.onItemLongClick(v,position+1);
                    }else{
                        if(position!=0){
                            onItemLongClickListener.onItemLongClick(v,position);
                        }
                    }
                    return true;
                }
            });
        }
        //如果没有设置点击事件和长按事件
        if(onItemClickListener==null&&onItemLongClickListener==null){
            holder.itemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemSelectedListener!=null){
                        onItemSelectedListener.onItemSelected(v,position);
                    }
                }
            });
            holder.itemLl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemSelectedListener!=null){
                        onItemSelectedListener.onItemSelected(v,position);
                    }
                    return true;
                }
            });
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView item;
        LinearLayout itemLl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.tv_lock_column_item);
            itemLl = itemView.findViewById(R.id.ll_lock_column_item);
        }
    }

    /**
     * 设置表格所需参数
     */
    public void setParams(ParamsBean params){
        paramsBean = params;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
