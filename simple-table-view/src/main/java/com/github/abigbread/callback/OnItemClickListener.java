package com.github.abigbread.callback;

import android.view.View;

/**
 * @author abigbread
 * 时间：2022/6/28 15:02
 * 包名：com.abigbread.simpletableview
 * 简述：<item 点击事件>
 */

public interface OnItemClickListener {
    /**
     * @param item     点击项
     * @param position 点击位置
     */
    void onItemClick(View item, int position);
}
