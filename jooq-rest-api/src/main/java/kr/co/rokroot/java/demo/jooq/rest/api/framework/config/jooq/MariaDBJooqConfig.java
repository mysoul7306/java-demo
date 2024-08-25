/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.framework.config.jooq;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.*;
import org.jooq.impl.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@EnableTransactionManagement(proxyTargetClass = true)
public class MariaDBJooqConfig {

    @Value("${db.mariadb.host}")
    private String host;

    @Value("${db.mariadb.port}")
    private String port;

    @Value("${db.mariadb.username}")
    private String username;

    @Value("${db.mariadb.password}")
    private String password;

    @Primary
    @Bean(name = "mariaDBDataSource")
    public DataSource mariaDBDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(String.format("jdbc:mariadb://%s:%s?allowMultiQueries=true&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8", host, port));
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(3);
        dataSource.setMaxLifetime(59302);

        return dataSource;
    }

    @Bean(name = "mariaDBTransactionManager")
    public PlatformTransactionManager mariaDBTransactionManager(@Qualifier(value = "mariaDBDataSource") DataSource mariaDBDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(mariaDBDataSource);
        transactionManager.setDefaultTimeout(3);
        transactionManager.setRollbackOnCommitFailure(true);

        return transactionManager;
    }

    @Bean(name = "mariaDBDataSourceConnectionProvider")
    public DataSourceConnectionProvider mariaDBDataSourceConnectionProvider(@Qualifier(value = "mariaDBDataSource") DataSource mariaDBDataSource) throws SQLException {
        TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(mariaDBDataSource);
        proxy.setLoginTimeout(3);

        return new DataSourceConnectionProvider(proxy);
    }

    @Primary
    @Bean(name = "mariaDBDSL")
    public DSLContext mariaDBDSL(@Qualifier(value = "mariaDBDataSourceConnectionProvider") DataSourceConnectionProvider mariaDBDataSourceConnectionProvider) {
        DefaultConfiguration conf = new DefaultConfiguration();
        conf.set(mariaDBDataSourceConnectionProvider);
        conf.set(SQLDialect.MARIADB);
        conf.set(new DefaultExecuteListenerProvider(new ExceptionTranslator()));

        return new DefaultDSLContext(conf);
    }


    static class ExceptionTranslator implements ExecuteListener {

        @Override
        public void exception(ExecuteContext ctx) {
            SQLDialect dialect = ctx.configuration().dialect();
            SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.name());

            ctx.exception(translator.translate("Access database using jOOQ", ctx.sql(), ctx.sqlException()));
        }
    }
}
