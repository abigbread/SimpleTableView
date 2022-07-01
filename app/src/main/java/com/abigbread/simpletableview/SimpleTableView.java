package com.abigbread.simpletableview;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abigbread.simpletableview.adapter.TableViewAdapter;
import com.abigbread.simpletableview.bean.ParamsBean;
import com.abigbread.simpletableview.callback.OnItemClickListener;
import com.abigbread.simpletableview.callback.OnItemLongClickListener;
import com.abigbread.simpletableview.callback.OnTableViewCreatedListener;
import com.abigbread.simpletableview.callback.OnTableViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author abigbread
 * 时间：2022/6/28 14:01
 * 包名：com.abigbread.simpletableview
 * 简述：<行列可锁定的表格>
 */

public class SimpleTableView {


    private static TableViewAdapter tableViewAdapter;
    private final boolean isLockFirstRow;
    private final boolean isLockFirstColumn;
    Builder builder;

    private SimpleTableView(Builder builder) {
        this.isLockFirstRow = builder.isLockFirstRow;
        this.isLockFirstColumn = builder.isLockFirstColumn;
        this.builder = builder;

    }

    /**
     * 判断当前表格是否锁定首行
     *
     * @return true 表示锁定首行
     */
    public boolean isLockFirstRow() {
        return isLockFirstRow;
    }

    /**
     * 判断当前表格是否锁定首列
     *
     * @return true 表示锁定首列
     */
    public boolean isLockFirstColumn() {
        return isLockFirstColumn;
    }

    /**
     * 设置数据
     *
     * @param data 未处理的表格数据
     */
    public void setData(ArrayList<ArrayList<String>> data) {
        builder.setTableData(data);
    }

    public static class Builder {

        private final String TAG = SimpleTableView.class.getSimpleName();

        /**
         * 上下文
         */
        private Context context;
        /**
         * 表格父视图
         */
        private ViewGroup containerView;
        /**
         * 表格数据（未处理的完整数据）
         */
        private ArrayList<ArrayList<String>> data;

        /**
         * 表格视图
         */
        private View tableView;


        //表格视图相关 start --->>>

        /**
         * 表格左上角视图（第一行第一列交叉的那一格）
         */
        private TextView lockColumnHeadTitle;
        /**
         * 第一行布局（锁定第一列）
         */
        private LinearLayout lockColumnHeadLl;
        /**
         * 第一行布局（未锁定第一列）
         */
        private LinearLayout unLockColumnHeadLl;
        /**
         * 第一行滚动视图（锁定第一列）
         */
        private HorizontalScrollView lockColumnHeadHsv;
        /**
         * 第一行滚动视图（未锁定第一列）
         */
        private HorizontalScrollView unLockColumnHeadHsv;
        /**
         * 表格主视图
         */
        private RecyclerView tableMainRv;
        /**
         * 表格主视图适配器
         */
        private TableViewAdapter tableViewAdapter;

        //表格视图相关 <<< --- end


        //表格数据相关 start --->>>

        /**
         * 表格左上角数据（第一行，第一列的位置）
         */
        private String columnTitle;

        /**
         * 表格第一行数据
         * 如果锁定第一列，则不包括第一个元素
         * 如果不锁定第一列，则是原本数据中第一行的数据
         */
        private ArrayList<String> tableFirstRowData = new ArrayList<>();
        /**
         * 表格第一列数据，不包括第一个元素
         */
        private ArrayList<String> tableFirstColumnData = new ArrayList<>();
        /**
         * 表格每一行的数据
         * <p>
         * 如果锁定第一行
         * 1.如果锁定第一列，则不包括第一行和第一列的数据
         * 2.如果不锁定第一列，则只不包括第一行
         * <p>
         * 如果不锁定第一行
         * 1.如果锁定第一列，则不包括第一列的数据（每行第一个数据）
         * 2.如果不锁定第一列，则是原本的数据 data
         */
        private ArrayList<ArrayList<String>> tableRowData = new ArrayList<ArrayList<String>>();

        /**
         * 记录每列最大宽度
         */
        private ArrayList<Integer> columnMaxWidths = new ArrayList<Integer>();
        /**
         * 记录每行最大高度
         */
        private ArrayList<Integer> rowMaxHeights = new ArrayList<Integer>();
        /**
         * 把所有的滚动视图放图列表，后面实现联动效果
         */
        private ArrayList<HorizontalScrollView> mScrollViews = new ArrayList<HorizontalScrollView>();

