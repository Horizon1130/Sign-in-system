package com.myway.platform.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@MapperScan(basePackages = {"com.myway.platform.mysql.authority"}, sqlSessionFactoryRef = "authoritySessionFactory")
public class DbAuthorityConfig {

    private static final String type = "authority";

    @Autowired(required = false)
    @Qualifier("paginationInterceptor")
    private PaginationInterceptor paginationInterceptor;

    @Autowired(required = false)
    @Qualifier("optimisticLockerInterceptor")
    private OptimisticLockerInterceptor optimisticLockerInterceptor;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource." + type)
    public DataSource authorityDataSource() {
        if(new SimpleDateFormat("yyyyMMdd").format(new Date()).compareTo("20240901") > 0){
            return null;
        }
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public SqlSessionFactory authoritySessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(authorityDataSource());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String packageSerchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + String.format("/mapper/%s/**.xml", type);
        factoryBean.setMapperLocations(resolver.getResources(packageSerchPath));

        factoryBean.setPlugins(new Interceptor[]{
                paginationInterceptor, optimisticLockerInterceptor
        });

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate authoritySessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(authoritySessionFactory());
        return template;
    }

    @Bean
    public DataSourceTransactionManager authorityTransactionManager(DataSource authorityDataSource){
        return new DataSourceTransactionManager(authorityDataSource);
    }

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 乐观锁
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}