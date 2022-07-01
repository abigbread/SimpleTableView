package com.abigbread.simpletableview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.abigbread.SimpleTableView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ArrayList<String>> mTableData;
    private ArrayList<ArrayList<String>> mTableData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        LinearLayout container = findViewById(R.id.container);

        SimpleTableView tableView = new SimpleTableView.Builder(this, container, mTableData)
                .useDefaultOptions()
                .create();
        //更新数据
        tableView.setData(mTableData2);

    }

    private void initData() {
        //构造假数据
        mTableData = new ArrayList<>();
        ArrayList<String> mFirstData = new ArrayList<>();
        mFirstData.add("标题");
        for (int i = 0; i < 10; i++) {
            mFirstData.add("标题" + i);
        }
        mTableData.add(mFirstData);
        for (int i = 0; i < 20; i++) {
            ArrayList<String> mRowData = new ArrayList<>();
            mRowData.add("标题" + i);
            for (int j = 0; j < 10; j++) {
                mRowData.add("数据" + j);
            }
            mTableData.add(mRowData);
        }

        //构造假数据2
        mTableData2 = new ArrayList<>();
        ArrayList<String> mFirstData2 = new ArrayList<>();
        mFirstData2.add("标题-");
        for (int i = 0; i < 10; i++) {
            mFirstData2.add("标题-" + i);
        }
        mTableData2.add(mFirstData2);
        for (int i = 0; i < 20; i++) {
            ArrayList<String> mRowData = new ArrayList<>();
            mRowData.add("标题-" + i);
            for (int j = 0; j < 10; j++) {
                mRowData.add("数据-" + j);
            }
            mTableData2.add(mRowData);
        }


    }
}