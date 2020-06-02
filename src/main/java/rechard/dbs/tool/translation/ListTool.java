package rechard.dbs.tool.translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ListTool {
	static String PROPERTY_ROOT=AdvanceTranslationTool.ROOT+"/corpbanking-config";
	static String XML_ROOT=AdvanceTranslationTool.ROOT+"/payments-web/WebContent/WEB-INF";
	private static final int DEFAULT_BUF_SIZE = 10240;

	private static int searchCount=0;

	//查询字符从普通文件
	void searchStrInXml(InputStream is, String fileName,String searchStr, int buf) throws IOException {
		searchStrInInputStream(fileName, searchStr, buf, is);
	}

	void searchStrInTxt(String file, String searchStr, int buf) throws IOException {
		searchStrInTxt(new FileInputStream(new File(file)),file, searchStr, buf);
	}

	//查询字符从普通文件
	void searchStrInTxt(InputStream is, String fileName,String searchStr, int buf) throws IOException {
		searchStrInInputStream(fileName, searchStr, buf, is);
	}
	//查询字符从指定InputStream
	private void searchStrInInputStream(String fileName, String searchStr, int buf, InputStream is) throws IOException{
		searchStrInBufferedReader(fileName, searchStr, buf, new BufferedReader(new InputStreamReader(is)));
	}
	private void searchStrInBufferedReader(String fileName, String searchStr, int buf, BufferedReader r) throws IOException {
		char[] blocks = new char[buf];
		int length = searchStr.length();
		if (buf < length) {
			throw new IOException("读取大小不能小于搜索字符串的长度");
		}
		String preStr = null;
		while (r.read(blocks) != -1) {
			String tempStr = new String(blocks);
			String tmpStr = (preStr + tempStr);
			if (null != tmpStr && tmpStr.indexOf(searchStr) > -1) {
				System.out.println(fileName + "---->" + searchStr);
				searchCount++;
			}
			if (tempStr.length() > length) {
				preStr = tempStr.substring(tempStr.length() - length);
			} else {
				preStr = tempStr;
			}
		}
	}
	void searchReplaceStrInFile(String dir, Map m, boolean recurse) {
		try {
			File d = new File(dir);
			if (!d.isDirectory()) {
				return;
			}
			File[] files = d.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (recurse && files[i].isDirectory()) {
					searchReplaceStrInFile(files[i].getAbsolutePath(), m, true);
				} else {
					String filename = files[i].getAbsolutePath();
					if (filename.endsWith(".properties")) {
						if(filename.indexOf("_pso")!=-1)
							searchReplaceStrInProperties(filename,m);
					}else if(filename.endsWith(".xml")) {
						if(filename.indexOf("_pso")!=-1)
							searchReplaceStrInXml(filename,m);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//查询字符从指定文件或目录
	void searchStrInFile(String dir, String searchStr, boolean recurse) {
		String fileName = null;
		try {
			File d = new File(dir);
			if (!d.isDirectory()) {
				return;
			}
			File[] files = d.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (recurse && files[i].isDirectory()) {
					searchStrInFile(files[i].getAbsolutePath(), searchStr, true);
				} else {
					String filename = files[i].getAbsolutePath();
					if (filename.endsWith(".properties")) {
						if(filename.indexOf("_pso")!=-1)
							//searchStrInTxt(filename,searchStr, DEFAULT_BUF_SIZE);
							searchStrInProperties(filename,searchStr);
					}else if(filename.endsWith(".xml")) {
						if(filename.indexOf("_pso")!=-1)
							searchStrInXml(filename,searchStr);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String searchReplaceStrInProperties(String filename,Map m) throws Exception{
		PropertiesFile op = new PropertiesFile(filename);
		String searchStr = m.get("EN").toString();
		if(searchStr==null || searchStr.equals(""))
			return null;
		Map.Entry pair = op.findAllValue(searchStr);
		if(pair!=null){
			System.out.println(filename + "---->" + searchStr);
			System.out.println(pair.getKey()+":"+pair.getValue());
			Set keySet = m.keySet();
			Iterator it = keySet.iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				String ctry = key;
				if(ctry.equals("EN"))
					ctry="pso";
				
				PropertiesFile pf=null;
				try {
					pf = new PropertiesFile(filename.replaceAll("pso.properties", ctry+".properties"));
				} catch (FileNotFoundException e) {
				}
				
				PropertiesFile pf2=null;
				try {
					if(!ctry.equals("pso")){
					 pf2 = new PropertiesFile(filename.replaceAll("pso.properties", "pso_"+ctry+".properties"));
					}
				} catch (FileNotFoundException e) {
				}
				
				 String value = (String)m.get(key);
				if(ctry.startsWith("zh_")){
					value=UnicodeUtil.gbEncoding(value);
				}
				
				if(pf2!=null){
					pf2.put(pair.getKey(),value);
					pf2.save();
				}else if(pf!=null){
					pf.put(pair.getKey(),value);
					pf.save();
				}
			}
			return filename;
		}
		return null;
	}

	private void searchReplaceStrInXml(String filename, Map m) throws Exception {
		XMLFile xmlFile = new XMLFile(filename);
		String searchStr = m.get("EN").toString();
		if(searchStr==null || searchStr.equals(""))
			return ;
		List l = xmlFile.findAllElement(searchStr);
		if(!l.isEmpty()){
			System.out.println(filename + "---->" + searchStr);
			Iterator it = l.iterator();
			while(it.hasNext()){
				Element ele = (Element)it.next();
				System.out.println(ele.getAttribute("name")+":"+ele.getAttribute("value"));
				Set keySet = m.keySet();
				Iterator mapit = keySet.iterator();
				while(mapit.hasNext()){
					String key = (String)mapit.next();
					String ctry = key;
					if(ctry.equals("EN"))
						ctry="pso";					
					
					XMLFile otheraffxmlFile = null;
					try {
						otheraffxmlFile = new XMLFile(filename.replaceAll("pso.xml", ctry+".xml"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					XMLFile otheraffxmlFile2 = null;
					try {
						if(!ctry.equals("pso")){
							otheraffxmlFile2 = new XMLFile(filename.replaceAll("pso.xml", "pso_"+ctry+".xml"));
						}
					} catch (Exception e) {
					}
					
					String putvalue = (String)m.get(key);
					if(otheraffxmlFile2!=null){
						otheraffxmlFile2.save(ele,putvalue);
					}else if(otheraffxmlFile !=null){
						otheraffxmlFile.save(ele,putvalue);
					}
				}
			}			
		}
		
	}



	private String searchStrInProperties(String filename, String searchStr) throws Exception{
		PropertiesFile op = new PropertiesFile(filename);
		Map.Entry pair = op.findAllValue(searchStr);
		if(pair!=null){
			System.out.println(filename + "---->" + searchStr);
			System.out.println(pair.getKey()+":"+pair.getValue());
			return filename;
		}
		return null;
	}

	private String searchStrInXml(String filename, String searchStr) throws Exception {
		XMLFile xmlFile = new XMLFile(filename);
		List l = xmlFile.findAllElement(searchStr);
		
		if(!l.isEmpty()){
			System.out.println(filename + "---->" + searchStr);
			Iterator it = l.iterator();
			while(it.hasNext()){
				Element ele = (Element)it.next();
				System.out.println(ele.getAttribute("name")+":"+ele.getAttribute("value"));
			}
			return filename;
		}
		return null;
	}

	public static void main(String[] args) {

		FileReader is;
		BufferedReader br;
		try {

			is = new FileReader(ListTool.class.getResource("").getPath()+"/translation.txt");
			br = new BufferedReader(is);
			String line = null;
			ListTool lt = new ListTool();
			while((line=br.readLine())!=null){
				lt.searchStrInFile(PROPERTY_ROOT,line,true);
				lt.searchStrInFile(XML_ROOT,line,true);
			}

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
