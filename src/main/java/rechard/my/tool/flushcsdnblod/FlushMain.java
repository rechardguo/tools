package rechard.my.tool.flushcsdnblod;

public class FlushMain {
    //收集代理
    private void collectProxy(){
        ProxyCollectors.addCollector(new ProxyxicidailiExtractor());
        ProxyCollectors.addCollector(new ProxykuaidailiExtractor());
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
                "https://blog.csdn.net/guo_xl/article/details/83927944"});
    }


}
