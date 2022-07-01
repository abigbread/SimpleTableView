package com.github.abigbread.callback;

import android.widget.HorizontalScrollView;

/**
 * @author abigbread
 * 时间：2022/6/29 14:15
 * 包名：com.abigbread.simpletableview
 * 简述：<表格创建完成回调>
 */

public interface OnTableViewCreatedListener {
    /**
     * 返回当前横向滚动视图给上个界面
     */
    void onTableViewCreatedCompleted(HorizontalScrollView hsv);
}
