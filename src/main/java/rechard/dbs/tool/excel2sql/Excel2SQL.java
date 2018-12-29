package rechard.dbs.tool.excel2sql;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel2SQL {

	static String sqlPreInsert = "INSERT INTO BILLORGCATEGORY (BILLORGCATEGORYKY,CATEGORYNAME,AFFILATE,LEVEL,PID,"
	                +"CATEGORYNAME2,DBSCORPCODE,4CHARSNAME,CORPACCTTYPE,POSBCORPCODE,3CHARSNAME,"
	                +"CORPSMSOTPIND,CORPACCTNO)VALUES";
	
	static String tempsql ="( (select t.ky from (select max(BILLORGCATEGORYKY)+1 from BILLORGCATEGORY)t ), "
	                +"{CATEGORYNAME},'DBSSG',2,0,{CATEGORYNAME2},{DBSCORPCODE},{4CHARSNAME},"
	                +"{CORPACCTTYPE},{POSBCORPCODE},{3CHARSNAME},{CORPSMSOTPIND},{CORPACCTNO})";
	
	
	
	public static void main(String[]args){
		InputStream stream;
		try {
			stream = new FileInputStream("D:/DOC/R15/BOlist.xlsx");
			Workbook wb = new XSSFWorkbook(stream);
			Sheet sheet1 = wb.getSheetAt(0);
			Row firstRow = null;
			StringBuffer sb = new StringBuffer();
			for (Row row : sheet1) {
				//第一行是为了看方便
				if(row.getRowNum()==0){
					firstRow = row;
					//for (Cell cell : row) 
						//System.out.print(cell.getStringCellValue()+",");
				}else{
					String sql = tempsql;
					sql = sql.replaceAll("\\{CATEGORYNAME\\}", "'"+getCellValue(row.getCell(2))+"'")
					   .replaceAll("\\{CATEGORYNAME2\\}", "'"+getCellValue(row.getCell(3))+"'")
					   .replaceAll("\\{DBSCORPCODE\\}", "'"+getCellValue(row.getCell(0))+"'")
					   .replaceAll("\\{4CHARSNAME\\}", "'"+getCellValue(row.getCell(1))+"'")
					   .replaceAll("\\{CORPACCTTYPE\\}", "'"+getCellValue(row.getCell(4))+"'")
					   .replaceAll("\\{3CHARSNAME\\}", "'"+getCellValue(row.getCell(7))+"'")
					   .replaceAll("\\{CORPSMSOTPIND\\}", "'"+getCellValue(row.getCell(8))+"'")
					   .replaceAll("\\{CORPACCTNO\\}", "'"+getCellValue(row.getCell(5))+"'")
					   .replaceAll("\\{POSBCORPCODE\\}", "'"+getCellValue(row.getCell(6))+"'");
					//System.out.println(sql+"");
					sb.append(sql+",");
				}
				
			}
			
			System.out.println(sqlPreInsert+sb.toString());
			if(wb!=null)
				wb.close();
			if(stream!=null)
				stream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static String getCellValue(Cell cell){
		return cell==null?"":cell.toString();
	}
}
