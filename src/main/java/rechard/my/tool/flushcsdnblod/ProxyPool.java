package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

public class ProxyPool {
    private ProxyPool(){}
    private static CopyOnWriteArraySet<Proxy> proxiesCache=null;

    private static final Logger logger= LoggerFactory.getLogger(ProxyPool.class);

    public static void update(Proxy proxy){
        proxiesCache.add(proxy);
    }

    public static void update(Collection<Proxy> collection){
        proxiesCache.clear();
        proxiesCache.addAll(collection);
    }

    public static  Collection<Proxy> getProxies(){
        return proxiesCache;
    }

    public static boolean contains(Proxy proxy){
        if(proxiesCache !=null){
            return proxiesCache.contains(proxy);
        }
        return false;
    }

    /**
     * 第一次启动会从文件里加载proxy 到proxySet(内存)里
     * 然后启动一个线程定期将proxySet(内存)里的数据写回到文件里，默认定期时间是60s
     * @throws Exception
     */
    public static void  start() throws Exception{
        if(proxiesCache ==null) {
            proxiesCache = new CopyOnWriteArraySet(ProxyDataSource.read());
            logger.info("first time start, load proxy datasource from file");
        }
        new Thread(()->{
            boolean update;
            for(;;) {
                try {
                    logger.info("schedule update proxy datasource");
                    ProxyDataSource.updateLatest(proxiesCache);
                    Thread.sleep(60000);
                } catch (Exception e) {
                    logger.error("schedule update proxy datasource exception:" + e.getMessage());
                }
            }
        },"ProxyPool").start();
    }

}
