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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.malltail.erp.mapper.second", sqlSessionFactoryRef = "sqlSessionSecondFactory")
@EnableTransactionManagement
public class DatabaseSecondConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConfigurationProperties(prefix = "spring.second-db.datasource.hikari")
    public HikariConfig hikariSecondConfig(){
        return new HikariConfig();
    }

    @Bean(name = "secondDataSource")
    public DataSource secondDataSource(){
        return new HikariDataSource(hikariSecondConfig());
        //return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionSecondFactory")
    public SqlSessionFactory sqlSessionSecondFactory(@Qualifier("secondDataSource") DataSource secondDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(secondDataSource());
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/second/*.xml"));
        factoryBean.setConfiguration(mybatisSecondConfig());

        return factoryBean.getObject();
    }

    @Bean(name = "sqlSecondSessionTemplate")
    public SqlSessionTemplate sqlSecondSessionTemplate(SqlSessionFactory secondSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(secondSqlSessionFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration mybatisSecondConfig(){
        return new org.apache.ibatis.session.Configuration();
    }
}
