package com.github.abigbread.callback;

/**
 * @author abigbread
 * 时间：2022/6/29 14:08
 * 包名：com.abigbread.simpletableview
 * 简述：<表格横向滚动监听事件>
 */

public interface OnTableViewListener {
    /**
     * 滚动监听
     *
     * @param x
     * @param y
     */
    void onTableViewScrollChange(int x, int y);
}
