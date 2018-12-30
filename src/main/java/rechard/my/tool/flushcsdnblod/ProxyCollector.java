package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public abstract class ProxyCollector implements Runnable {
    public  Logger logger= LoggerFactory.getLogger(this.getClass());
    public void run(){
        logger.info("start collect proxy");
        collect();
        //Collection<Proxy> proxies=collect();
        logger.info("end start collect proxy");
        //ProxyFilterCollector.put(proxies);
    }
    public abstract Collection<Proxy> collect();
}
