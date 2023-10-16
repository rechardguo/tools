package rechard.my.point.javainvokejs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        new JavaInvokejs().test();
    }

    public void test() throws Exception {

        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        om.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        Foo foo = new Foo();
        foo.setAge(13);
        foo.setName("test");
        System.out.println("original json:"+ om.writeValueAsString(foo));
        byte[] bytes = om.writeValueAsBytes(foo);
        String x = new String(Base64.getEncoder().encode(bytes));
        foo.setX(x);
        System.out.println("add signature json:"+ om.writeValueAsString(foo));
        js(om.writeValueAsString(foo));

    }

    public void js(String JsonObj) throws ScriptException, NoSuchMethodException, IOException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        scriptEngine.eval(readJSFile());

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


    private  String  readJSFile() throws IOException {
        // 项目resources下新建js目录，将JS脚本放到js目录下
        ClassPathResource resource = new ClassPathResource("js/" + "base64.js");
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
