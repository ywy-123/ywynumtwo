package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 三文鱼先生
 * @title
 * @description 导入工具类
 * @date 2022/8/17
 **/
public class ExcelImportSheet {

    /**
     * @description 根据文件后缀获取相应的Workbook对象
     * @author 三文鱼先生
     * @date 9:46 2022/8/17
     * @param in 用于构建Workbook对象的输入流
     * @param fileName 文件名称
     * @return org.apache.poi.ss.usermodel.Workbook
     **/
    public static Workbook getTypeFromExtends(InputStream in , String fileName) throws Exception {
        String[] str = fileName.split("\\.");
        //获取文件后缀
        String extend = str[1];
        if(extend.equals("xls")) {
            //2003版的excel
            return new HSSFWorkbook(in);
        } else if(extend.equals("xlsx")){
            //2007版的excel
            return new XSSFWorkbook(in);
        }else {
            throw new Exception("请检查文件类型是否正确。");
        }
    }

    /**
     * @description 将单个sheet里的数据获取到List<T>的泛型列表里面
     * @author 三文鱼先生
     * @date 9:47 2022/8/17
     * @param sheet 单个的工作表
     * @param cs 生成的对象类名
     * @param map 表头与对象属性映射
     * @return java.util.List<T>
     **/
    public static <T> List<T> getListFromExcel(Sheet sheet , Class cs , Map<String , String> map) throws Exception {
        T e;
        List<T> list = new ArrayList<>();
        //根据第一行获取表头对应的属性顺序
        List<String> paramsList = getMethodFromFirstRow(sheet , map);
        //根据类和属性顺序的List 获取属性对应的类型属性
        List<Class> typeClass = getParamsType(cs , paramsList);
        //遍历所有行 从第二行开始 首行是表头字段
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            //单元行
            Row row = sheet.getRow(i);
            //一行对应一个T，将对象强转为泛型
            e = (T) cs.newInstance();
            //遍历单元行的每一列 设置值给泛型e
            for (int j = 0; j < row.getLastCellNum(); j++) {
                //获取一个单元格
                Cell cell = row.getCell(j);
                //调用泛型对象的set方法设置单元格里的值 这也就是为什么我们要获取属性顺序以及其对应的类型
                cs.getMethod(getSetterMethodName(paramsList.get(j)) , typeClass.get(j))
                        .invoke(e , getValueFromType(cell , typeClass.get(j)));
            }
            list.add(e);
        }
        return list;
    }

    /**
     * @description 获取属性的setter方法
     * @author 三文鱼先生
     * @date 9:54 2022/8/17
     * @param param 属性
     * @return java.lang.String 返回一个setXxx
     **/
    public static String getSetterMethodName(String param) {
        char[] chars = param.toCharArray();
        //首字母大写
        if(Character.isLowerCase(chars[0])) {
            chars[0] -= 32;
        }
        //拼接set方法
        return "set" + new String(chars);
    }

    /**
     * @description 从第一行（表头）获取字段对应的属性的顺序
     * @author 三文鱼先生
     * @date 9:51 2022/8/17
     * @param sheet 工作表
     * @param map 表头字段与对象属性的映射
     * @return java.util.List<java.lang.String> 属性的集合
     **/
    public static List<String> getMethodFromFirstRow(Sheet sheet , Map<String,String> map) throws Exception {
        //获取表头
        Row row = sheet.getRow(sheet.getFirstRowNum());
        //获取到的属性列表
        List<String> paramsList = new ArrayList<>();
        //遍历表头
        for(int i = row.getFirstCellNum(); i  < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            //获取行字符串的值
            String str = cell.getStringCellValue();
            //键值对映射获取对应方法名称
            if(map.containsKey(str)) {
                //获取对应属性的set方法
                paramsList.add(map.get(str));
            } else {
                throw new Exception("请检查首行数据是否正确。");
            }
        }
        return  paramsList;
    }

    /**
     * @description  根据对象和属性顺序列表，返回对应顺序的参数类型List
     * @author 三文鱼先生
     * @date 9:55 2022/8/17
     * @param cs 对象类
     * @param paramsList 表头对应的属性顺序List
     * @return java.util.List<java.lang.Class>
     **/
    public static List<Class> getParamsType(Class cs , List<String> paramsList) {
        List<Class> typeClass = new ArrayList<>();
        //对象的所有属性
        Field[] fields = cs.getDeclaredFields();
        //临时的属性 - 类型映射
        Map<String , Class> map = new HashMap();
        //获取属性名称及类型
        for (Field field : fields) {
            map.put(field.getName(), field.getType());
        }
        //遍历属性List获取对应的类型List
        for (String s : paramsList) {
            typeClass.add(map.get(s));
        }
        return typeClass;
    }


    /**
     * @description 根据对应的Class获取将对应的值类型
     * @author 三文鱼先生
     * @date 9:59 2022/8/17
     * @param cell
     * @param cs
     * @return java.lang.Object
     **/
    public static Object getValueFromType(Cell cell , Class cs) {
        //字符串类型
        if (String.class.equals(cs)) {
            //设置对应的类型
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        } else if(boolean.class.equals(cs)){
            //boolean类型
            cell.setCellType(CellType.BOOLEAN);
            return cell.getBooleanCellValue();
        }else if (Date.class.equals(cs)) {
            //日期类型 此种数据并未测试
            return cell.getDateCellValue();
        } else if (int.class.equals(cs) || Integer.class.equals(cs)){
            //int类型
            cell.setCellType(CellType.NUMERIC);
            return (int)cell.getNumericCellValue();
        } else if(double.class.equals(cs) || Double.class.equals(cs)) {
            //double类型
            cell.setCellType(CellType.NUMERIC);
            return cell.getNumericCellValue();
        }
        //这里还可以填充其他类型
        else {
            //未知类型 默认为错误类型
            return cell.getErrorCellValue();
        }
    }
}

