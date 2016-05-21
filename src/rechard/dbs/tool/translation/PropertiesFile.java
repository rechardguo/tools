package rechard.dbs.tool.translation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/**
 * 这里写propertiesFile而不用propeties类，
 * 由于propeties是继承hashtable,在把properties的值读入到hashtable后，
 * 会不按顺序来存，而是按hash值保存。
 * 这样导致修改值后调用propeties.store()，
 * 整个文件原来的行的顺序会打乱，变得很乱。
 * @author rechard
 *
 */
public class PropertiesFile extends LinkedHashMap {
	String fileName = null;
	public PropertiesFile(String fileName) throws IOException{
		this.fileName = fileName;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String line = null;
		int number = 0;
		while((line = reader.readLine())!=null){
			if("".equals(line.trim())){
				this.put("$"+number++, line);
			}else if(line.startsWith("#")){
				this.put("$"+number++, line);
			}else{
				//有可能文件里有key=的情况出现
				if(line.indexOf("=")!=-1){
					 String vals[] = line.split("=");
						this.put(vals[0].trim(), vals[1].trim());
				}
				this.put("$"+number++, line);  
			}
		}
		reader.close();
	}
	

	public void save(){
		Iterator it = entrySet().iterator();
		StringBuffer sb = new  StringBuffer();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if(pairs.getKey().toString().startsWith("$")){
				sb.append(pairs.getValue());
			}else{
				sb.append(pairs.getKey().toString()+"="+pairs.getValue());
			}
			sb.append("\r\n");
		}
		
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
