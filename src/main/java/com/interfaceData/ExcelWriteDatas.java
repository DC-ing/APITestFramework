package com.interfaceData;

import java.util.Map;

public class ExcelWriteDatas {

    private Map<String, ExcelCellData> writeDatasMap;

    public ExcelWriteDatas(Map<String, ExcelCellData> writeDatasMap) {
        this.writeDatasMap = writeDatasMap;
    }

    public Map<String, ExcelCellData> getWriteDatasMap() {
        return writeDatasMap;
    }

    public void setWriteDatasMap(Map<String, ExcelCellData> writeDatasMap) {
        this.writeDatasMap = writeDatasMap;
    }

    @Override
    public String toString() {
        return "ExcelWriteDatas " + writeDatasMap;
    }
}
