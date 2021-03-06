package rechard.dbs.tool.translation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Element;


public class AdvanceTranslationTool {
	static String filename="";
	static String ROOT="D:\\code\\DBS_CB\\CORP_PSO_SRC";
	static Element xmlpsoElement=null;
	public static void main(String[]args){
		try {
			config("D:/workspace/tools/src/rechard/dbs/tool/translation/Translations.xlsx");
			//config(TranslationTool.class.getResource("").getPath()+"R12_Translationsv1.1.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void config(String filePath) throws Exception {
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("�������excel��ʽ����ȷ");
		}
		//��Ҫ�㣺���ﰴxlsx���sheet���ģ������Ҷ����sheet1��property,sheet2��XML
		Sheet sheet1 = wb.getSheetAt(0);
		Row firstRow = null;
		for (Row row : sheet1) {
			//��һ����Ϊ�˿�����
			if(row.getRowNum()==0){
				firstRow = row;
				for (Cell cell : row) {
					//  System.out.print(cell.getStringCellValue()+",");
				}
			}else{
				
				Map m = new HashMap();
				for (Cell cell : row) {
					String value=cell.getStringCellValue().trim();
					String ctry = firstRow.getCell(cell.getColumnIndex()).getStringCellValue();
					m.put(ctry, value);
				}
				if(m!=null){
					ListTool lt = new ListTool();					
					lt.searchReplaceStrInFile(ListTool.PROPERTY_ROOT,m,true);
					lt.searchReplaceStrInFile(ListTool.XML_ROOT,m,true);
				}
				/*if(fileName.endsWith("properties"))	{
					if(ctry.startsWith("zh_")){
						value=UnicodeUtil.gbEncoding(value);
					}
					writePropertyFile(fileName, key, value,  ctry);
				}
				if(fileName.endsWith("xml")){
					
					writeXmlFile(fileName, key, value, ctry);
				}*/
				
				
				}
			}
		if(wb!=null)
		wb.close();
		if(stream!=null)
		stream.close();
	}
		
	private static void writeXmlFile(String fileName, String key, String value, String ctry) 
			throws Exception {
		if(fileName!=null){
			if(key!=null && value!=null){
				XMLFile xmlFile = new XMLFile(fileName.replaceAll("pso.xml", ctry+".xml"));
				Element ele = xmlFile.findPutElement(key);
				if(ele==null) {
					//Node definitionNode = xmlpsoElement.getParentNode();
					System.err.println("no ["+key+"] in ["+fileName.replaceAll("pso.xml", ctry+".xml")+"] "
							+ "on definition ["+xmlpsoElement.getParentNode().getAttributes().getNamedItem("name")+"]");
					return ;
				}else{
				  if(ctry.equals("pso"))
					xmlpsoElement=ele;	
				}
				ele.setAttribute("value",value);
				xmlFile.save();
			}
		}
	}
	
	private static String convert(String value){
		value = value.replaceAll("\"", "&quote;");
		return value;
	}
	
	private static void writePropertyFile(String fileName, String key, String value,  String ctry) 
			throws IOException {
		if(fileName!=null){
			if(key!=null && value!=null && !"".equals(key) && !"".equals(value)){
				PropertiesFile op = new PropertiesFile(fileName.replaceAll("pso.properties", ctry+".properties"));
				op.put(key, value);
				op.save();
			}
		}
	}
	

}
