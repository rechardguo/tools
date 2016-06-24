package rechard.dbs.tool.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

import rechard.dbs.tool.translation.TranslationTool;

public class sqlTools {
	static String ROOT="D:\\code\\DBS_CB\\CORP_PSO_SRC\\";

	public static void main(String[] args) {
		
		FileReader is;
		BufferedReader br;
		try {
			
			is = new FileReader(sqlTools.class.getResource("").getPath()+"/sql.txt");
			br = new BufferedReader(is);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			
			String sql = sb.toString();
			//System.out.println(sql.substring(0,sql.indexOf("VALUES")));
			//System.out.println(sql.substring(sql.indexOf("VALUES")));
			String part1 = sql.substring(0,sql.indexOf("VALUES"));
			String part2 = sql.substring(sql.indexOf("VALUES"));
			String wh = part2.substring(0, part2.indexOf(")")+1);
			String paramstr = part2.substring(part2.indexOf(")")+1);
			System.out.println(paramstr);
			String [] params = paramstr.split(",", -1);
			paramstr="";
			for (int i = 0; i < params.length; i++) {
				if(params[i].indexOf("null:")!=-1){
					params[i]=null;
				}
				if(i<params.length-1)
				 paramstr+=params[i]+",";
			}
			System.out.println(paramstr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	

}
