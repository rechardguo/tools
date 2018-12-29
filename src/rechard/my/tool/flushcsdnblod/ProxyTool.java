package rechard.my.tool.flushcsdnblod;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.ConnectException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProxyTool {
    static final Logger logger = LoggerFactory.getLogger(ProxyTool.class);
    public static boolean validateAndRecordInDB(Proxy proxy, String urlstr) throws Exception{
        logger.info("validate "+proxy);
        Collection<Proxy> proxies = ProxyPool.getProxys();
        if(proxies.contains(proxy)){
            logger.info("skip proxy "+proxy+"already collected!");
        }
        long start=System.currentTimeMillis();
        boolean validate=ProxyTool.validete(proxy,urlstr);
        long end=System.currentTimeMillis();
        if(validate) {
            long responseTime=end-start;
            ProxyDataSource.record(proxy.toString() + System.getProperty("line.separator"));
            logger.info("validate and get one proxy: "+ proxy );
        }
        return validate;
    }
    public static Collection<Proxy> validateAndRecordInDB(Collection<Proxy> collection, String urlstr) throws Exception{
        return collection.stream().parallel().filter(proxy -> {
            try {
                collection.remove(proxy);
                logger.info("validate "+proxy);
                long start=System.currentTimeMillis();
                boolean validate=ProxyTool.validete(proxy,urlstr);
                long end=System.currentTimeMillis();
                if(validate) {
                    long responseTime=end-start;
                    ProxyDataSource.record(proxy.toString() + System.getProperty("line.separator"));
                }
                logger.info("validate "+ proxy +" : "+validate);
                return validate;
            } catch (Exception e) {
                logger.info("validate "+ proxy +" error "+e.getMessage());
                return false;
            }
        }).collect(Collectors.toList());
    }

    public static boolean validete(Proxy proxy,String urlstr) throws Exception{
        CloseableHttpClient httpclient=null;
        try {
            /**
             *  请求参数配置
             *  connectionRequestTimeout:
             *                          从连接池中获取连接的超时时间，超过该时间未拿到可用连接，
             *                          会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
             *  connectTimeout:
             *                  连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
             *  socketTimeout:
             *                  服务器返回数据(response)的时间，超过该时间抛出read timeout
             */
            httpclient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build()).build();
            URL url = new URL(urlstr);
            HttpHost target = new HttpHost(url.getHost(), url.getDefaultPort(), url.getProtocol());
            HttpHost hostProxy = new HttpHost(proxy.getIp(),Integer.parseInt(proxy.getPort()));

            RequestConfig config = RequestConfig.custom().setProxy(hostProxy).build();
            HttpGet httpget = new HttpGet(url.getPath());
            httpget.setConfig(config);
            httpget.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpget.addHeader("accept-encoding", "gzip, deflate, br");
            httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            CloseableHttpResponse response = httpclient.execute(target, httpget);
            try {
                return  response.getStatusLine().getStatusCode()==200;
            } finally {
                response.close();
            }
        }catch(ConnectException e){
            logger.info("validate "+ proxy +" time out");
            return  false;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        } finally{
            httpclient.close();
        }
    }

    public static CloseableHttpClient createSSLClientDefault(){
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy(){
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }


    public static  String getContent(String url) throws Exception{
        CloseableHttpClient hp = createSSLClientDefault();
        HttpGet hg = new HttpGet(url);
        //设置请求头
        hg.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        hg.setHeader("Accept-Encoding", "gzip, deflate, br");
        hg.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        hg.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
        CloseableHttpResponse response = hp.execute(hg);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity,"utf-8");
        hp.close();
        return content;
    }
}
