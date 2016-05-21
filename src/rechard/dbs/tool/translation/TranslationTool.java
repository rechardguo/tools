package rechard.dbs.tool.translation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * 写的一个按xlsx里配置的翻译来直接替换对应property或xml里的翻译
 * 当配置好xlsx后，修改45行的Sheet sheet1 = wb.getSheetAt(1);
 * 来读取到不同的sheet里的值并进行配置
 * 
 * @author rechard
 *
 */
public class TranslationTool {
	static String filename="";
	static String ROOT="D:\\code\\DBS_CB\\CORP_PSO_SRC";
	static Element xmlpsoElement=null;
	public static void main(String[]args){
		try {
			config(TranslationTool.class.getResource("").getPath()+"R12_Translationsv1.1.xlsx");
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
			System.out.println("您输入的excel格式不正确");
		}
		//重要点：这里按xlsx里的sheet来改，比如我定义的sheet1是property,sheet2是XML
		Sheet sheet1 = wb.getSheetAt(1);
		Row firstRow = null;
		for (Row row : sheet1) {
			//第一行是为了看方便
			if(row.getRowNum()==0){
				firstRow = row;
				for (Cell cell : row) {
					//  System.out.print(cell.getStringCellValue()+",");
				}
			}else{
				String fileName=null;
				String key=null;
				String value=null;
				for (Cell cell : row) {
					if(cell.getColumnIndex()==0)
						fileName=cell.getStringCellValue().trim();
					else if(cell.getColumnIndex()==1)
						key=cell.getStringCellValue().trim();
					else{
						value=cell.getStringCellValue().trim();
						String ctry = firstRow.getCell(cell.getColumnIndex()).getStringCellValue();
						if(ctry.equals("EN")){
							ctry="pso";
						}
						if(fileName==null) continue;
						if(fileName.endsWith("properties"))	{
							if(ctry.startsWith("zh_")){
								value=UnicodeUtil.gbEncoding(value);
							}
							writePropertyFile(fileName, key, value,  ctry);
						}
						if(fileName.endsWith("xml")){
							
							writeXmlFile(fileName, key, value, ctry);
						}
					}
				}
			}

		}
	}
		
	private static void writeXmlFile(String fileName, String key, String value, String ctry) 
			throws Exception {
		if(fileName!=null){
			if(key!=null && value!=null){
				XMLFile xmlFile = new XMLFile(fileName.replaceAll("pso.xml", ctry+".xml"));
				Element ele = xmlFile.findPutElement(key);
				if(ele==null) {
					Node definitionNode = xmlpsoElement.getParentNode();
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
