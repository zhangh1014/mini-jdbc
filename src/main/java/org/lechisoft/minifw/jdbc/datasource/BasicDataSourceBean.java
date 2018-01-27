package org.lechisoft.minifw.jdbc.datasource;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.lechisoft.minifw.log.MiniLog;

public class BasicDataSourceBean {
    public final static String CONF_PATH = "jdbc.properties";
    private static BasicDataSource dataSource = null;

    /**
     * 获取数据源
     * 
     * @return BasicDataSource对象
     */
    public static BasicDataSource getDataSource() {
        return dataSource;
    }

    /**
     * 静态构造创建BasicDataSourceBean
     */
    static {
        MiniLog.debug(BasicDataSourceBean.class.getName() + " -> "
                + Thread.currentThread().getStackTrace()[1].getMethodName() + " begin.");

        try {
            URL url = BasicDataSourceBean.class.getClassLoader().getResource(CONF_PATH);
            if (null == url) {
                MiniLog.error("Init datasource field:Can not find classpath/" + CONF_PATH);
            } else {
                // load jdbc.properties
                Properties props = new Properties();
                props.load(BasicDataSourceBean.class.getClassLoader().getResourceAsStream(CONF_PATH));
                dataSource = BasicDataSourceFactory.createDataSource(props);

                MiniLog.debug("Init datasource:load classpath/" + CONF_PATH + " complete.");
            }
        } catch (IOException e) {
            MiniLog.error("load jdbc.properties failed.", e);
        } catch (Exception e) {
            MiniLog.error("create BasicDataSource failed.", e);
        }

        MiniLog.debug(BasicDataSourceBean.class.getName() + " -> "
                + Thread.currentThread().getStackTrace()[1].getMethodName() + " end.");
    }
}