        //表格数据相关 <<< --- end


        //相关参数设置 start --->>>

        /**
         * 是否锁定第一行
         */
        private boolean isLockFirstRow;

        /**
         * 是否锁定第一列
         */
        private boolean isLockFirstColumn;

        /**
         * 最大列宽
         */
        private int maxColumnWidth;

        /**
         * 最小列宽
         */
        private int minColumnWidth;

        /**
         * 最大行高
         */
        private int maxRowHeight;

        /**
         * 最小行高
         */
        private int minRowHeight;

        /**
         * 第一行的背景颜色
         */
        private int firstRowBgColor;

        /**
         * 为 null 或 “” 的时候所用的占位字符串
         */
        private String emptyString;

        /**
         * 表格显示的字体大小
         */
        private int textSize;
        /**
         * 表格头部的文字颜色（第一行）
         */
        private int headTextColor;

        /**
         * 表格内容中的文字颜色（第一行以外）
         */
        private int contentTextColor;

        /**
         * 表格内边距
         */
        private int cellPadding;

        /**
         * item 选中效果
         */
        private boolean itemSelectedStatus;
        /**
         * Item 点击事件
         */
        private OnItemClickListener onItemClickListener;
        /**
         * Item 长按事件
         */
        private OnItemLongClickListener onItemLongClickListener;

        /**
         * 表格横向滚动监听事件
         */
        private OnTableViewListener tableViewListener;

        /**
         * 要改变的列集合
         */
        private HashMap<Integer, Integer> changeColumns = new HashMap<>();


        /**
         * Item选中样式
         */
        private int itemSelectedColor;
        //相关参数设置 <<< --- end

        /**
         * @param context       上下文
         * @param containerView 表格父视图
         * @param data          表格数据
         */
        public Builder(Context context, ViewGroup containerView, ArrayList<ArrayList<String>> data) {
            this.context = context;
            this.containerView = containerView;
            this.data = data;
            initAttrs();


        }

        /**
         * 初始化默认参数
         */
        private void initAttrs() {
            tableView = LayoutInflater.from(context).inflate(R.layout.table_view_layout, null);
            maxColumnWidth = 100;
            minColumnWidth = 70;
            minRowHeight = 20;
            maxRowHeight = 60;
            emptyString = "N/A";
            headTextColor = R.color.head_text_color;
            contentTextColor = R.color.content_text_color;
            firstRowBgColor = R.color.head_bg;
            textSize = 16;
            cellPadding = DisplayUtil.dip2px(context, 45);
        }

        /**
         * 设置是否锁定第一行
         *
         * @param lockFirstRow true 锁定
         */
        public Builder setLockFirstRow(boolean lockFirstRow) {
            this.isLockFirstRow = lockFirstRow;
            return this;
        }

        /**
         * 设置是否锁定第一列
         *
         * @param lockFirstColumn true 锁定
         */
        public Builder setLockFirstColumn(boolean lockFirstColumn) {
            this.isLockFirstColumn = lockFirstColumn;
            return this;
        }

        /**
         * 设置表格中一列的最大宽度
         *
         * @param maxColumnWidth 最大列宽
         */
        public Builder setMaxColumnWidth(int maxColumnWidth) {
            this.maxColumnWidth = maxColumnWidth;
            return this;
        }

        /**
         * 设置表格中一列的最小宽度
         *
         * @param minColumnWidth 最小列宽
         */
        public Builder setMinColumnWidth(int minColumnWidth) {
            this.minColumnWidth = minColumnWidth;
            return this;
        }

        /**
         * 设置表格中一行的最大高度
         *
         * @param maxRowHeight 最大行高
         */
        public Builder setMaxRowHeight(int maxRowHeight) {
            this.maxRowHeight = maxRowHeight;
            return this;
        }

        /**
         * 设置表格中一行的最小高度
         *
         * @param minRowHeight 最小行高
         */
        public Builder setMinRowHeight(int minRowHeight) {
            this.minRowHeight = minRowHeight;
            return this;
        }

