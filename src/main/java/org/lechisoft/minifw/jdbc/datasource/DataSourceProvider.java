package org.lechisoft.minifw.jdbc.datasource;

import javax.sql.DataSource;

public class DataSourceProvider {

    public static enum DataSourceType {
        dbcp2, c3p0
    }

    public static DataSource getDataSource(DataSourceType dstype) {

        if (dstype == DataSourceType.c3p0) {
            return ComboPooledDataSourceBean.getDataSource();
        }

        return BasicDataSourceBean.getDataSource();
    }

}
