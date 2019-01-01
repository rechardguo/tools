package rechard.my.tool.flushcsdnblod;
import java.net.URL;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FlushTool {
    static Logger logger= LoggerFactory.getLogger(FlushTool.class);
    static ExecutorService es = Executors.newFixedThreadPool(Config.getIntProperty(Config.FLUSH_THREAD_NUMBER));
    public static void startFlush(String[] blogUrls){
        new Thread(()->{
            int count=0;
            for(;;) {
                logger.info("start to flush :"+ (++count)+" time");
                try {
                    for (int i = 0; i < blogUrls.length; i++) {
                        flush(blogUrls[i]);
                    }
                    Thread.sleep(60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void flush(String articleUrl)throws Exception{
        logger.info("start flush articleUrl >>>>>");
        Collection<Proxy> list = ProxyPool.getProxies();
        for (Proxy proxy:list ) {
            //访问不成功-1,invalidatecount有可能减到负数
            if(Math.abs(proxy.getInvalidateCount())<Config.getIntProperty(Config.PRPXY_INVALIDATE_ACCESS_NUMBER)) {
                Future f = es.submit(new Worker(articleUrl, proxy));
            }
        }
    }

    /**
     *
     * @param articleUrl
     * @param proxy
     * @return status code
     * @throws Exception
     */
    public static int flush(String articleUrl,Proxy proxy) throws Exception {
       /* CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxy.getIp(),Integer.parseInt(proxy.getPort())),
                new UsernamePasswordCredentials("", ""));*/
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionTimeToLive(10, TimeUnit.SECONDS)
                /*.setDefaultCredentialsProvider(credsProvider)*/.build();
        try {
            URL url = new URL(articleUrl);
            HttpHost target = new HttpHost(url.getHost(), url.getDefaultPort(), url.getProtocol());
            HttpHost hostProxy = new HttpHost(proxy.getIp(),Integer.parseInt(proxy.getPort()));

            RequestConfig config = RequestConfig.custom().setProxy(hostProxy).build();
            HttpGet httpget = new HttpGet(url.getPath());
            httpget.setConfig(config);
           /* httpget.addHeader("Accept-Encoding", "gzip"); //使用gzip压缩传输数据让访问更快
            httpget.addHeader(":authority", "blog.csdn.net");
            httpget.addHeader(":method", "GET");
            httpget.addHeader(":path", "/guo_xl/article/details/85165333");
            httpget.addHeader(":scheme", "https");*/
            httpget.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpget.addHeader("accept-encoding", "gzip, deflate, br");
            httpget.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            CloseableHttpResponse response = httpclient.execute(target, httpget);
            try {
                //System.out.println("----------------------------------------");
               return  response.getStatusLine().getStatusCode();
                //System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    static class Worker implements Runnable {
        private final String url;
        private final Proxy proxy;
        public Worker(String url, Proxy proxy) {
            this.url = url;
            this.proxy = proxy;
        }
        public void run() {
            int code=0;
            try {
                logger.info("start to flush url : "+ url+" with proxy : "+proxy);
                code=FlushTool.flush(url,proxy);
                if(code==200)
                    logger.info("success to flush url : "+ url+" with proxy : "+proxy);
                else {
                    logger.error("error to flush url : " + url + " with proxy : " + proxy + ",response code " + code);
                }
            } catch (Exception e) {
                logger.error("fail to flush url: "+
                        url+" with proxy : "+proxy+",with exception "+e.getMessage()           );
            }
            //如果刷新不成功,更新不成功数
            if(code!=200) {
                proxy.increaseInvalidateCount();
            }else{
                proxy.decreaseInvalidateCount();
            }
            ProxyPool.update(proxy);
        }
    }
}
