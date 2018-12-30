package rechard.my.tool.flushcsdnblod.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import rechard.my.tool.flushcsdnblod.Proxy;
import rechard.my.tool.flushcsdnblod.ProxyCollector;
import rechard.my.tool.flushcsdnblod.ProxyFilterCollector;
import rechard.my.tool.flushcsdnblod.ProxyTool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProxykuaidailiExtractor extends ProxyCollector {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    String url ="https://www.kuaidaili.com/free/inha/";
    List<Proxy> list = new ArrayList<>();
    public Collection<Proxy> collect(){
            String content = null;
            try {
                logger.info("collect url from :"+url);
                int pageIndex=1;
                content = ProxyTool.getContent(url);
                while(content!=null){
                    String[] strings = StringUtils.delimitedListToStringArray(content,"\n");
                    boolean meetIp=false;
                    String ip="";
                    String port="";
                    for (String str:strings){
                        if(str.trim().matches("<td data-title=\"IP\">\\d*\\.\\d*\\.\\d*\\.\\d*<\\/td>")){
                            meetIp=true;
                            ip=str.trim().replaceAll("<td data-title=\"IP\">","").replaceAll("<\\/td>","");
                            continue;
                        }
                        if(meetIp){
                            port=str.trim().replaceAll("<td data-title=\"PORT\">","").replaceAll("<\\/td>","");
                            meetIp=false;
                            logger.info("find a proxy:"+ip+":"+port);
                            ProxyFilterCollector.put(new Proxy(ip,port));
                        }
                    }
                    //end of page to find next url
                    content=ProxyTool.getContent(url+(++pageIndex));
                }

            } catch (Exception e) {
                logger.error("error occur while collect proxy"+e.getMessage());
            }
        logger.info("find total proxy"+list.size());
        return list;
    }
}