        /**
         * 设置第一行的背景颜色
         *
         * @param firstRowBgColor 背景颜色
         */
        public Builder setFirstRowBgColor(int firstRowBgColor) {
            this.firstRowBgColor = firstRowBgColor;
            return this;
        }

        /**
         * 设置 null 或 “” 的占位字符串
         *
         * @param emptyString 占位字符串
         */
        public Builder setEmptyString(String emptyString) {
            this.emptyString = emptyString;
            return this;
        }

        /**
         * 设置表格中文字的大小
         *
         * @param textSize 字体大小
         */
        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 设置表格头部的文字颜色（第一行）
         *
         * @param headTextColor 文字颜色
         */
        public Builder setHeadTextColor(int headTextColor) {
            this.headTextColor = headTextColor;
            return this;
        }

        /**
         * 设置表格内容的文字颜色（第一行之外）
         *
         * @param contentTextColor 文字颜色
         */
        public Builder setContentTextColor(int contentTextColor) {
            this.contentTextColor = contentTextColor;
            return this;
        }

        /**
         * 设置表格文字的边距
         *
         * @param cellPadding
         * @return
         */
        public Builder setCellPadding(int cellPadding) {
            this.cellPadding = DisplayUtil.dip2px(context, cellPadding);
            return this;
        }

        /**
         * @param itemSelectedStatus true 则item有选中效果
         */
        public Builder setItemSelectedStatus(boolean itemSelectedStatus) {
            this.itemSelectedStatus = itemSelectedStatus;
            return this;
        }

        /**
         * 指定第几列对应的宽度
         *
         * @param columnNum   第几列
         * @param columnWidth 宽度
         */
        public Builder setColumnWidth(int columnNum, int columnWidth) {
            //判断是否已经设置过
            if (changeColumns.containsKey(columnNum)) {
                changeColumns.remove(columnNum);
            }
            changeColumns.put(columnNum, columnWidth);
            return this;
        }

        /**
         * 使用默认的配置
         */
        public Builder useDefaultOptions() {
            this.setLockFirstColumn(true) //是否锁定第一列
                    .setLockFirstRow(true) //是否锁定第一行
                    .setMaxColumnWidth(100) //列最大宽度
                    .setMinColumnWidth(60) //列最小宽度
                    .setMinRowHeight(20)//行最小高度
                    .setMaxRowHeight(60)//行最大高度
                    .setTextSize(16) //单元格字体大小
                    .setCellPadding(15)//设置单元格内边距(dp)
                    .setEmptyString("N/A")
                    .setItemSelectedStatus(true)//设置item选中效果是否开启
                    .setItemSelectedColor(R.color.selected_bg)//设置item选中颜色
                    .setContentTextColor(R.color.white)
                    .setFirstRowBgColor(R.color.head_bg)//表头背景色
                    .setHeadTextColor(R.color.head_text_color)//表头字体颜色
                    .setContentTextColor(R.color.content_text_color);//单元格字体颜色;//空值替换值
            return this;
        }

        /**
         * 选中item的颜色
         *
         * @param itemSelected 资源id
         */
        public Builder setItemSelectedColor(int itemSelected) {
            this.itemSelectedColor = itemSelected;
            return this;
        }

        /**
         * 设置横向滚动监听
         */
        public Builder setTableViewListener(OnTableViewListener tableViewListener) {
            this.tableViewListener = tableViewListener;
            return this;
        }

        /**
         * 设置 item 点击事件监听
         */
        public Builder setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        /**
         * 设置 item 长按事件监听
         */
        public Builder setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
            return this;
        }

        /**
         * 数据刷新时，重新设值
         *
         * @param data 未处理的原始表格数据
         */
        public void setTableData(ArrayList<ArrayList<String>> data) {
            this.data.clear();
            this.data.addAll(data);
            tableFirstRowData.clear();
            tableFirstColumnData.clear();
            tableRowData.clear();
            columnMaxWidths.clear();
            rowMaxHeights.clear();
            initData();
            updateFirstRow();
            tableViewAdapter.notifyDataSetChanged();
        }


        public SimpleTableView create() {
            initData();
            initView();
            containerView.removeAllViews();//清空视图
            containerView.addView(tableView);
            return new SimpleTableView(this);
        }

