package com.interfaceData;

/**
 * 此类存储"实际测试结果"单元格的方位信息，
 * 以便测试完毕后，根据记录的位置信息，写入到指定的位置中
 *
 * @version 1.0
 */

public class ExcelCellData {

    private int rowNum;
    private int cellNum;
    private String data;

    /**
     * 声明类时，一次性设置类的参数
     *
     * @param rowNum 行号
     * @param cellNum 列号
     * @param data 实际测试结果内容
     */
    public ExcelCellData(int rowNum, int cellNum, String data) {
        this.rowNum = rowNum;
        this.cellNum = cellNum;
        this.data = data;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString(){
        return data;
    }
}
