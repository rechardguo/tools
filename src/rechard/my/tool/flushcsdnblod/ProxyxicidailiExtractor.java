package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProxyxicidailiExtractor extends ProxyCollector {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    String url ="https://www.xicidaili.com/nn/";
    List<Proxy> list = new ArrayList<>();
    public Collection<Proxy> collect(){
        for(int i=0;i<10;i++){
            String content = null;
            try {
                content = ProxyTool.getContent(url+i);
                String[] strings = StringUtils.delimitedListToStringArray(content,"\n");
                boolean meetIp=false;
                String ip="";
                String port="";
                for (String str:strings){
                    if(str.trim().matches("<td>\\d*\\.\\d*\\.\\d*\\.\\d*<\\/td>")){
                        meetIp=true;
                        ip=str.trim().replaceAll("<td>","").replaceAll("<\\/td>","");
                        continue;
                    }
                    if(meetIp){
                        port=str.trim().replaceAll("<td>","").replaceAll("<\\/td>","");
                        meetIp=false;
                    }
                }
            } catch (Exception e) {
                //simply continue
                //e.printStackTrace();
                logger.error("error occur while collect proxy"+e.getMessage());
                continue;
            }
        }
        return list;
    }
}
