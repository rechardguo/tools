package rechard.dbs.tool.dao.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.util.Locale;

public class FreemarkerUtil {
	/**
	 * 通过文件名加载模版
	 * @param ftlName
	 */
	public static Template getTemplate(String ftlName) throws Exception{
		try {
			//通过Freemaker的Configuration读取相应的ftl
			Configuration cfg = Configuration.getDefaultConfiguration();											
			cfg.setEncoding(Locale.CHINA, "utf-8");
			cfg.setDirectoryForTemplateLoading(new File(Thread.currentThread().getContextClassLoader().getResource("Template").toURI()));
			Template temp = cfg.getTemplate(ftlName);												//在模板文件目录中找到名称为name的文件
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[]args){
		try {
			System.out.println(FreemarkerUtil.getTemplate("controllerTemplate.ftl"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
