package xyz.pentaworks.gdc.member.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;


@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "xyz.pentaworks.gdc", annotationClass = SSPDatasource.class,
        sqlSessionFactoryRef = "SSPSessionFactory")
public class SSPMybatisConfig {

    private final ApplicationContext applicationContext;

    public SSPMybatisConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean("SSPConfig")
    @ConfigurationProperties(prefix = "spring.multi-datasource.ssp")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "SSPDatasource")
    public DataSource getDatasource(@Qualifier("SSPConfig") HikariConfig hikariConfig)
            throws UnsupportedEncodingException {
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean(name = "SSPSessionFactory")
    public SqlSessionFactory getSessionFactory(
            @Qualifier("SSPDatasource") final DataSource dataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory
                .setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));
        sqlSessionFactory.setTypeAliasesPackage("xyz.pentaworks.gdc.**.model.**");

        return sqlSessionFactory.getObject();
    }

    @Bean(name = "SSPSessionTemplate")
    @Primary
    public SqlSessionTemplate getSessionTemplate(
            @Qualifier("SSPSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "SSPTransactionManager")
    @Primary
    public DataSourceTransactionManager getTransactionManager(
            @Qualifier("SSPDatasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}

