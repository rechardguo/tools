package rechard.my.tool.flushcsdnblod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class Config {
    private Config(){}
    private static volatile Properties cfg=null;
    public static final String PROXY_PERSIST_FILE = "proxy.persist.file";
    public static final String PROXY_VALIDATION_URL = "proxy.validation.url";
    public static final String PROXY_VALIDATION_THREAD_NUMBER = "proxy.validation.threads.number";
    public static final String FLUSH_THREAD_NUMBER = "flush.threads.number";

    private static Logger logger= LoggerFactory.getLogger(Config.class);

    private static void initializeDefaultConfig() throws Exception{
        // short-circuit if already initialized
        if (cfg != null) {
            return;
        }
        InputStream is = null;
        Properties props = new Properties();
        String filename="flush_config.properties";
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        try {
            if(is != null) {
                is = new BufferedInputStream(is);
                logger.info("the specified file : '" + filename + "' from the class resource path.");
            } else {
                is = new BufferedInputStream(new FileInputStream(filename));
                logger.info("the specified file : '" + filename + "'");
            }
            props.load(is);
        } catch (IOException ioe) {
            logger.info("Properties file: '"
                    + filename + "' could not be read.", ioe);
            throw ioe;
        }
        finally {
            if(is != null)
                try { is.close(); } catch(IOException ignore) {}
        }
        cfg=props;
    }
    public static void initialize() throws Exception{
        initializeDefaultConfig();
        String proxyPersistFile = System.getProperty(PROXY_PERSIST_FILE);
        if(proxyPersistFile == null)
            cfg.setProperty(PROXY_PERSIST_FILE,proxyPersistFile);
    }

    public static String getStringProperty(String name) {
        return getStringProperty(name, null);
    }

    public static int getIntProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public static int getIntProperty(String name, int def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Get the trimmed String value of the property with the given
     * <code>name</code> or the given default value if the value is
     * null or empty after trimming.
     */
    public static String getStringProperty(String name, String def){
        if (cfg==null) {
            synchronized (Config.class){
                if(cfg==null)
                    try {
                        initialize();
                    } catch (Exception e) {
                        logger.error("initialize error "+e.getMessage());
                    }
            }
        }
        String val = cfg.getProperty(name, def);
        if (val == null) {
            return def;
        }
        val = val.trim();
        return (val.length() == 0) ? def : val;
    }
}
