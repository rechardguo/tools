package rechard.my.tool.flushcsdnblod;

import rechard.my.tool.flushcsdnblod.extractor.ProxygaoniExtractor;
import rechard.my.tool.flushcsdnblod.extractor.ProxykuaidailiExtractor;
import rechard.my.tool.flushcsdnblod.extractor.ProxyxicidailiExtractor;

public class FlushMain {
    //收集代理
    private void collectProxy(){
        ProxyCollectors.addCollector(new ProxyxicidailiExtractor());
        ProxyCollectors.addCollector(new ProxykuaidailiExtractor());
        ProxyCollectors.addCollector(new ProxygaoniExtractor());
        ProxyCollectors.start();
    }

    public void start(String[] blogUrls) throws Exception{
        //1.启动代理收集线程
        collectProxy();
        //2.启动更新copyOnWriteSet线程
        ProxyPool.start();
        //3.启动刷新blog线程
        FlushTool.startFlush(blogUrls);
    }

    public static void main(String[] args) throws Exception{
        FlushMain fm=new FlushMain();
        fm.start(new String[]{
                "https://blog.csdn.net/guo_xl/article/details/78858959",
                "https://blog.csdn.net/guo_xl/article/details/79534887",
                "https://blog.csdn.net/guo_xl/article/details/83927983",
                "https://blog.csdn.net/guo_xl/article/details/79605578",
                "https://blog.csdn.net/guo_xl/article/details/85165333",
                "https://blog.csdn.net/guo_xl/article/details/83716342",
                "https://blog.csdn.net/guo_xl/article/details/83384755",
                "https://blog.csdn.net/guo_xl/article/details/84942404",
                "https://blog.csdn.net/guo_xl/article/details/84111835",
                "https://blog.csdn.net/guo_xl/article/details/83883746",
                "https://blog.csdn.net/guo_xl/article/details/83384755",
                "https://blog.csdn.net/guo_xl/article/details/82388273",
                "https://blog.csdn.net/guo_xl/article/details/82110659",
                "https://blog.csdn.net/guo_xl/article/details/79534734",
                "https://blog.csdn.net/guo_xl/article/details/85068719"
        });
    }


}
