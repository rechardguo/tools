package rechard.dbs.tool.dao.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class Freemarker {

	/**
	 * 打印到控制台(测试用)
	 *  @param ftlName
	 */
	public static void print(String relativeResPath,String ftlName, Map<String,Object> root) throws Exception{
		try {
			Template temp = getTemplate(ftlName,relativeResPath);		//通过Template可以将模板文件输出到相应的流
			temp.process(root, new PrintWriter(System.out));
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出到输出到文件
	 * @param  relativeResPath 相对于resources的路径
	 * @param ftlName   ftl文件名
	 * @param root		传入的map
	 * @param outFile	输出后的文件全部路径
	 * @param filePath	输出前的文件上部路径
	 */
	public static void printFile(String relativeResPath,String ftlName, Map<String,Object> root, String outFile, String filePath)
			throws Exception{
		try {
			File file = new File(filePath + outFile);
			if(!file.getParentFile().exists()){				//判断有没有父路径，就是判断文件整个路径是否存在
				file.getParentFile().mkdirs();				//不存在就全部创建
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			Template template = getTemplate(ftlName,relativeResPath);
			template.process(root, out);					//模版输出
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过文件名加载模版
	 * @param ftlName
	 */
	public static Template getTemplate(String ftlName,String relativeResPath) throws Exception{
		try {
			Configuration cfg = new Configuration();//通过Freemaker的Configuration读取相应的ftl
			cfg.setDirectoryForTemplateLoading(new File(Thread.currentThread().getContextClassLoader().getResource(relativeResPath).toURI()));
			Template temp = cfg.getTemplate(ftlName);//在模板文件目录中找到名称为name的文件
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
