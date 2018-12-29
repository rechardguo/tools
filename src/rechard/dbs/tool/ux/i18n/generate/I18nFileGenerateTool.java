package rechard.dbs.tool.ux.i18n.generate;

import com.sun.deploy.net.proxy.ProxyType;
import org.apache.commons.cli.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.json.JSONString;
import org.w3c.dom.Element;
import rechard.dbs.tool.base.FileTool;
import rechard.dbs.tool.translation.ListTool;
import rechard.dbs.tool.translation.PropertiesFile;
import rechard.dbs.tool.translation.XMLFile;

import java.io.*;
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
    public static void start(String[] args) {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption(Option.builder("e")
                .longOpt("enFile")
                .hasArg()
                .desc("Set en.json file path.")
                .build());
        options.addOption(Option.builder("t")
                .longOpt("translationFile")
                .hasArg()
                .desc("Set Translation xlx file.")
                .build());
        options.addOption(Option.builder("r")
                .longOpt("rowIndexOfEnglish")
                .hasArg()
                .desc("Set row index in Translation xlx sheet.")
                .build());
        options.addOption(Option.builder("a")
                .longOpt("rowIndexOfTargetAffliate")
                .hasArg()
                .desc("Set row index of target Affiliate Translation in  Translation xlx file.")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg()
                .desc("Set output file.")
                .build());

        if (args == null || args.length == 0) {
            formatter.printHelp("parse error:", options);
            return;
        }

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (!line.hasOption("e")||!line.hasOption("t")||!line.hasOption("r")||!line.hasOption("a")||!line.hasOption("o")) {
                formatter.printHelp("i18n <options>", options);
                return;
            }else{
                generate(line.getOptionValue("e"),line.getOptionValue("t"),Integer.parseInt(line.getOptionValue("r")),
                        Integer.parseInt(line.getOptionValue("a")),line.getOptionValue("o"));
            }
        } catch (Exception e) {
            formatter.printHelp("Unrecognized option", options);
        }
    }


    private static  void generate(String enFile,String translateFile,int enColIndex,int affColIndex,String outFile) throws Exception{
        //1. 读取en.json  file 封装成json对象
        JSONObject jsonObject = new JSONObject(FileTool.read(enFile));//D:\DOC\R18\i18n\en.json
        //2.从 excel文件里读取对应的英文和翻译
        //"D:\\DOC\\R18\\i18n\\en.xlsx" 1 5
        Map m =readToMap(translateFile,enColIndex,affColIndex);
        //3.找到英文翻译，替换对应翻译
        replace(m ,jsonObject);
        //4.json生成 <affilate>.json
        JSONObject oldTranslationJsonObj = new JSONObject(FileTool.read(outFile));
        replace2(m,jsonObject,oldTranslationJsonObj);
        //System.out.println(oldTranslationJsonObj.toString());
        createFile(outFile,oldTranslationJsonObj);
    }

    public static void createFile(String path, JSONObject jsonObj) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(jsonObj.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static void main(String[]args)throws Exception{
        start(args);
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

    /**
     * 以enjsonObject 例找到m里的的英文翻译，如果有，则m里的value为需要翻译的国家翻译
     * 将这个翻译替换或加入到targetjsonObject
     * @param m key [english  translation]: value [target affiliate translation]
     * @param enjsonObject
     * @param targetjsonObject
     */
    private static  void  replace2(Map m ,JSONObject enjsonObject,JSONObject targetjsonObject){
        Iterator<String> it=enjsonObject.keys();
        while (it.hasNext()){
            String key=it.next();
            Object obj =enjsonObject.get(key);
            if(obj instanceof JSONObject) {
                if(!targetjsonObject.has(key))
                    targetjsonObject.put(key,new JSONObject());
                Object obj2 =targetjsonObject.get(key);
                replace2(m, (JSONObject) obj, (JSONObject) obj2);
            }else if(obj instanceof String){
                if(m.containsKey(obj)) {
                    targetjsonObject.put(key, m.get(obj));
                }
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
