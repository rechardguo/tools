package rechard.dbs.tool.CopyFile;

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

public class CopyModifyFileTool {
	static String ROOT="D:\\code\\DBS_CB\\CORP_PSO_SRC\\";

	public static void main(String[] args) {
		
		FileReader is;
		BufferedReader br;
		try {
			
			is = new FileReader(CopyModifyFileTool.class.getResource("").getPath()+"/copy.txt");
			br = new BufferedReader(is);
			String line = null;
		
			while((line=br.readLine())!=null){
				
				//System.out.println(line);
				File f =new File(ROOT+line);
				String f2str = "D://CODE//"+line;
				new File(f2str).getParentFile().mkdirs();
				forChannel(f,new File(f2str));
				
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
	
	public static long forChannel(File f1,File f2) throws Exception{
        long time=new Date().getTime();
        int length=2097152;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        ByteBuffer b=null;
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                return new Date().getTime()-time;
            }
            if((inC.size()-inC.position())<length){
                length=(int)(inC.size()-inC.position());
            }else
                length=2097152;
            b=ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }

}
