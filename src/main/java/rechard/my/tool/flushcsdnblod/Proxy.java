package rechard.my.tool.flushcsdnblod;

import java.util.concurrent.atomic.AtomicInteger;

public class Proxy {
   private String ip;
   private String port;
   private AtomicInteger invalidateCount=new AtomicInteger(0);
   private long reponseTime;//unit is millsecond


    public Proxy(String ip, String port){
        this(ip,port,0);
    }

    public Proxy(String ip, String port,long reponseTime) {
        this.ip = ip;
        this.port = port;
        this.reponseTime=reponseTime;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public int increaseInvalidateCount() {
       return this.invalidateCount.incrementAndGet();
    }
    public int getInvalidateCount(){
        return this.invalidateCount.intValue();
    }

    public long getReponseTime() {
        return reponseTime;
    }

    public void setReponseTime(long reponseTime) {
        this.reponseTime = reponseTime;
    }

    @Override
    public String toString() {
        return ip+":"+port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        if (ip != null ? !ip.equals(proxy.ip) : proxy.ip != null) return false;
        return port != null ? port.equals(proxy.port) : proxy.port == null;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }
}
