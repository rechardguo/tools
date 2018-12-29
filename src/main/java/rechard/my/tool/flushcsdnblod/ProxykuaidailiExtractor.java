package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProxykuaidailiExtractor extends ProxyCollector {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    String url ="https://www.kuaidaili.com/free/inha/";
    List<Proxy> list = new ArrayList<>();
    public Collection<Proxy> collect(){
        for(int i=0;i<10;i++){
            String content = null;
            try {
                logger.info("collect url:"+url+i);
                content = ProxyTool.getContent(url+i);
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
                        list.add(new Proxy(ip,port));
                    }
                }
            } catch (Exception e) {
                logger.error("error occur while collect proxy"+e.getMessage());
                continue;
            }
        }
        logger.info("find total proxy"+list.size());
        return list;
    }
}
