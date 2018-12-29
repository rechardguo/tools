package rechard.dbs.tool.translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * 使用java dom 来解析xml
 * @author rechard
 *
 */
public class XMLFile {
	String fileName = null;
	Document document = null; 
	Element rootElement = null;
	public XMLFile(String fileName) throws Exception{
		this.fileName = fileName;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		//DOM parser instance 
		DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
		//xml文件里有个dtd的文件存在，在加载的时候会去网络上下载，如果网络不好，就会报
		//java.net.ConnectException: Connection timed out: connect
		//这里用以下的代码先绕过
		builder.setEntityResolver(
				new EntityResolver(){
					public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
					{
						return new InputSource(new StringBufferInputStream(""));   
					}
				}
				);
		File f = new File(fileName);
		//这里要用UTF-8读取出来，类似“”会导致报错
		InputSource is=new InputSource(new InputStreamReader (new FileInputStream(f),"UTF-8"));
		document = builder.parse(is);
		//get root element 
		rootElement = document.getDocumentElement();
	}

	public Comment createComment(String data){
		return document.createComment(data);
	}
	//这个是针对struts 的tiles的xml定义，一般都有<put name="xxx" value="valxxx">,直接用put的name来取得
	public Element findPutElement(String putkey){
		NodeList nodeList = rootElement.getElementsByTagName("put"); 
		if(nodeList != null){ 
			for (int i = 0 ; i < nodeList.getLength(); i++) { 
				Element element = (Element)nodeList.item(i); 
				String name = element.getAttribute("name");
				if(name.equals(putkey)){
					return element;
				}
			} 
		}
		return null;
	}

	//这个是针对struts 的tiles的xml定义，一般都有<put name="xxx" value="valxxx">,直接用put的name来取得
	public List findAllElement(String value){
		List list = new ArrayList();
		NodeList nodeList = rootElement.getElementsByTagName("put"); 
		if(nodeList != null){ 
			for (int i = 0 ; i < nodeList.getLength(); i++) { 
				Element element = (Element)nodeList.item(i); 
				String val = element.getAttribute("value");
				if(val.equals(value)){
					list.add(element);
				}
			} 
		}
		return list;
	}


	public Element findDefenitionElement(String val){
		NodeList nodeList = rootElement.getElementsByTagName("definition"); 
		if(nodeList != null){ 
			for (int i = 0 ; i < nodeList.getLength(); i++) { 
				Element element = (Element)nodeList.item(i); 
				String value = element.getAttribute("name");
				if(value.equals(val)){
					return element;
				}
			} 
		}
		return null;
	}

	public void save()  throws Exception{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(document);
		//生成出来的xml会每个节点一行
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		//这里是加上 xml的doctype,里面都是一些DTD的定义
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_PUBLIC, document.getDoctype().getPublicId());    
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId()); 
		//PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
		//保存会去的时候也需要用UTF-8编码来保存
		OutputStreamWriter os = new OutputStreamWriter (new FileOutputStream(fileName),"UTF-8");
		StreamResult result = new StreamResult(os);
		//保存xml
		transformer.transform(source, result);
		System.out.println("XML文件"+fileName+"替换翻译成功!");
		os.flush();
		os.close();
	}



	public static void main(String[]args) throws Exception{
		XMLFile xmlFile = new XMLFile("D:/code/DBS_CB/CORP_PSO_SRC/payments-web/WebContent/WEB-INF/payments/paymentLists/pc/paymentcentre-tiles-definitions_pso.xml");
		List l = xmlFile.findAllElement("Release Batch");
		Iterator it = l.iterator();
		while(it.hasNext()){
			Element ele = (Element)it.next();
			XMLFile xmlFile2 = new XMLFile("D:/code/DBS_CB/CORP_PSO_SRC/payments-web/WebContent/WEB-INF/payments/paymentLists/pc/paymentcentre-tiles-definitions_pso_zh_CN.xml");
			xmlFile2.save(ele, "Release Batch222"); 
		}
		//ele.setAttribute("value", "$$$$");
		//xmlFile.save();
	}
	/**
	 * 在xml里的definition的name是不可能重复的，
	 * 而不同的definition里的put的key是可以重复的，所以要先由definition的name找到 definiton再在
	 * definiton的所有put里找到对应的value，如果没有就建立，如果有，就覆盖
	 * @param putvalueParentDefinition
	 * @param putvalue
	 */
	public void save(Element ele, String putvalue) throws Exception{
		// TODO Auto-generated method stub
		String putvalueParentDefinition = ele.getParentNode().getAttributes().getNamedItem("name").getNodeValue();
		Element definitionNode = findDefenitionElement(putvalueParentDefinition);
		if(definitionNode==null){
			System.err.println("没有找到"+ele.getAttribute("name")+"的上级definition定义"+putvalueParentDefinition);
		}else{
			NodeList nodelists = definitionNode.getElementsByTagName("put"); 
			if(nodelists != null){ 
				for (int i = 0 ; i < nodelists.getLength(); i++) { 
					Element element = (Element)nodelists.item(i); 
					String key = element.getAttribute("name");
					if(key.equals(ele.getAttribute("name"))){
						element.setAttribute("value",putvalue);
						save();
						return;
					}
				} 
			}
			
		}
	}

}
