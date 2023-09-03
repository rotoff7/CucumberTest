package testing.utils;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class UtilClass {
    public static DataSource getH2DataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost:9092/mem:testdb");
        dataSource.setUser("user");
        dataSource.setPassword("pass");
        return dataSource;
    }
}
