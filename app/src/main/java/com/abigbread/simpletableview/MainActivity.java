package com.abigbread.simpletableview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ArrayList<String>> mTableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        LinearLayout container = findViewById(R.id.container);
        SimpleTableView simpleTableView = new SimpleTableView.Builder(this, container, mTableData)
                .setLockFirstRow(true)
                .setLockFirstColumn(true)
                .create();

        Log.d("table", "lockFirstRow:" + simpleTableView.isLockFirstColumn());
        Log.d("table", "lockFirstColumn:" + simpleTableView.isLockFirstColumn());
    }

    private void initData() {
        //构造假数据
        mTableData = new ArrayList<ArrayList<String>>();
        ArrayList<String> mFirstData = new ArrayList<String>();
        mFirstData.add("标题");
        for (int i = 0; i < 10; i++) {
            mFirstData.add("标题" + i);
        }
        mTableData.add(mFirstData);
        for (int i = 0; i < 20; i++) {
            ArrayList<String> mRowDatas = new ArrayList<String>();
            mRowDatas.add("标题" + i);
            for (int j = 0; j < 10; j++) {
                mRowDatas.add("数据" + j);
            }
            mTableData.add(mRowDatas);
        }
    }
}