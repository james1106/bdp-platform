package com.bdp.tx.datasource.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdp.tx.aop.bean.TxTransactionLocal;
import com.bdp.tx.datasource.AbstractTransactionThread;
import com.bdp.tx.datasource.ICallClose;
import com.bdp.tx.datasource.ILCNResource;
import com.bdp.tx.datasource.LCNConnection;
import com.bdp.tx.datasource.service.DataSourceService;
import com.bdp.tx.framework.task.TaskGroup;
import com.bdp.tx.framework.task.TaskGroupManager;
import com.bdp.tx.framework.task.TaskState;
import com.bdp.tx.framework.task.TxTask;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

/**
 * 事务参与方获取的java.sql.Connection的代理类，很重要，分布式实现的实现奇迹发生的地方
 */
public class LCNDBConnection extends AbstractTransactionThread implements LCNConnection {

	private Logger logger = LoggerFactory.getLogger(LCNDBConnection.class);

	private ThreadLocal<Boolean> isClose = new ThreadLocal<>();

	private volatile int state = 1;

	private Connection connection;

	private DataSourceService dataSourceService;

	private ICallClose<ILCNResource<?>> runnable;

	private int maxOutTime;

	private String groupId;

	// 很重要，会另开线程，通过该变量做线程同步，最后由相应的TransactionServer实现类中唤醒，
	// 并进行最终的数据提交或者回滚.再次强调线程同步
	private TxTask waitTask;

	private boolean readOnly = false;

	public LCNDBConnection(Connection connection, DataSourceService dataSourceService,
			ICallClose<ILCNResource<?>> runnable) {
		logger.debug("init lcn connection ! ");
		this.connection = connection;
		this.runnable = runnable;
		this.dataSourceService = dataSourceService;
		TxTransactionLocal transactionLocal = TxTransactionLocal.current();
		groupId = transactionLocal.getGroupId();
		maxOutTime = transactionLocal.getMaxTimeOut();
		TaskGroup taskGroup = TaskGroupManager.getInstance().createTask(transactionLocal.getKid(),
				transactionLocal.getType());
		waitTask = taskGroup.getCurrent();
	}

	@Override
	public void commit() throws SQLException {
		logger.debug("commit label");
		state = 1;
		close();
		isClose.set(true);
	}

	@Override
	public void rollback() throws SQLException {
		logger.debug("rollback label");
		state = 0;
		close();
		isClose.set(true);
	}

	protected void closeConnection() throws SQLException {
		runnable.close(this);
		connection.close();
		logger.debug("lcnConnection closed groupId:" + groupId);
	}

	/**
	 * 覆盖java.sql.Connection中的close()方法，<br/>
	 * 在Connection关闭时再根据分布式事务协调器来决定是回滚还是提交事务
	 */
	@Override
	public void close() throws SQLException {
		if (isClose.get() != null && isClose.get()) {
			return;
		}
		if (connection == null || connection.isClosed()) {
			return;
		}
		if (readOnly) {
			closeConnection();
			logger.debug("now transaction is readOnly , groupId:" + groupId);
			return;
		}
		logger.debug("now transaction state is " + state + ", (1:commit,0:rollback) groupId:" + groupId);
		if (state == 0) {
			rollbackConnection();
			closeConnection();
			logger.debug("rollback transaction ,groupId:" + groupId);
		}
		// 表示本地框架已经提交事务，比如Spring框架，即说明本地执行没有问题。
		// 但此时还需要根据分布式事务管理器来协调是否应该提交.
		if (state == 1) {
			TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
			boolean hasGroup = (txTransactionLocal != null) ? txTransactionLocal.isHasIsGroup() : false;
			if (hasGroup) {
				// 加入队列的连接，仅操作连接对象，不处理事务
				logger.debug("connection hasGroup -> " + hasGroup);
				return;
			}
			startRunnable();
		}
	}

	@Override
	protected void rollbackConnection() throws SQLException {
		connection.rollback();
	}

	/**
	 * 对事务进行提交或回滚操作，注意，这个方法其实是在父类的startRunnable()<br/>
	 * 方法中通过开启线程的方式调用的， 所以里面使用了线程同步技术
	 */
	public void transaction() throws SQLException {
		if (waitTask == null) {
			rollbackConnection();
			logger.info(" waitTask is null");
			return;
		}

		// 考虑超时情况，即成功加入事务组后，但是在超时时间内没有等到TxManager发送来的提交或回滚命令
		// 这里做一个补救措施，主动去查询TxManager，根据返回结果再决定事务提交还是回滚
		Timer timer = new Timer();
		logger.info(" maxOutTime : " + maxOutTime);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("auto execute ,groupId:" + getGroupId());
				dataSourceService.schedule(getGroupId(), waitTask);
			}
		}, maxOutTime);
		logger.info("transaction is wait for TxManager notify, groupId : " + getGroupId());

		// 当前线程阻塞，等待被唤醒，有3个地方可以唤醒当前线程：
		// 1.加入事务组失败时会被唤醒，代码TxDoTransactionServerImpl，加入成功则靠第2条来主动唤醒
		// 2.TxManager发送过来的提交或回滚命令可以唤醒，如果没有接收到TxManager命令再靠第3条补救措施
		// 3.上面定义的Timer超时了，通过主动查询TxManager根据返回值，设置执行waitTask状态，执行唤醒
		waitTask.awaitTask();

		// 走到这里说明整个分布式事务未超时，所以取消上面的定时器设置
		timer.cancel();

		int rs = waitTask.getState();
		try {
			if (rs == 1) {
				connection.commit();
			} else {
				rollbackConnection();
			}
			logger.info("lcn transaction over, res -> groupId:" + getGroupId() + " and  state is "
					+ (rs == 1 ? "commit" : "rollback"));

		} catch (SQLException e) {
			logger.info("lcn transaction over,but connection is closed, res -> groupId:" + getGroupId());
			// 很重要，再次设置提交的结果，这个结果会在ActionTServiceImpl.notifyWaitTask()中通过自循环等待获取，然后发送给TxManager
			waitTask.setState(TaskState.connectionError.getCode());
		}
		waitTask.remove();
	}

	public String getGroupId() {
		return groupId;
	}

	public TxTask getWaitTask() {
		return waitTask;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		if (connection != null) {
			connection.setAutoCommit(false);
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {

		if (readOnly) {
			this.readOnly = readOnly;
			logger.debug("setReadOnly - >" + readOnly);
			connection.setReadOnly(readOnly);
			TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
			txTransactionLocal.setReadOnly(readOnly);
		}
	}

	/***** default *******/

	@Override
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return connection.prepareCall(sql);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return connection.nativeSQL(sql);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return connection.getAutoCommit();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return connection.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return connection.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		connection.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return connection.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return connection.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return connection.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		connection.clearWarnings();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return connection.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return connection.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		connection.setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		connection.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return connection.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return connection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		connection.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return connection.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return connection.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return connection.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		return connection.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return connection.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return connection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return connection.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return connection.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		connection.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		connection.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return connection.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return connection.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return connection.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return connection.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		connection.setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return connection.getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		connection.abort(executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		connection.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return connection.getNetworkTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return connection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return connection.isWrapperFor(iface);
	}
}
