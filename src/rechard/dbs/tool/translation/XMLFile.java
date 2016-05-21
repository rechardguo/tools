package rechard.dbs.tool.translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;

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
 * ʹ��java dom ������xml
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
		//xml�ļ����и�dtd���ļ����ڣ��ڼ��ص�ʱ���ȥ���������أ�������粻�ã��ͻᱨ
		//java.net.ConnectException: Connection timed out: connect
		//���������µĴ������ƹ�
		builder.setEntityResolver(
                new EntityResolver(){
                   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
                   {
                       return new InputSource(new StringBufferInputStream(""));   
                       }
                }
        );
		File f = new File(fileName);
		//����Ҫ��UTF-8��ȡ���������ơ����ᵼ�±���
		InputSource is=new InputSource(new InputStreamReader (new FileInputStream(f),"UTF-8"));
		document = builder.parse(is);
		//get root element 
		rootElement = document.getDocumentElement();
	}
	
	public Comment createComment(String data){
		return document.createComment(data);
	}
	//��������struts ��tiles��xml���壬һ�㶼��<put name="xxx" value="valxxx">,ֱ����put��name��ȡ��
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
            //���ɳ�����xml��ÿ���ڵ�һ��
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //�����Ǽ��� xml��doctype,���涼��һЩDTD�Ķ���
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_PUBLIC, document.getDoctype().getPublicId());    
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId()); 
            //PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            //�����ȥ��ʱ��Ҳ��Ҫ��UTF-8����������
            OutputStreamWriter os = new OutputStreamWriter (new FileOutputStream(fileName),"UTF-8");
            StreamResult result = new StreamResult(os);
            //����xml
            transformer.transform(source, result);
           // System.out.println("XML�ļ�"+fileName+"�滻����ɹ�!");
	}
	

	
	public static void main(String[]args) throws Exception{
		XMLFile xmlFile = new XMLFile("D:/code/DBS_CB/CORP_PSO_SRC/payments-web/WebContent/WEB-INF/payments/modules/default/payments-tiles-definitions_pso.xml");
		Element ele = xmlFile.findPutElement("IFSCodePre");
		ele.setAttribute("value", "$$$$");
		xmlFile.save();
	}

}
