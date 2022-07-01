package com.github.abigbread.callback;

import android.view.View;

/**
 * @author abigbread
 * 时间：2022/6/28 15:05
 * 包名：com.abigbread.simpletableview
 * 简述：<item 长按事件>
 */

public interface OnItemLongClickListener {
    /**
     * @param item     点击项
     * @param position 点击位置
     */
    void onItemLongClick(View item, int position);
}
