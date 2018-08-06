package api.utils;

import api.beans.TestDataBean;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExcelUtil {
    public static <T> List<T> readTestDataFromExcel(Class<T> clazz, String excelPath, String sheet) {
        List<T> dataList = new ArrayList<>();
        List<T> temp = new ArrayList<>();
        String[] sheetArr = sheet.split(";");
        for (String excel : excelPath.split(";")) {
            File file = Paths.get(System.getProperty("user.dir"), excel).toFile();
            temp.clear();
            if (sheetArr.length != 0 && sheetArr[0] != null) {
                for (String sheetName : sheetArr) {
                    temp.addAll(readExcelSheet(clazz, file.getAbsolutePath(), sheetName));
                }
            } else {
                temp.addAll(readExcelSheet(clazz, file.getAbsolutePath()));
            }
            dataList.addAll(temp);
        }
        return dataList;
    }

    private static <T> Collection<T> readExcelSheet(Class<T> clazz, String filePath) {
        return readExcelSheet(clazz, filePath, "");
    }

    private static <T> Collection<T> readExcelSheet(Class<T> clazz, String filePath, String sheetName) {
        if (StringUtil.isEmptyString(filePath))
            return null;
        InputStream inputStream;
        Workbook workbook;
        List<T> data = new ArrayList<>();
        try {
            inputStream = new FileInputStream(filePath);
            if (filePath.endsWith(".xls"))    workbook = new HSSFWorkbook(inputStream);
            else                              workbook = new XSSFWorkbook(inputStream);
            inputStream.close();

            if (StringUtil.isEmptyString(sheetName)) {
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    sheetName += workbook.getSheetName(i) + ";";
                }
            }
            data = injectData(clazz, workbook, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static <T> List<T> injectData(Class<T> clazz, Workbook workbook, String sheetNames) {
        List<T> excelData = new ArrayList<>();
        for (String sheetName : sheetNames.split(";")) {
            if (StringUtil.isEmptyString(sheetName))
                continue;

            Sheet sheet = workbook.getSheet(sheetName);
            Row firstRow = sheet.getRow(0);
            if (null == firstRow)
                return excelData;
            List<Object> header = readRow(firstRow);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                try {
                    Row row = sheet.getRow(i);
                    if (null == row)
                        continue;
                    T t = clazz.newInstance();
                    List<Object> data = readRow(row);
                    while (data.size() < header.size()) {
                        data.add("");
                    }
                    setValue(t, header, data);
                    excelData.add(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return excelData;
    }

    private static void setValue(Object ob, List<Object> header, List<Object> data) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = ob.getClass().getMethods();
        List<String> title = new ArrayList<>();
        String name;
        Object value;
        for (Object o : header) {
            title.add(o.toString().toLowerCase());
        }
        for (Method m : methods) {
            name = m.getName().toLowerCase();
            if (name.startsWith("set")) {
                int index = title.indexOf(name.substring(3));
                value = data.get(index);

                Class<?> param = m.getParameterTypes()[0];
                if (String.class.equals(param)) {
                    m.invoke(ob, value);
                } else if (Integer.class.equals(param) || int.class.equals(param)) {
                    if(StringUtil.isEmptyString(value.toString())){
                        value = 0;
                    }
                    m.invoke(ob, new BigDecimal(value.toString()).intValue());
                } else if (Long.class.equals(param) || long.class.equals(param)) {
                    if(StringUtil.isEmptyString(value.toString())){
                        value = 0;
                    }
                    m.invoke(ob, new BigDecimal(value.toString()).longValue());
                } else if (Short.class.equals(param) || short.class.equals(param)) {
                    if(StringUtil.isEmptyString(value.toString())){
                        value = 0;
                    }
                    m.invoke(ob, new BigDecimal(value.toString()).shortValue());
                } else if (Boolean.class.equals(param) || boolean.class.equals(param)) {
                    m.invoke(ob, value.toString().toLowerCase().equals("y"));
                } else {
                    m.invoke(ob, value);
                }
            }
        }
    }

    private static List<Object> readRow(Row row) {
        List<Object> cells = new ArrayList<>();
        if (null != row) {
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cellData = row.getCell(i);
                cells.add(getValue(cellData));
            }
        }
        return cells;
    }

    private static String getValue(Cell cellData) {
        if (null == cellData) {
            return "";
        } else if (cellData.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(cellData.getBooleanCellValue());
        } else if (cellData.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf(cellData.getNumericCellValue());
        } else {
            return String.valueOf(cellData.getStringCellValue());
        }
    }


}
