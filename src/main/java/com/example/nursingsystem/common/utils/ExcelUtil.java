package com.example.nursingsystem.common.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel 工具类
 */
public class ExcelUtil {

    /**
     * 读取 Excel 文件
     */
    public static List<List<String>> readExcel(InputStream inputStream, int sheetIndex) throws IOException {
        List<List<String>> dataList = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValue(cell));
                }
                dataList.add(rowData);
            }
        }
        
        return dataList;
    }

    /**
     * 写入 Excel 文件
     */
    public static void writeExcel(OutputStream outputStream, List<List<String>> data, String sheetName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            
            int rowNum = 0;
            for (List<String> rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String cellData : rowData) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(cellData);
                }
            }
            
            workbook.write(outputStream);
        }
    }

    /**
     * 获取单元格的值
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
