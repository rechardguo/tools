package rechard.my.tool.flushcsdnblod;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

public class ProxyCollectors {

    private static Collection<ProxyCollector> collections=new ArrayList<>();

    public static void addCollector(ProxyCollector collector){
        collections.add(collector);
    }

    public static void start(){
        collections.forEach(
                (proxyCollector) -> {
                    try {
                        new Thread(()->{
                            proxyCollector.run();
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        ProxyFilterCollector.startValidate();
    }
}
