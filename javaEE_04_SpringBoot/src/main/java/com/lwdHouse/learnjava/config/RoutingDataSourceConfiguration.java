package com.lwdHouse.learnjava.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * RoutingDataSourceConfiguration：配置数据源，jdbcTemplate，事务管理器
 * 数据源：RoutingDataSource，从AbstractRoutingDataSource继承，AbstractRoutingDataSource
 *       是springframework提供的，用来管理多个数据源。我们构造这个数据源的时候，传入一个包含多数据源的map即可
 *       然后要覆写determineCurrentLookupKey方法，这个方法返回那个map的key值，来表示使用哪个数据源value。
 *       此方法会被框架调用用来决定每次使用哪个数据源。
 *
 * 【注意】：注意到DataSourceTransactionManager和JdbcTemplate引用的都是RoutingDataSource，
 *   所以，这种设计的一个限制就是：在一个请求中，一旦切换了内部数据源，在同一个事务中，不能再切到另一个，
 *   否则，DataSourceTransactionManager和JdbcTemplate操作的就不是同一个数据库连接。
 */
public class RoutingDataSourceConfiguration {

    @Primary
    @Bean
    DataSource dataSource(@Autowired @Qualifier(RoutingDataSourceContext.MASTER_DATASOURCE) DataSource masterDataSource,
                          @Autowired @Qualifier(RoutingDataSourceContext.SLAVE_DATASOURCE) DataSource slaveDataSource){
        var ds = new RoutingDataSource();
        ds.setTargetDataSources(Map.of(RoutingDataSourceContext.MASTER_DATASOURCE, masterDataSource,
                RoutingDataSourceContext.SLAVE_DATASOURCE, slaveDataSource));
        ds.setDefaultTargetDataSource(masterDataSource);
        return ds;
    }

    @Bean
    JdbcTemplate jdbcTemplate(/*这里注入的就会使Primary标注的DataSource*/@Autowired DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    DataSourceTransactionManager dataSourceTransactionManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}

class RoutingDataSource extends AbstractRoutingDataSource{

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getDataSourceRoutingKey();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSource ds = super.determineTargetDataSource();
        logger.info("determin target datasource: {}", ds);
        return ds;
    }
}
