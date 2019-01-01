package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ProxyDataSource {
    static FileChannel fileChannel;
    static String file=Config.getStringProperty(Config.PROXY_PERSIST_FILE);
    static Lock lock=new ReentrantLock();
    static Logger logger= LoggerFactory.getLogger(ProxyDataSource.class);
    static {
        try {
            openFile();
        }catch(Exception e){
            logger.error("error in open validate proxy file ,"+e.getMessage());
        }
    }

    private static void openFile() throws Exception{
        //Path path = FileSystems.getDefault().getPath("D://", "ip_datasource.txt");
        RandomAccessFile raf=new RandomAccessFile(file,"rwd");
        fileChannel = raf.getChannel();
        /*fileChannel = FileChannel.open(path,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);*/
    }

    public static  void record(String str){
        ByteBuffer bb=ByteBuffer.wrap(str.getBytes());
        try {
            // lock.lock();
            fileChannel.write(bb);
        } catch (IOException e) {
            logger.error("error in save proxy to file ,"+ e.getMessage());
            // e.printStackTrace();
        }/*finally {
            lock.unlock();
        }*/
    }

    public static void updateLatest(Collection<Proxy> collection) throws Exception {
        String str=collection.stream().map(p->{return
                                p.getIp()+":"
                                +p.getPort()+":"
                                +p.getInvalidateCount();})
                .collect(Collectors.joining(System.lineSeparator()));
        fileChannel.truncate(0);
        record(str);
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
            collection.add(new Proxy(strs[0],strs[1],Long.parseLong(strs[2])));
        }
        inputstream.close();
        return collection;
    }
}

