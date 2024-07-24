package com.malltail.erp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.malltail.erp.mapper.primary", sqlSessionFactoryRef = "sqlSessionPrimaryFactory")
@EnableTransactionManagement
public class DatabasePrimaryConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConfigurationProperties(prefix = "spring.primary-db.datasource.hikari")
    public HikariConfig hikariPrimaryConfig(){
        return new HikariConfig();
    }

    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource(){
        return new HikariDataSource(hikariPrimaryConfig());
        //return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "sqlSessionPrimaryFactory")
    public SqlSessionFactory sqlSessionPrimaryFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(primaryDataSource());
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/primary/*.xml"));
        factoryBean.setConfiguration(mybatisConfig());

        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlPrimarySessionTemplate")
    public SqlSessionTemplate sqlPrimarySessionTemplate(SqlSessionFactory primarySqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(primarySqlSessionFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration mybatisConfig(){
        return new org.apache.ibatis.session.Configuration();
    }
}