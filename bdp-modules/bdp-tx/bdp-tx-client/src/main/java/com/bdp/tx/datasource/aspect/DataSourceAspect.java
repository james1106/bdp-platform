package com.bdp.tx.datasource.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bdp.tx.datasource.LCNDataSource;

import java.sql.Connection;

/**
 * 数据库连接的切面类,会切入到javax.sql.DataSource.getConnection方法中，
 * 由LCNDataSource根据线程变量来创建数据库连接，返回原生连接或者经过包装后的代理连接 <br/>
 */
@Aspect
@Component
public class DataSourceAspect {

	private Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

	@Autowired
	private LCNDataSource lcnDataSource;

	@Around("execution(* javax.sql.DataSource.getConnection(..))")
	public Connection around(ProceedingJoinPoint point) throws Throwable {
		logger.debug("getConnection-start---->");
		Connection connection = lcnDataSource.getConnection(point);
		logger.debug("connection-->" + connection);
		logger.debug("getConnection-end---->");
		return connection;
	}

}
