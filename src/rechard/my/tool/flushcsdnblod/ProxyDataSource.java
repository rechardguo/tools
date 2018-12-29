package rechard.my.tool.flushcsdnblod;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class ProxyDataSource {
    static FileChannel fileChannel;
    static String file="D://ip_datasource.txt";
    static {
        Path path = FileSystems.getDefault().getPath("D://", "ip_datasource.txt");
        try {
             fileChannel = FileChannel.open(path,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
        }catch(Exception e){

        }
    }

    public static void record(String str){
        ByteBuffer bb=ByteBuffer.wrap(str.getBytes());
        try {
            fileChannel.write(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Collection<Proxy> read() throws Exception{
        Collection<Proxy> collection= new HashSet<>();
        FileInputStream inputstream = new FileInputStream(file);
        StringBuffer buffer = new StringBuffer();
        String line; // 用来保存每行读取的内容
        BufferedReader bufferreader = new BufferedReader(new InputStreamReader(
                inputstream));
        while ((line=bufferreader.readLine()) != null) { // 如果 line 为空说明读完了
            String[] strs=line.split(":");
            collection.add(new Proxy(strs[0],strs[1]));
        }
        inputstream.close();
        return collection;
    }
}

