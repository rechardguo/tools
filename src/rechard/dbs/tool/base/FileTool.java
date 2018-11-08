package rechard.dbs.tool.base;

import rechard.dbs.tool.CopyFile.CopyModifyFileTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileTool {
    public static String read(String filePath)throws Exception{
       StringBuffer stringBuffer=new StringBuffer();
        FileReader is;
        BufferedReader br;
            is = new FileReader(filePath);
            br = new BufferedReader(is);
            String line = null;
            while((line=br.readLine())!=null){
                stringBuffer.append(line);
            }
        return  stringBuffer.toString();
    }
}
