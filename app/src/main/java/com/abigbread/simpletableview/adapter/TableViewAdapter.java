package com.abigbread.simpletableview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abigbread.simpletableview.R;
import com.abigbread.simpletableview.bean.ParamsBean;
import com.abigbread.simpletableview.callback.OnItemClickListener;
import com.abigbread.simpletableview.callback.OnItemLongClickListener;
import com.abigbread.simpletableview.callback.OnItemSelectedListener;
import com.abigbread.simpletableview.callback.OnTableViewCreatedListener;
import com.abigbread.simpletableview.callback.OnTableViewListener;

import java.util.ArrayList;

/**
 * @author abigbread
 * 时间：2022/6/29 13:37
 * 包名：com.abigbread.simpletableview
 * 简述：<表格主视图适配器>
 */

public class TableViewAdapter extends RecyclerView.Adapter<TableViewAdapter.ViewHolder> {

    private Context context;
    /**
     * 第一列的数据（不包含第一行）
     */
    private ArrayList<String> firstColumnData;
    /**
     * 表格主内容的数据
     * <p>
     * 如果锁定第一行
     * 1. 如果锁定第一列，则此数据内不包含第一行第一列的数据
     * 2. 如果不锁定第一列，则此数据不包含第一行，但包含第一列的数据
     * <p>
     * 如果不锁定第一行
     * 1. 如果锁定第一列，则此数据内包含第一行，不包含第一列的数据
     * 2. 如果不锁定第一列，则此数据包含第一行第一列的数据
     */
    private ArrayList<ArrayList<String>> tableData;
    /**
     * 是否锁定第一列
     */
    private boolean isLockFirstColumn;
    /**
     * 是否锁定第一行
     */
    private boolean isLockFirstRow;

    /**
     * 表格所需的参数
     */
    private ParamsBean paramsBean;


    /**
     * 表格横向滚动监听事件
     */
    private static OnTableViewListener tableHScrollListener;
    /**
     * 表格横向滚动监听事件
     */
    private OnTableViewCreatedListener onTableViewCreatedListener;

    /**
     * Item点击事件
     */
    private OnItemClickListener onItemClickListener;

    /**
     * Item长按事件
     */
    private OnItemLongClickListener onItemLongClickListener;
    /**
     * Item选中样式
     */
    private int itemSelectedColor;

    /**
     * 锁定后的第一列的适配器
     */
    private LockFirstColumnAdapter firstColumnAdapter;
    private TableMainAdapter mainAdapter;

