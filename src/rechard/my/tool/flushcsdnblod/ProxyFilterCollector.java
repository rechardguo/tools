package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.*;

public class ProxyFilterCollector {

    private static final String URL="http://www.baidu.com/";
    Logger logger = LoggerFactory.getLogger(this.getClass());

    //收集到的proxy放到这个队列里
    public static volatile LinkedBlockingQueue<Proxy> queue=new LinkedBlockingQueue<>();

    public static void put(Collection<Proxy> proxies){
          queue.addAll(proxies);
    }

    public static void startValidate(){
        ExecutorService es=Executors.newFixedThreadPool(50);
        new Thread(()->{
            for(;;) {
                try {
                    Proxy p = queue.take();
                    es.submit(()->{
                        try {
                            ProxyTool.validateAndRecordInDB(p,URL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
