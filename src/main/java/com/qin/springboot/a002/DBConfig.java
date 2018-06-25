package com.qin.springboot.a002;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by windwant on 2016/12/30.
 * implements EnvironmentAware, ApplicationContextAware
 */
@Configuration
public class DBConfig {

    @Autowired
    private Environment environment;


    //配置文件取值的第一种方法
    @Value("${mybatis.config-locations}")
    private String configLocation;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    /**
     * 写库 数据源配置
     * @return
     */
    @Bean(name = "writeDataSource")
    @Primary
    @Order(value = 1)
    @ConfigurationProperties(prefix = "mysql.datasource.write")
    public DataSource writeDataSource() {
        System.out.println(environment);
        return DruidDataSourceBuilder.create().build();
    }
    /**
     * 有多少个从库就要配置多少个
     * @return
     */
    @Bean(name = "readDataSource01")
    @Order(value = 2)
    @ConfigurationProperties(prefix = "mysql.datasource.read01")
    public DataSource readDataSourceOne() {
        return DruidDataSourceBuilder.create().build();
    }
    @Bean(name = "readDataSource02")
    @Order(value = 3)
    @ConfigurationProperties(prefix = "mysql.datasource.read02")
    public DataSource readDataSourceTwo() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "routingDataSource")
    @Order(value = 4)
    public DataSource routingDataSource(@Qualifier("writeDataSource") DataSource writeDataSource,
                                        @Qualifier("readDataSource01") DataSource readDataSource01,
                                        @Qualifier("readDataSource02") DataSource readDataSource02){

        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> dataSources = new HashMap<>();
        //把所有数据库都放在targetDataSources中,注意key值要和determineCurrentLookupKey()中代码写的一至，
        //否则切换数据源时找不到正确的数据源
        dataSources.put(DataSourceType.writeDataSource.name(), writeDataSource);
        dataSources.put(DataSourceType.readDataSource01.name(), readDataSource01);
        dataSources.put(DataSourceType.readDataSource02.name(), readDataSource02);


        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(writeDataSource);
        return routingDataSource;
    }


    @Bean
    @Order(value = 4)
    @Lazy
    public SqlSessionFactory sqlSessionFactory(@Qualifier("routingDataSource") DataSource routingDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(routingDataSource);
       // factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        // 读取配置
        factoryBean.setTypeAliasesPackage(typeAliasesPackage);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mapper/*.xml"));
        //factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("routingDataSource") DataSource routingDataSource) throws Exception {
        return new DataSourceTransactionManager(routingDataSource);
    }

//    private ApplicationContext ctx;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.ctx = applicationContext;
//    }
}
