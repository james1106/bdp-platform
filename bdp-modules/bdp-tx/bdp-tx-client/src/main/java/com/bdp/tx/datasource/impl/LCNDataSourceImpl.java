package com.bdp.tx.datasource.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bdp.tx.aop.bean.TxCompensateLocal;
import com.bdp.tx.aop.bean.TxTransactionLocal;
import com.bdp.tx.datasource.AbstractResourceProxy;
import com.bdp.tx.datasource.LCNDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 关系型数据库代理连接池对象
 */
@Component
public class LCNDataSourceImpl extends AbstractResourceProxy<Connection, LCNDBConnection> implements LCNDataSource {

	private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

	// 很重要，会根据当前线程是否绑定了TxTransactionLocal 来确定是否返回包装后的Connection
	// 创建包装的Connection，是实现分布式事务的核心机制，即代理原生连接，通过假关闭，延迟提交，回滚等操作来实现分布式事务
	@Override
	public Connection getConnection(ProceedingJoinPoint point) throws Throwable {
		// 说明有db操作.
		hasTransaction = true;
		initDbType();
		Connection connection = (Connection) loadConnection();
		if (connection == null) {
			connection = initLCNConnection((Connection) point.proceed());
			if (connection == null) {
				throw new SQLException("connection was overload");
			}
			return connection;
		} else {
			return connection;
		}
	}

	@Override
	protected Connection createLcnConnection(Connection connection, TxTransactionLocal txTransactionLocal) {
		nowCount++;
		if (txTransactionLocal.isHasStart()) {// 分布式事务发起方
			LCNStartConnection lcnStartConnection = new LCNStartConnection(connection, subNowCount);
			logger.debug("get new start connection - > " + txTransactionLocal.getGroupId());
			pools.put(txTransactionLocal.getGroupId(), lcnStartConnection);
			txTransactionLocal.setHasConnection(true);
			return lcnStartConnection;
		} else {// 分布式事务的参与方
			LCNDBConnection lcn = new LCNDBConnection(connection, dataSourceService, subNowCount);
			logger.debug("get new connection ->" + txTransactionLocal.getGroupId());
			pools.put(txTransactionLocal.getGroupId(), lcn);
			txTransactionLocal.setHasConnection(true);
			return lcn;
		}
	}

	@Override
	protected void initDbType() {
		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
		if (txTransactionLocal != null) {
			// 设置db类型
			txTransactionLocal.setType("datasource");
		}
		TxCompensateLocal txCompensateLocal = TxCompensateLocal.current();
		if (txCompensateLocal != null) {
			// 设置db类型
			txCompensateLocal.setType("datasource");
		}
	}
}
