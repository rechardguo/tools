package rechard.my.point.javainvokejs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.ClassPathResource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * java调用js,背景是打算给response加个base64加密的签名,然后在js端校验
 *
 * 调用后有个waring
 * Warning: Nashorn engine is planned to be removed from a future JDK release
 *
 */
public class JavaInvokejs {

    public static void main(String[] args) throws Exception {
        JavaInvokejs javaInvokejs = new JavaInvokejs();
        System.out.println("=======BASE64======");
        javaInvokejs.testBase64();
        System.out.println("=======MD5======");
        javaInvokejs.testMD5();
        System.out.println("=======MD5withSalt======");
        javaInvokejs.testMD5WithSalt();

    }

    public void  testMD5() throws Exception {
        ObjectMapper om = getObjectMapper();
        Foo foo = new Foo();
        foo.setAge(13);
        foo.setName("test");
        System.out.println("original json:"+ om.writeValueAsString(foo));
        byte[] digest  = MessageDigest.getInstance("md5").digest(om.writeValueAsBytes(foo));
        String md5Str = new BigInteger(1, digest).toString(16);
        foo.setX(md5Str);
        System.out.println("add signature json:"+ om.writeValueAsString(foo));
        jsVerifyXMD5(om.writeValueAsString(foo));
    }

    /**
     * md5加密加盐
     * 区别于testMD5,这里直接使用了工具类DigestUtils
     * @throws Exception
     */
    public void  testMD5WithSalt() throws Exception {
        ObjectMapper om = getObjectMapper();
        Foo foo = new Foo();
        foo.setAge(13);
        foo.setName("test");
        System.out.println("original json:"+ om.writeValueAsString(foo));
        byte[] digest  = MessageDigest.getInstance("md5").digest(om.writeValueAsBytes(foo));
        String md5Str = new BigInteger(1, digest).toString(16);
        String salt = String.valueOf(md5Str.charAt(1) + md5Str.charAt(md5Str.length()-1));
        System.out.println(salt);
        foo.setX(md5Str+salt);
        System.out.println("add signature json:"+ om.writeValueAsString(foo));
        jsVerifyXMD5Salt(om.writeValueAsString(foo));
    }

    public void testBase64() throws Exception {
        ObjectMapper om = getObjectMapper();
        Foo foo = new Foo();
        foo.setAge(13);
        foo.setName("test");
        System.out.println("original json:"+ om.writeValueAsString(foo));
        byte[] bytes = om.writeValueAsBytes(foo);
        String x = new String(Base64.getEncoder().encode(bytes));
        foo.setX(x);
        System.out.println("add signature json:"+ om.writeValueAsString(foo));
        jsVerifyXBase64(om.writeValueAsString(foo));
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        om.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        return om;
    }

    public void jsVerifyXBase64(String JsonObj) throws ScriptException, NoSuchMethodException, IOException {
        ScriptEngine scriptEngine = getScriptEngine();
        scriptEngine.eval(readJSFile("base64.js"));

        scriptEngine.eval("function check(jsonObj) {" +
                "        var obj = JSON.parse(jsonObj);" +
                "        var x=obj.x;" +
                "        delete obj.x;" +
                "        obj=JSON.stringify(obj);"+
                "        var x2=Base64.encode(obj);" +
                "       return x2==x;" +
                "}");
        //ScriptEngine强转为Invocable
        Invocable inv = (Invocable) scriptEngine;
        boolean result =  (Boolean) inv.invokeFunction("check", JsonObj);
        System.out.println("js verify result:"+result);
    }

    private static ScriptEngine getScriptEngine() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        return scriptEngine;
    }

    public void jsVerifyXMD5(String JsonObj) throws ScriptException, NoSuchMethodException, IOException {
        ScriptEngine scriptEngine = getScriptEngine();
        scriptEngine.eval(readJSFile("md5.js"));
        scriptEngine.eval("function check(jsonObj) {" +
                "        var obj = JSON.parse(jsonObj);" +
                "        var x=obj.x;" +
                "        delete obj.x;" +
                "        obj=JSON.stringify(obj);"+
                "        var x2 = md5(obj);" +
                "       return x2==x;" +
                "}");
        //ScriptEngine强转为Invocable
        Invocable inv = (Invocable) scriptEngine;
        boolean result =  (Boolean) inv.invokeFunction("check", JsonObj);
        System.out.println("js verify result:"+result);
    }

    /**
     * 由于scriptEngine.eval里没法console打印出来，所以可以通过html里来执行查看，
     * 见src/main/resources/js/jsverifymd5print.html
     * @param JsonObj
     * @throws ScriptException
     * @throws NoSuchMethodException
     * @throws IOException
     */
    public void jsVerifyXMD5Salt(String JsonObj) throws ScriptException, NoSuchMethodException, IOException {
        ScriptEngine scriptEngine = getScriptEngine();
        scriptEngine.eval(readJSFile("md5.js"));
        scriptEngine.eval("function check(jsonObj) {" +
                "        var obj = JSON.parse(jsonObj);" +
                "        var x=obj.x;" +
                "        delete obj.x;" +
                "        obj=JSON.stringify(obj);"+
                "        var x2 = md5(obj);" +
                "        var x2salt = x2.charCodeAt(1)+x2.charCodeAt(x2.length-1);"+
                "        var x2saltStr = x2salt+'';"+
                "        return x2==x.substring(0,x.length-x2saltStr.length) && x2saltStr==x.substring(x.length-x2saltStr.length,x.length);" +
                "}");
        Invocable inv = (Invocable) scriptEngine;
        boolean result =  (Boolean) inv.invokeFunction("check", JsonObj);
        System.out.println("js verify result:"+result);
    }

    private  String  readJSFile(String jsFile) throws IOException {
        ClassPathResource resource = new ClassPathResource("js/" + jsFile);
        StringBuffer script = new StringBuffer();
        // 获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
        InputStream inputStream = resource.getInputStream();
        BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
        String tempString = null;
        while ((tempString = bufferreader.readLine()) != null) {
            script.append(tempString).append("\n");
        }
        bufferreader.close();
        inputStream.close();
        return script.toString();
    }



    public class Foo {
        private String name;
        private int age;

        private String x;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }
}
