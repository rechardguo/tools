package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

public class ProxyPool {
   private static CopyOnWriteArraySet<Proxy> proxySet=new CopyOnWriteArraySet();

   private static final Logger logger= LoggerFactory.getLogger(ProxyPool.class);

   public static void update(Collection<Proxy> collection){
       proxySet.clear();
       proxySet.addAll(collection);
    }

    public static  Collection<Proxy> getProxys(){
       return proxySet;
    }

    public static void  scheduleUpdate(){
        new Thread(()->{
            try {
                logger.info("这是定期更新代理池代理");
                update(ProxyDataSource.read());
                Thread.sleep(10000);
            } catch (Exception e) {
                logger.error("更新代理池发生异常:"+e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

}
