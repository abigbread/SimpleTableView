package com.github.abigbread.bean;

import java.util.ArrayList;

/**
 * @author abigbread
 * 时间：2022/6/29 14:42
 * 包名：com.abigbread.simpletableview
 * 简述：<表格相关参数，adapter中传值需要>
 */

public class ParamsBean {

   /**
    * 是否锁定第一列
    */
   private boolean isLockFirstColumn;
   /**
    * 是否锁定第一行
    */
   private boolean isLockFirstRow;

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
    * 记录每列最大宽度
    */
   private ArrayList<Integer> columnMaxWidths = new ArrayList<Integer>();
   /**
    * 记录每行最大高度
    */
   private ArrayList<Integer> rowMaxHeights = new ArrayList<Integer>();

   public boolean isLockFirstColumn() {
      return isLockFirstColumn;
   }

   public void setLockFirstColumn(boolean lockFirstColumn) {
      isLockFirstColumn = lockFirstColumn;
   }

   public boolean isLockFirstRow() {
      return isLockFirstRow;
   }

   public void setLockFirstRow(boolean lockFirstRow) {
      isLockFirstRow = lockFirstRow;
   }

   public int getMaxColumnWidth() {
      return maxColumnWidth;
   }

   public void setMaxColumnWidth(int maxColumnWidth) {
      this.maxColumnWidth = maxColumnWidth;
   }

   public int getMinColumnWidth() {
      return minColumnWidth;
   }

   public void setMinColumnWidth(int minColumnWidth) {
      this.minColumnWidth = minColumnWidth;
   }

   public int getMaxRowHeight() {
      return maxRowHeight;
   }

   public void setMaxRowHeight(int maxRowHeight) {
      this.maxRowHeight = maxRowHeight;
   }

   public int getMinRowHeight() {
      return minRowHeight;
   }

   public void setMinRowHeight(int minRowHeight) {
      this.minRowHeight = minRowHeight;
   }

   public int getFirstRowBgColor() {
      return firstRowBgColor;
   }

   public void setFirstRowBgColor(int firstRowBgColor) {
      this.firstRowBgColor = firstRowBgColor;
   }

   public String getEmptyString() {
      return emptyString;
   }

   public void setEmptyString(String emptyString) {
      this.emptyString = emptyString;
   }

   public int getTextSize() {
      return textSize;
   }

   public void setTextSize(int textSize) {
      this.textSize = textSize;
   }

   public int getHeadTextColor() {
      return headTextColor;
   }

   public void setHeadTextColor(int headTextColor) {
      this.headTextColor = headTextColor;
   }

   public int getContentTextColor() {
      return contentTextColor;
   }

   public void setContentTextColor(int contentTextColor) {
      this.contentTextColor = contentTextColor;
   }

   public int getCellPadding() {
      return cellPadding;
   }

   public void setCellPadding(int cellPadding) {
      this.cellPadding = cellPadding;
   }

   public ArrayList<Integer> getColumnMaxWidths() {
      return columnMaxWidths;
   }

   public void setColumnMaxWidths(ArrayList<Integer> columnMaxWidths) {
      this.columnMaxWidths = columnMaxWidths;
   }

   public ArrayList<Integer> getRowMaxHeights() {
      return rowMaxHeights;
   }

   public void setRowMaxHeights(ArrayList<Integer> rowMaxHeights) {
      this.rowMaxHeights = rowMaxHeights;
   }

   public boolean isItemSelectedStatus() {
      return itemSelectedStatus;
   }

   public void setItemSelectedStatus(boolean itemSelectedStatus) {
      this.itemSelectedStatus = itemSelectedStatus;
   }
}