        /**
         * 更新表格第一行标题（锁定状态下才需要重新创建）
         */
        private void updateFirstRow() {
            if (isLockFirstRow) {
                createHeadView();
            }
        }

        /**
         * 初始化表格数据格式
         */
        private void initData() {

            if (data != null && data.size() > 0) {
                formatTable();
                initColumnMaxWidth();
                initRowMaxHeight();
                formatTableData();
            } else {
                //数据为空
            }
        }


        /**
         * 初始化表格视图
         */
        private void initView() {
            //表格左上角视图（第一行第一列交叉的那一格）
            lockColumnHeadTitle = tableView.findViewById(R.id.tv_lock_column_text);
            //第一行布局（锁定第一列）
            lockColumnHeadLl = tableView.findViewById(R.id.ll_lock_column_head);
            //第一行布局（未锁定第一列）
            unLockColumnHeadLl = tableView.findViewById(R.id.ll_unlock_column_head);
            //第一行滚动视图（锁定第一列）
            lockColumnHeadHsv = tableView.findViewById(R.id.hsv_lock_column_head);
            //第一行滚动视图（未锁定第一列）
            unLockColumnHeadHsv = tableView.findViewById(R.id.hsv_unlock_column_head);
            //表格主视图
            tableMainRv = tableView.findViewById(R.id.rv_table_main);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            tableMainRv.setLayoutManager(layoutManager);

            tableViewAdapter = new TableViewAdapter(context, tableFirstColumnData, tableRowData, isLockFirstColumn, isLockFirstRow);
            ParamsBean paramsBean = new ParamsBean();
            paramsBean.setCellPadding(cellPadding);
            paramsBean.setColumnMaxWidths(columnMaxWidths);
            paramsBean.setRowMaxHeights(rowMaxHeights);
            paramsBean.setTextSize(textSize);
            paramsBean.setContentTextColor(contentTextColor);
            paramsBean.setHeadTextColor(headTextColor);
            paramsBean.setFirstRowBgColor(firstRowBgColor);
            paramsBean.setItemSelectedStatus(itemSelectedStatus);
            tableViewAdapter.setParams(paramsBean);

            tableViewAdapter.setHorizontalScrollView(new OnTableViewListener() {
                @Override
                public void onTableViewScrollChange(int x, int y) {
                    changeAllScrollView(x, y);
                }
            });


            tableViewAdapter.setOnTableViewCreatedListener(new OnTableViewCreatedListener() {
                @Override
                public void onTableViewCreatedCompleted(HorizontalScrollView hsv) {
                    mScrollViews.add(hsv);
                }
            });

            tableMainRv.setAdapter(tableViewAdapter);

            lockColumnHeadLl.setBackgroundColor(ContextCompat.getColor(context, firstRowBgColor));
            unLockColumnHeadLl.setBackgroundColor(ContextCompat.getColor(context, firstRowBgColor));
            if (isLockFirstRow) {
                if (isLockFirstColumn) {
                    lockColumnHeadLl.setVisibility(View.VISIBLE);
                    unLockColumnHeadLl.setVisibility(View.GONE);
                } else {
                    lockColumnHeadLl.setVisibility(View.GONE);
                    unLockColumnHeadLl.setVisibility(View.VISIBLE);
                }
                createHeadView();
            } else {
                lockColumnHeadLl.setVisibility(View.GONE);
                unLockColumnHeadLl.setVisibility(View.GONE);
            }

            if (onItemClickListener != null) {
                tableViewAdapter.setOnItemClickListener(onItemClickListener);
            }
            if (onItemLongClickListener != null) {
                tableViewAdapter.setOnItemLongClickListener(onItemLongClickListener);
            }
            if (itemSelectedColor != 0) {
                tableViewAdapter.setItemSelectedColor(itemSelectedColor);
            } else {
                tableViewAdapter.setItemSelectedColor(R.color.dash_line_color);
            }


        }

