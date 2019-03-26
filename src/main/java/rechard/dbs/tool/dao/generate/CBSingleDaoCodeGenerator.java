package rechard.dbs.tool.dao.generate;

import java.util.*;

public class CBSingleDaoCodeGenerator {
	static List<String[]> fieldList = new ArrayList<String[]>();   	
	
	/////////////////////////////配置项,手动添加这里-begin////////////////////
	String filePath = "D://autoGenerate//";									//代码生成存放路径
	String packageName = "com.s1.pso22.digidoc.dao";  								//包名
	String objectName = "PSO22INCOMETRANSFER";	   						//类名
	String tableName = "PSO22INCOMETRANSFER";	   						//表名
	static {
		//数据库键名，属性，类里对应的属性名
		fieldList.add(new String[]{"PSO22INCOMETRANSFERKY",Fields.PRIMARY.toString()});
		//属性集合:数据库名,类型,类里的field		
		fieldList.add(new String[]{"REFERENCE",Fields.STRING.toString(),"reference"});
		fieldList.add(new String[]{"AMOUNT",Fields.DOUBLE.toString(),"amount"});
		fieldList.add(new String[]{"CURRENCY",Fields.STRING.toString(),"ccy"});
		fieldList.add(new String[]{"PAYER",Fields.STRING.toString(),"peryer"});
		fieldList.add(new String[]{"PAYERBANK",Fields.STRING.toString(),"peryerbank"});
		fieldList.add(new String[]{"RECEIVER",Fields.STRING.toString(),"receiver"});
		fieldList.add(new String[]{"RECEIVERBANK",Fields.STRING.toString(),"receiverbank"});
		fieldList.add(new String[]{"STATUS",Fields.INTEGER.toString(),"status"});
		fieldList.add(new String[]{"APPROVALNUM",Fields.INTEGER.toString(),"approvalNum"});
		fieldList.add(new String[]{"APPROVEDNUM",Fields.INTEGER.toString(),"approvedNum"});
		//fieldList.add(new String[]{"ACCTCCY",Fields.STRING.toString(),"acctCcy"});
		//fieldList.add(new String[]{"INDICATOR",Fields.STRING.toString(),"indicator"});
		//fieldList.add(new String[]{"CORCUSTOMERKY",Fields.INTEGER.toString(),"corCustomerky"});
	}
	//////////////////////////////配置项-end////////////////////////////////
	
	public static void main(String[]args){
		try {
			new CBSingleDaoCodeGenerator().generateCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void generateCode() throws Exception{
		/* ============================================================================================= */
		Map<String,Object> root = new HashMap<String,Object>();		//创建数据模型
		root.put("fieldList", fieldList);
		root.put("packageName", packageName);						//包名
		root.put("objectName", objectName);							//类名
		String objectNameFirstUpper = objectName.substring(0,1).toUpperCase()+objectName.substring(1);
		root.put("objectNameFirstUpper", objectNameFirstUpper);	    //类名(首字符大写)
		root.put("objectNameLower", objectName.toLowerCase());		//类名(全小写)
		root.put("objectNameUpper", objectName.toUpperCase());		//类名(全大写)
		root.put("tableName", tableName);							//表前缀	
		root.put("nowDate", new Date());							//当前日期
		
		//开始生成代码
		/* ============================================================================================= */
		
		/*生成data*/
		Freemarker.printFile("Data.ftl", root, packageName.replaceAll(".", "/")+"/"+objectNameFirstUpper+".java", filePath);

		/*生成dao*/
		Freemarker.printFile("Dao.ftl", root, packageName.replaceAll(".", "/")+"/"+objectNameFirstUpper+"Dao.java", filePath);
		
		/*生成Criteria*/
		Freemarker.printFile("Criteria.ftl", root, packageName.replaceAll(".", "/")+"/"+objectNameFirstUpper+"Criteria.java", filePath);

		/*生成sql*/
		Freemarker.printFile("Sql.ftl", root, packageName.replaceAll(".", "/")+"/"+"ddl.sql", filePath);
		
		/*生成说明*/
		Freemarker.printFile("Note.ftl", root, packageName.replaceAll(".", "/")+"/note.txt", filePath);

	}
	
}
