## SimpleTableView

一个简单的自定义表格 View，可锁定首行和首列

## 引入方式

- 添加依赖

```java
implementation'com.github.abigbread:SimpleTableView:1.0.0'
```

- 项目根目录下的 build.gradle 中加入：

```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

## 使用方式

- 方式一：

```java
new SimpleTableView.Builder(this, container, mTableData)
        .useDefaultOptions()//默认表格样式
        .create();
```

- 方式二：

```java

new SimpleTableView.Builder(this, container, mTableData)
        .setLockFirstColumn(true) //是否锁定第一列
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
        .setContentTextColor(R.color.content_text_color)//单元格字体颜色;//空值替换值
        .create();
```

- 数据格式：

```java

ArrayList<ArrayList<String>> mTableData = new ArrayList<>();
ArrayList<String> mFirstData = new ArrayList<>();
//第一行
mFirstData.add("标题");
for (int i = 0; i < 10; i++) {
    mFirstData.add("标题" + i);
}
mTableData.add(mFirstData);
//其他行
for (int i = 0; i < 20; i++) {
    ArrayList<String> mRowData = new ArrayList<>();
    mRowData.add("标题" + i);
    for (int j = 0; j < 10; j++) {
        mRowData.add("数据" + j);
    }
    mTableData.add(mRowData);
}
```

## 感谢：

https://github.com/RmondJone/LockTableView


## License

```java
Copyright (c) 2022 abigbread

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
