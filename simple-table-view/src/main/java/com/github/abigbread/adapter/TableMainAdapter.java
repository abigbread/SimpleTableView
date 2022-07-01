package com.github.abigbread.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import java.util.List;

/**
 * @author abigbread
 * 时间：2022/6/29 15:18
 * 包名：com.abigbread.simpletableview
 * 简述：<表格主要视图部分适配器>
 */

public class TableMainAdapter extends RecyclerView.Adapter<TableMainAdapter.ViewHolder> {

    private Context context;
    /**
     * 表格主内容的数据
     *
     * 如果锁定第一行
     * 1. 如果锁定第一列，则此数据内不包含第一行第一列的数据
     * 2. 如果不锁定第一列，则此数据不包含第一行，但包含第一列的数据
     *
     * 如果不锁定第一行
     * 1. 如果锁定第一列，则此数据内包含第一行，不包含第一列的数据
     * 2. 如果不锁定第一列，则此数据包含第一行第一列的数据
     *
     */
    private ArrayList<ArrayList<String>> tableData;

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

    public TableMainAdapter(Context context, ArrayList<ArrayList<String>> tableData) {

        this.context = context;
        this.tableData = tableData;
    }

    @NonNull
    @Override
    public TableMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.table_main_item, null));
    }


    @Override
    public int getItemCount() {
        return tableData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TableMainAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ArrayList<String> data = tableData.get(position);
        if (paramsBean.isLockFirstRow()) {
            //第一行是锁定的
            createRowView(holder.itemLl, data, false, paramsBean.getRowMaxHeights().get(position + 1));
        } else {
            if (position == 0) {
                holder.itemLl.setBackgroundColor(ContextCompat.getColor(context, paramsBean.getFirstRowBgColor()));
                createRowView(holder.itemLl, data, true, paramsBean.getRowMaxHeights().get(position));
            } else {
                createRowView(holder.itemLl, data, false, paramsBean.getRowMaxHeights().get(position));
            }
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

        LinearLayout itemLl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLl = itemView.findViewById(R.id.ll_table_main_item);
        }
    }

    /**
     * 构造每行数据视图
     *
     * @param linearLayout
     * @param datas
     * @param isFristRow   是否是第一行
     */
    private void createRowView(LinearLayout linearLayout, List<String> datas, boolean isFristRow, int mMaxHeight) {
        //设置LinearLayout
        linearLayout.removeAllViews();//首先清空LinearLayout,复用会造成重复绘制，使内容超出预期长度
        for (int i = 0; i < datas.size(); i++) {
            //构造单元格
            TextView textView = new TextView(context);
            if (isFristRow) {
                textView.setTextColor(ContextCompat.getColor(context, paramsBean.getHeadTextColor()));
            } else {
                textView.setTextColor(ContextCompat.getColor(context, paramsBean.getContentTextColor()));
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, paramsBean.getTextSize());
            textView.setGravity(Gravity.CENTER);
            textView.setText(datas.get(i));
            //设置布局
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewParams.setMargins(paramsBean.getCellPadding(), paramsBean.getCellPadding(), paramsBean.getCellPadding(), paramsBean.getCellPadding());
            textViewParams.height = DisplayUtil.dip2px(context, mMaxHeight);
            if (paramsBean.isLockFirstColumn()) {
                textViewParams.width = DisplayUtil.dip2px(context, paramsBean.getColumnMaxWidths().get(i+1));
            } else {
                textViewParams.width = DisplayUtil.dip2px(context, paramsBean.getColumnMaxWidths().get(i));
            }
            Log.d("width",textViewParams.width+"=="+i);
            textView.setLayoutParams(textViewParams);
            linearLayout.addView(textView);
            //画分隔线
            if (i != datas.size() - 1) {
                View splitView = new View(context);
                ViewGroup.LayoutParams splitViewParmas = new ViewGroup.LayoutParams(DisplayUtil.dip2px(context, 1),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                splitView.setLayoutParams(splitViewParmas);
                if (isFristRow) {
                    splitView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    splitView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                }
                linearLayout.addView(splitView);
            }
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
