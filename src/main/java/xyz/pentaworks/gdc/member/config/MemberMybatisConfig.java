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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "xyz.pentaworks.gdc", annotationClass = MemberDatasource.class,
        sqlSessionFactoryRef = "MemberSessionFactory")
public class MemberMybatisConfig {

    private final ApplicationContext applicationContext;

    public MemberMybatisConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean("MemberConfig")
    @ConfigurationProperties(prefix = "spring.multi-datasource.member")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }


    @Bean(name = "MemberDatasource")
    public DataSource getDatasource(@Qualifier("MemberConfig") HikariConfig hikariConfig)
            throws UnsupportedEncodingException {
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean(name = "MemberSessionFactory")
    public SqlSessionFactory getSessionFactory(
            @Qualifier("MemberDatasource") final DataSource dataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory
                .setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));
        sqlSessionFactory.setTypeAliasesPackage("xyz.pentaworks.gdc.**.model.**");
        org.apache.ibatis.session.Configuration configuration =
                new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactory.setConfiguration(configuration);

        return sqlSessionFactory.getObject();
    }

    @Bean(name = "MemberSessionTemplate")
    public SqlSessionTemplate getSessionTemplate(
            @Qualifier("MemberSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "MemberTransactionManager")
    public DataSourceTransactionManager getTransactionManager(
            @Qualifier("MemberDatasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}

