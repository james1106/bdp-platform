package com.bdp.tx.datasource;

import org.aspectj.lang.ProceedingJoinPoint;

import java.sql.Connection;

/**
 * 相当于连接工厂，类似于javax.sql.DataSource，通过该类来获取经过包装后的数据库连接，<br/>
 * 其实现类参考LCNDataSourceImpl
 */
public interface LCNDataSource {
	// 很重要，会根据当前线程是否绑定了TxTransactionLocal 来确定是否返回包装后的Connection
	Connection getConnection(ProceedingJoinPoint point) throws Throwable;
}