        /**
         * 创建头部视图
         */
        private void createHeadView() {
            if (isLockFirstColumn) {
                lockColumnHeadHsv.removeAllViews();
                lockColumnHeadTitle.setTextColor(ContextCompat.getColor(context, headTextColor));
                lockColumnHeadTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                lockColumnHeadTitle.setText(columnTitle);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) lockColumnHeadTitle.getLayoutParams();
                layoutParams.width = DisplayUtil.dip2px(context, columnMaxWidths.get(0));
                layoutParams.height = DisplayUtil.dip2px(context, rowMaxHeights.get(0));
                layoutParams.setMargins(cellPadding, cellPadding, cellPadding, cellPadding);
                lockColumnHeadTitle.setLayoutParams(layoutParams);
                createFirstRow(lockColumnHeadHsv, tableFirstRowData);
                if (!mScrollViews.contains(lockColumnHeadHsv)) {
                    mScrollViews.add(lockColumnHeadHsv);
                }
                lockColumnHeadHsv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int l, int t, int r, int b) {
                        changeAllScrollView(l, t);
                    }
                });
            } else {
                unLockColumnHeadHsv.removeAllViews();
                createFirstRow(unLockColumnHeadHsv, tableFirstRowData);
                if (!mScrollViews.contains(unLockColumnHeadHsv)) {
                    mScrollViews.add(unLockColumnHeadHsv);
                }
                unLockColumnHeadHsv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int l, int t, int r, int b) {
                        changeAllScrollView(l, t);
                    }
                });
            }
        }

        /**
         * 构造可以滚动的表格第一行视图（表格头）
         *
         * @param data 第一行的数据
         */
        private void createFirstRow(HorizontalScrollView scrollView, List<String> data) {
            //设置LinearLayout
            LinearLayout linearLayout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0; i < data.size(); i++) {
                //构造单元格
                TextView textView = new TextView(context);
                textView.setTextColor(ContextCompat.getColor(context, headTextColor));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                textView.setGravity(Gravity.CENTER);
                textView.setText(data.get(i));
                //设置布局
                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                textViewParams.setMargins(cellPadding, cellPadding, cellPadding, cellPadding);
                textView.setLayoutParams(textViewParams);
                ViewGroup.LayoutParams textViewParamsCopy = textView.getLayoutParams();
                if (isLockFirstColumn) {
                    textViewParamsCopy.width = DisplayUtil.dip2px(context, columnMaxWidths.get(i + 1));
                } else {
                    textViewParamsCopy.width = DisplayUtil.dip2px(context, columnMaxWidths.get(i));
                }
                linearLayout.addView(textView);
                //画分隔线
                if (i != data.size() - 1) {
                    View splitView = new View(context);
                    ViewGroup.LayoutParams splitViewParmas = new ViewGroup.LayoutParams(DisplayUtil.dip2px(context, 1),
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    splitView.setLayoutParams(splitViewParmas);
                    splitView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    linearLayout.addView(splitView);
                }
            }

            scrollView.addView(linearLayout);
        }


        /**
         * 格式化，将数据不整齐（每行长度不一样）的部分用默认值填充
         */
        private void formatTable() {
            //表格中最长的那一行的长度
            int rowMaxLength = 0;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).size() >= rowMaxLength) {
                    rowMaxLength = data.get(i).size();
                }
                //一行的数据
                ArrayList<String> rowData = data.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    //为 null 或 “” 则替换为 emptyString
                    if (rowData.get(j) == null || rowData.get(j).equals("")) {
                        rowData.set(j, emptyString);
                    }
                }
                data.set(i, rowData);
            }
            //如果表格数据各行的长度不一致，以最长为那一行为标准，其他不足的填"N/A"字符串
            for (int i = 0; i < data.size(); i++) {
                ArrayList<String> rowData = data.get(i);
                if (rowData.size() < rowMaxLength) {
                    int size = rowMaxLength - rowData.size();
                    for (int j = 0; j < size; j++) {
                        rowData.add(emptyString);
                    }
                    data.set(i, rowData);
                }
            }

        }

        /**
         * 初始化每列最大宽度
         */
        private void initColumnMaxWidth() {

            for (int i = 0; i < data.size(); i++) {

                ArrayList<String> rowData = data.get(i);

                for (int j = 0; j < rowData.size(); j++) {

                    TextView textView = new TextView(context);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                    textView.setText(rowData.get(j));
                    textView.setGravity(Gravity.CENTER);
                    //设置布局
                    LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    textViewParams.setMargins(cellPadding, cellPadding, cellPadding, cellPadding);//android:layout_margin="15dp"
                    textView.setLayoutParams(textViewParams);

                    if (i == 0) {
                        //将第一行中每格的列宽记录在 columnMaxWidths 中
                        columnMaxWidths.add(measureTextWidth(textView, rowData.get(j)));
                    } else {
                        //其他行中如果还有更宽的，则取代 columnMaxWidths 中对应的宽度
                        int length = columnMaxWidths.get(j);
                        int currentWidth = measureTextWidth(textView, rowData.get(j));
                        if (currentWidth > length) {
                            columnMaxWidths.set(j, currentWidth);
                        }
                    }
                }
            }

            //如果用户指定某列宽度，则按照用户指定宽度计算
            if (changeColumns.size() > 0) {
                for (Integer key : changeColumns.keySet()) {
                    changeColumnWidth(key, changeColumns.get(key));
                }
            }
        }

        /**
         * 初始化每行的最大高度
         */
        private void initRowMaxHeight() {
            //初始化每行最大高度
            for (int i = 0; i < data.size(); i++) {

                ArrayList<String> rowData = data.get(i);

                TextView textView = new TextView(context);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                textView.setGravity(Gravity.CENTER);
                //设置布局
                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                textViewParams.setMargins(cellPadding, cellPadding, cellPadding, cellPadding);//android:layout_margin="15dp"
                textView.setLayoutParams(textViewParams);

                int maxHeight = measureTextHeight(textView, rowData.get(0));
                //将每一行中的高度记录在 rowMaxHeights 中
                rowMaxHeights.add(maxHeight);

                for (int j = 0; j < rowData.size(); j++) {
                    int currentHeight;
                    //如果用户指定某列宽度，则按照用户指定宽度算对应列的高度
                    if (changeColumns.size() > 0 && changeColumns.containsKey(j)) {
                        currentHeight = measureTextHeight(textView, rowData.get(j), changeColumns.get(j));
                    } else {
                        currentHeight = measureTextHeight(textView, rowData.get(j));
                    }
                    if (currentHeight > maxHeight) {
                        rowMaxHeights.set(i, currentHeight);
                    }
                }
            }
        }

        /**
         * 格式化表格数据
         * 根据不同情况（锁定首行、锁定首列），数据会被装在不同的集合中
         */
        private void formatTableData() {
            //锁定第一行
            if (isLockFirstRow) {
                ArrayList<String> firstRowData = (ArrayList<String>) data.get(0).clone();
                //锁定第一列
                if (isLockFirstColumn) {
                    //左上角（第一行与第一列交叉的那一格）
                    columnTitle = firstRowData.get(0);
                    firstRowData.remove(0);

                    //不包含第一行的第一个数据
                    tableFirstRowData.addAll(firstRowData);

                    //构造第一列数据,并且构造表格每行数据，i 从 1 开始
                    for (int i = 1; i < data.size(); i++) {
                        ArrayList<String> rowData = (ArrayList<String>) data.get(i).clone();

                        //将每行中的第一个数据添加到锁定的列 tableFirstColumnData 中
                        tableFirstColumnData.add(rowData.get(0));
                        rowData.remove(0);

                        //所有行的数据（不包含第一行，不包含第一列）
                        tableRowData.add(rowData);
                    }
                } else {
                    //不锁定第一列，则将原始数据的第一行添加到 tableFirstRowData 中
                    tableFirstRowData.addAll(firstRowData);

                    // i 从 1 开始
                    for (int i = 1; i < data.size(); i++) {
                        //所有行的数据（不包含第一行）
                        tableRowData.add(data.get(i));
                    }
                }
            } else {//不锁定第一行

                //锁定第一列
                if (isLockFirstColumn) {
                    //构造第一列数据,并且构造表格每行数据
                    for (int i = 0; i < data.size(); i++) {
                        ArrayList<String> rowData = (ArrayList<String>) data.get(i).clone();

                        //将每行中的第一个数据添加到 tableFirstColumnData 中
                        tableFirstColumnData.add(rowData.get(0));
                        rowData.remove(0);

                        //所有行的数据（不包含每行中的第一个元素）
                        tableRowData.add(rowData);
                    }
                } else {
                    //不锁定第一列
                    tableRowData.addAll(data);
                }
            }
        }

        /**
         * 根据最大最小值，计算表格中的一格的宽度
         *
         * @param textView 表格中的一格
         * @param text     文字
         * @return 表格一格的宽度
         */
        private int measureTextWidth(TextView textView, String text) {
            if (textView != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                int width = DisplayUtil.px2dip(context, layoutParams.leftMargin) +
                        DisplayUtil.px2dip(context, layoutParams.rightMargin) +
                        getTextViewWidth(textView, text);
                if (width <= minColumnWidth) {
                    return minColumnWidth;
                } else if (width <= maxColumnWidth) {
                    return width;
                } else {
                    return maxColumnWidth;
                }
            }
            return 0;
        }

        /**
         * 计算表格中的一格的高度
         *
         * @param textView 表格中的一格
         * @param text     文字
         * @return 表格一格的高度
         */
        private int measureTextHeight(TextView textView, String text) {
            if (textView != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                int height = getTextViewHeight(textView, text);
                if (height < minRowHeight) {
                    return minRowHeight;
                } else if (height > minRowHeight && height < maxRowHeight) {
                    return height;
                } else {
                    return maxRowHeight;
                }
            }
            return 0;
        }

        /**
         * 计算表格中的一格的高度
         *
         * @param textView 表格中的一格
         * @param text     文字
         * @param width    指定宽度
         * @return 表格一格的高度
         */
        private int measureTextHeight(TextView textView, String text, int width) {
            if (textView != null) {
                int height = getTextViewHeight(textView, text, width);
                if (height < minRowHeight) {
                    return minRowHeight;
                } else if (height > minRowHeight && height < maxRowHeight) {
                    return height;
                } else {
                    return maxRowHeight;
                }
            }
            return 0;
        }

        /**
         * 根据文字计算表格一格所占的高度
         *
         * @param textView 表格中的一格
         * @param text     文字
         * @return 表格一格的高度
         */
        private int getTextViewHeight(TextView textView, String text) {
            if (textView != null) {
                int width = measureTextWidth(textView, text);
                TextPaint textPaint = textView.getPaint();
                //StaticLayout 计算自动换行后的行高
                StaticLayout staticLayout = new StaticLayout(text, textPaint, DisplayUtil.dip2px(context, width), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                return DisplayUtil.px2dip(context, staticLayout.getHeight());
            }
            return 0;
        }

        /**
         * 根据文字 和 指定宽度 计算表格一格所占的高度
         *
         * @param textView 表格中的一格
         * @param text     文字
         * @param width    指定宽度
         * @return 表格一格的高度
         */
        private int getTextViewHeight(TextView textView, String text, int width) {
            if (textView != null) {
                TextPaint textPaint = textView.getPaint();
                //StaticLayout 计算自动换行后的行高
                StaticLayout staticLayout = new StaticLayout(text, textPaint, DisplayUtil.dip2px(context, width), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                return DisplayUtil.px2dip(context, staticLayout.getHeight());
            }
            return 0;
        }

        /**
         * 根据文字计算表格一格所占的宽度
         *
         * @param textView 表格中的一格
         * @param text     表格一格中的文字
         * @return 表格一格的宽度
         */
        private int getTextViewWidth(TextView textView, String text) {
            if (textView != null) {
                TextPaint paint = textView.getPaint();
                return DisplayUtil.px2dip(context, (int) paint.measureText(text));
            }
            return 0;
        }


        /**
         * 改变指定列指定宽度
         *
         * @param columnNum   第几列
         * @param columnWidth 指定宽度
         */
        private void changeColumnWidth(int columnNum, int columnWidth) {
            if (columnMaxWidths != null && columnMaxWidths.size() > 0) {
                if (columnNum < columnMaxWidths.size() && columnNum >= 0) {
                    columnMaxWidths.set(columnNum, columnWidth);//加上左右外边距
                } else {
                    Log.e(TAG, "指定列不存在");
                }
            }
        }

        /**
         * 改变所有滚动视图位置
         *
         * @param x
         * @param y
         */
        private void changeAllScrollView(int x, int y) {
            if (mScrollViews.size() > 0) {
                if (tableViewListener != null) {
                    tableViewListener.onTableViewScrollChange(x, y);
                }
                for (int i = 0; i < mScrollViews.size(); i++) {
                    HorizontalScrollView scrollView = mScrollViews.get(i);
                    scrollView.scrollTo(x, y);
                }
            }
        }


    }

}
