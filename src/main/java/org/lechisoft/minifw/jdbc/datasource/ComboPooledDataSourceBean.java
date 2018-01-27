package org.lechisoft.minifw.jdbc.datasource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import org.lechisoft.minifw.log.MiniLog;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ComboPooledDataSourceBean {
    public final static String CONF_PATH = "jdbc.properties";
    private static ComboPooledDataSource dataSource = null;

    /**
     * 获取数据源
     * 
     * @return ComboPooledDataSource对象
     */
    public static ComboPooledDataSource getDataSource() {
        return dataSource;
    }

    /**
     * 静态构造创建BasicDataSourceBean
     */
    static {
        MiniLog.debug(ComboPooledDataSourceBean.class.getName() + " -> "
                + Thread.currentThread().getStackTrace()[1].getMethodName() + " begin.");

        try {
            URL url = BasicDataSourceBean.class.getClassLoader().getResource(CONF_PATH);
            if (null == url) {
                MiniLog.error("Init datasource field:Can not find classpath/" + CONF_PATH);
            } else {
                // load jdbc.properties
                Properties props = new Properties();
                props.load(ComboPooledDataSourceBean.class.getClassLoader().getResourceAsStream(CONF_PATH));
                dataSource = new ComboPooledDataSource();

                // 动态为数据源属性赋值
                Set<Object> keySet = props.keySet();
                for (Object object : keySet) {
                    String key = object.toString();
                    String value = props.getProperty(key);

                    String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                    Method method = null;
                    try {
                        method = method == null
                                ? ComboPooledDataSource.class.getMethod(methodName, new Class[] { String.class })
                                : method;
                    } catch (Exception e) {
                        method = null;
                    }
                    try {
                        method = method == null
                                ? ComboPooledDataSource.class.getMethod(methodName, new Class[] { Integer.class })
                                : method;
                    } catch (Exception e) {
                        method = null;
                    }

                    if (method == null) {
                        MiniLog.warn("dataSource has no attribute：" + key);
                        continue;
                    } else {
                        MiniLog.debug(key + "=" + value);
                    }
                    method.invoke(dataSource, value);
                }

                MiniLog.debug("Init datasource:load classpath/" + CONF_PATH + " complete.");
            }
        } catch (IOException e) {
            MiniLog.error("load jdbc.properties failed.", e);
        } catch (Exception e) {
            MiniLog.error("create ComboPooledDataSource failed.", e);
        }

        MiniLog.debug(ComboPooledDataSourceBean.class.getName() + " -> "
                + Thread.currentThread().getStackTrace()[1].getMethodName() + " end.");
    }
}
