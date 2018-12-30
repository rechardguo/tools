package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.*;

public class ProxyFilterCollector {

    private ProxyFilterCollector(){}
    private static final String URL=Config.getStringProperty(Config.PROXY_VALIDATION_URL);
    static Logger logger = LoggerFactory.getLogger(ProxyFilterCollector.class);

    //收集到的proxy放到这个队列里
    private static volatile LinkedBlockingQueue<Proxy> queue=new LinkedBlockingQueue<>();

    public static void put(Proxy proxy){
        queue.add(proxy);
    }

    public static void put(Collection<Proxy> proxies){
          queue.addAll(proxies);
    }

    /**
     * 通过从queue里获取到proxy,进行校验
     */
    public static void startValidate(){
        ExecutorService es=Executors.newFixedThreadPool(Config.getIntProperty(Config.PROXY_VALIDATION_THREAD_NUMBER));
        new Thread(()->{
            for(;;) {
                try {
                    Proxy p = queue.take();
                    es.submit(()->{
                        try {
                            ProxyTool.validateAndRecord(p,URL);
                        } catch (Exception e) {
                            logger.error("exception in validate proxy "+p.toString()+","+e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    logger.error("exception in take proxy from queue "+e.getMessage());
                }
            }
        },"ProxyFilterCollector#validate").start();
    }

}
