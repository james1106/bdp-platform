package com.bdp.framework.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * mybatis 配置数据源类
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfiguration{

	@Value("${spring.datasource.driver-class-name}")
	private String driveClassName;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String userName;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${mybatis.xmlLocation:classpath:mapper/**/*.xml}")
	private String xmlLocation;

	@Value("${mybatis.typeAliasesPackage:}")
	private String typeAliasesPackage;

	///////////////////// druid参数///////////////////////////////////////////////////
	@Value("${spring.datasource.filters:stat,wall}")
	private String filters;
	@Value("${spring.datasource.maxActive:10}")
	private int maxActive;
	@Value("${spring.datasource.initialSize:3}")
	private int initialSize;
	@Value("${spring.datasource.maxWait:60000}")
	private int maxWait;
	@Value("${spring.datasource.minIdle:5}")
	private int minIdle;
	@Value("${spring.datasource.timeBetweenEvictionRunsMillis:60000}")
	private int timeBetweenEvictionRunsMillis;
	@Value("${spring.datasource.minEvictableIdleTimeMillis:300000}")
	private int minEvictableIdleTimeMillis;
	@Value("${spring.datasource.validationQuery:select 1 from dual}")
	private String validationQuery;
	@Value("${spring.datasource.testWhileIdle:true}")
	private boolean testWhileIdle;
	@Value("${spring.datasource.testOnBorrow:false}")
	private boolean testOnBorrow;
	@Value("${spring.datasource.testOnReturn:false}")
	private boolean testOnReturn;
	@Value("${spring.datasource.poolPreparedStatements:true}")
	private boolean poolPreparedStatements;
	@Value("${spring.datasource.maxOpenPreparedStatements:20}")
	private int maxOpenPreparedStatements;
	//////////////////////////////////////////////////////////////////////////
	
	@Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName(driveClassName);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        try {
            druidDataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		if (StringUtils.isNotBlank(typeAliasesPackage)) {
			bean.setTypeAliasesPackage(typeAliasesPackage);
		}
		// 分页插件
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("reasonable", "true");
		properties.setProperty("supportMethodsArguments", "true");
		properties.setProperty("returnPageInfo", "check");
		properties.setProperty("params", "count=countSql");
		pageHelper.setProperties(properties);
		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Interceptor[] plugins = new Interceptor[] { pageHelper };
		bean.setPlugins(plugins);
		try {
			bean.setMapperLocations(resolver.getResources(xmlLocation));
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