    public TableViewAdapter(Context context, ArrayList<String> firstColumnData, ArrayList<ArrayList<String>> tableData,
                            boolean isLockFirstColumn, boolean isLockFirstRow) {
        this.context = context;
        this.firstColumnData = firstColumnData;
        this.tableData = tableData;
        this.isLockFirstColumn = isLockFirstColumn;
        this.isLockFirstRow = isLockFirstRow;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.table_view_item, null));
        if (onTableViewCreatedListener != null) {
            onTableViewCreatedListener.onTableViewCreatedCompleted(viewHolder.tableHsv);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (isLockFirstColumn) {
            //构造锁定视图
            holder.lockColumnRv.setVisibility(View.VISIBLE);
            if (firstColumnAdapter == null) {
                firstColumnAdapter = new LockFirstColumnAdapter(context, firstColumnData);
                firstColumnAdapter.setParams(paramsBean);
                //判断是否开启item选中效果
                if (paramsBean.isItemSelectedStatus()) {
                    firstColumnAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(View view, int position) {
                            selectedSetting(holder, position);
                        }
                    });
                }

                if (onItemClickListener != null) {
                    firstColumnAdapter.setOnItemClickListener(onItemClickListener);
                }
                if (onItemLongClickListener != null) {
                    firstColumnAdapter.setOnItemLongClickListener(onItemLongClickListener);
                }
                holder.lockColumnRv.setLayoutManager(layoutManager);
                holder.lockColumnRv.addItemDecoration(new DividerItemDecoration(context
                        , DividerItemDecoration.VERTICAL));
                holder.lockColumnRv.setAdapter(firstColumnAdapter);
            } else {
                firstColumnAdapter.notifyDataSetChanged();
            }
        } else {
            holder.lockColumnRv.setVisibility(View.GONE);
        }

        //构造主表格视图
        if (mainAdapter == null) {
            mainAdapter = new TableMainAdapter(context, tableData);
            paramsBean.setLockFirstRow(isLockFirstRow);
            paramsBean.setLockFirstColumn(isLockFirstColumn);
            mainAdapter.setParams(paramsBean);
            //判断是否开启item选中效果
            if (paramsBean.isItemSelectedStatus()) {
                mainAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(View view, int position) {
                        selectedSetting(holder, position);
                    }
                });
            }
            if (onItemClickListener != null) {
                mainAdapter.setOnItemClickListener(onItemClickListener);
            }
            if (onItemLongClickListener != null) {
                mainAdapter.setOnItemLongClickListener(onItemLongClickListener);
            }
            LinearLayoutManager unlockLayoutManager = new LinearLayoutManager(context);
            unlockLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.tableRv.setLayoutManager(unlockLayoutManager);
            holder.tableRv.addItemDecoration(new DividerItemDecoration(context
                    , DividerItemDecoration.VERTICAL));
            holder.tableRv.setAdapter(mainAdapter);
        } else {
            mainAdapter.notifyDataSetChanged();
        }

    }

    private void selectedSetting(ViewHolder holder, int position) {
        if (isLockFirstColumn) {
            RecyclerView.LayoutManager mLockLayoutManager = holder.lockColumnRv.getLayoutManager();
            int itemCount = mLockLayoutManager.getItemCount();
            View item = mLockLayoutManager.getChildAt(position);
            item.setBackgroundColor(ContextCompat.getColor(context, itemSelectedColor));
            for (int i = 0; i < itemCount; i++) {
                if (i != position) {
                    mLockLayoutManager.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
        RecyclerView.LayoutManager mUnLockLayoutManager = holder.tableRv.getLayoutManager();
        int itemUnLockCount = mUnLockLayoutManager.getItemCount();
        View mUnlockItem = mUnLockLayoutManager.getChildAt(position);
        mUnlockItem.setBackgroundColor(ContextCompat.getColor(context, itemSelectedColor));
        for (int i = 0; i < itemUnLockCount; i++) {
            if (i != position) {
                mUnLockLayoutManager.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }



    }

    @Override
    public int getItemCount() {
        return 1;//在此RecycleView中再创建两个RecycleView,具体数据在内部两个RecycleView中,所以这里只返回1
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView lockColumnRv, tableRv;//锁定第一列的数据展示容器，表格主要数据的展示容器
        HorizontalScrollView tableHsv;//包裹表格主要内容的HSV

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lockColumnRv = itemView.findViewById(R.id.rv_lock_column);
            tableRv = itemView.findViewById(R.id.rv_table);
            tableHsv = itemView.findViewById(R.id.hsv_table);
            //解决滑动冲突，只保留最外层 RecyclerView 的上下滑动事件，lockColumnRv 和 tableRv 设置不能滑动
            lockColumnRv.setFocusable(false);
            tableRv.setFocusable(false);
            tableHsv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int l, int t, int r, int b) {
                    if (tableHScrollListener != null) {
                        tableHScrollListener.onTableViewScrollChange(l, l);
                    }
                }
            });
        }

    }


    /**
     * 设置表格所需参数
     */
    public void setParams(ParamsBean params) {
        paramsBean = params;
    }


    /**
     * 设置横向滚动监听
     */
    public void setHorizontalScrollView(OnTableViewListener tableViewListener) {
        this.tableHScrollListener = tableViewListener;
    }

    public void setOnTableViewCreatedListener(OnTableViewCreatedListener onTableViewCreatedListener) {
        this.onTableViewCreatedListener = onTableViewCreatedListener;
    }

//    public void setTableViewRangeListener(LockTableView.OnTableViewRangeListener mTableViewRangeListener) {
//        this.mTableViewRangeListener = mTableViewRangeListener;
//    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setItemSelectedColor(int itemSelectedColor) {
        this.itemSelectedColor = itemSelectedColor;
    }


}
