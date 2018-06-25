package com.qin.springboot.a002;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

//@Configuration
//@PropertySource({"classpath:c3p0config.properties"})
public class C3p0DatasourceConfig {

    //配置文件取值的第一种方法
    @Value("${mybatis.config-locations}")
    private String configLocation;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Bean(name="dataSource")
    @Qualifier(value="dataSource")//限定描述符除了能根据名字进行注入，但能进行更细粒度的控制如何选择候选者
    @Primary//用@Primary区分主数据源
    @ConfigurationProperties(prefix="c3p0")//指定配置文件中，前缀为c3p0的属性值
    public DataSource dataSource(){
        return DataSourceBuilder.create().type(ComboPooledDataSource.class).build();//创建数据源
    }
    /**
     *返回sqlSessionFactory
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(){
        try{
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource());

        // 读取配置
        sqlSessionFactory.setTypeAliasesPackage(typeAliasesPackage);

        //mapper文件目录
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations);
        sqlSessionFactory.setMapperLocations(resources);

        //读取mybatis配置文件，插件配置
        sqlSessionFactory.setConfigLocation(
                new DefaultResourceLoader().getResource(configLocation));

        return sqlSessionFactory;
        } catch (IOException e) {
           e.printStackTrace();
            return null;
        } catch (Exception e) {
          e.printStackTrace();
            return null;
        }
    }
}