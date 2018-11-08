package rechard.dbs.tool.ux.i18n.generate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.json.JSONString;
import org.w3c.dom.Element;
import rechard.dbs.tool.base.FileTool;
import rechard.dbs.tool.translation.ListTool;
import rechard.dbs.tool.translation.PropertiesFile;
import rechard.dbs.tool.translation.XMLFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 写的一个按xlsx里配置的翻译来直接替换对应en.json里的翻译
 *
 * @author rechard
 *
 */
public class I18nFileGenerateTool {
    //args 0 en.json file
    //args 1 affiliate
    //args 3 excel file
    //args 4 enColIndex
    //args 5 affColIndex
    //args 6 genrate file path
    public static void main(String[]args)throws Exception{
        //1. 读取en.json  file 封装成json对象
        JSONObject jsonObject = new JSONObject(FileTool.read("D:\\DOC\\R18\\i18n\\en.json"));
        //2.从 excel文件里读取对应的英文和翻译
        Map m =readToMap("D:\\DOC\\R18\\i18n\\en.xlsx",1,5);
        //3.找到英文翻译，替换对应翻译
        replace(m ,jsonObject);
        //4.json生成 <affilate>.json
        System.out.println(jsonObject.toString());
       /* JSONObject obj = new JSONObject("{PERSON:{NAME:RECHARD,AGE:10}}");
       obj.get("PERSON");
        System.out.println(jsonObject);*/
    }


    private static  void  replace(Map m ,JSONObject jsonObject){
        Iterator<String> it=jsonObject.keys();
        while (it.hasNext()){
            String key=it.next();
            Object obj =jsonObject.get(key);
            if(obj instanceof JSONObject)
                replace(m,(JSONObject)obj);
            else if(obj instanceof String){
                if(m.containsKey(obj))
                    jsonObject.put(key,m.get(obj)) ;
            }


        }
    }

    //3.map key:英文翻译，中文翻译
    public static Map  readToMap(String filePath,int enColIndex,int affColIndex) throws Exception {
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        InputStream stream = new FileInputStream(filePath);
        Workbook wb = null;
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook(stream);
        } else if (fileType.equals("xlsx")) {
            wb = new XSSFWorkbook(stream);
        } else {
            System.out.println("您输入的excel格式不正确");
        }
        Map m = new HashMap();
        Sheet sheet1 = wb.getSheetAt(0);
        Row firstRow = null;
        for (Row row : sheet1) {
            //第一行是为了看方便
            if(row.getRowNum()==0){
                firstRow = row;
                for (Cell cell : row) {
                    //System.out.print(cell.getStringCellValue()+",");
                }
            }else{
                Cell enCell=row.getCell(enColIndex);
                Cell affCell=row.getCell(affColIndex);
                String enTran=enCell.getStringCellValue().trim();
                String affTran=affCell.getStringCellValue().trim();
                m.put(enTran, affTran);
            }
        }
        if(wb!=null)
            wb.close();
        if(stream!=null)
            stream.close();
        return m;
    }



}
