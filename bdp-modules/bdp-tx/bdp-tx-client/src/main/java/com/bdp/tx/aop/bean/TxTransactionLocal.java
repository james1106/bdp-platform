package com.bdp.tx.aop.bean;

import com.alibaba.fastjson.JSONObject;
import com.bdp.tx.framework.utils.SocketManager;
import com.bdp.tx.model.Request;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式事务远程调用控制对象
 */
public class TxTransactionLocal {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 使用InheritableThreadLocal只能获取父线程中设置的线程变量，
	// 但是hystrix使用THREAD隔离策略时从线程池中拿到的线程不能保证一定是当前调用线程的子线程，所以InheritableThreadLocal不能保证THREAD策略好使。
	// 推测：这里使用InheritableThreadLocal原因应该是保证当前微服务下在一个事务方法中另开线程调用其它事务方法时可以获取到父线程保存的变量。
	public final static ThreadLocal<TxTransactionLocal> currentLocal = new InheritableThreadLocal<TxTransactionLocal>();

	private String groupId;

	private int maxTimeOut;

	private Map<String, String> cacheModelInfo = new ConcurrentHashMap<>();

	/**
	 * 当前模块是否已经在该事务组中，同一个模块被一个事务组多次请求，第一次不在，以后就在
	 */
	private boolean hasIsGroup = false;

	/**
	 * 是否是发起方模块
	 */
	private boolean hasStart = false;

	/**
	 * 时候已经获取到连接对象
	 */
	private boolean hasConnection = false;

	private String kid;

	// 事务的类型，如datasource表示数据库事务
	private String type;

	private boolean readOnly = false;

	public boolean isHasIsGroup() {
		return hasIsGroup;
	}

	public void setHasIsGroup(boolean hasIsGroup) {
		this.hasIsGroup = hasIsGroup;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public boolean isHasStart() {
		return hasStart;
	}

	public void setHasStart(boolean hasStart) {
		this.hasStart = hasStart;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isHasConnection() {
		return hasConnection;
	}

	public void setHasConnection(boolean hasConnection) {
		this.hasConnection = hasConnection;
	}

	public int getMaxTimeOut() {
		return maxTimeOut;
	}

	public void setMaxTimeOut(int maxTimeOut) {
		this.maxTimeOut = maxTimeOut;
	}

	public static TxTransactionLocal current() {
		return currentLocal.get();
	}

	public static void setCurrent(TxTransactionLocal current) {
		currentLocal.set(current);
	}

	public void putLoadBalance(String key, String data) {
		cacheModelInfo.put(key, data);
		// 与TxManager通讯
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("g", getGroupId());
		jsonObject.put("k", key);
		jsonObject.put("d", data);
		logger.debug("putLoadBalance--> start ");
		Request request = new Request("plb", jsonObject.toString());
		SocketManager.getInstance().sendMsg(request);
		logger.debug("putLoadBalance--> end");
	}

	public String getLoadBalance(String key) {
		String old = cacheModelInfo.get(key);
		logger.debug("cacheModelInfo->" + old);
		if (old == null) {
			// 与TxManager通讯
			logger.debug("getLoadBalance--> start");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("g", getGroupId());
			jsonObject.put("k", key);
			Request request = new Request("glb", jsonObject.toString());
			String json = SocketManager.getInstance().sendMsg(request);
			logger.debug("getLoadBalance--> end ,res - >" + json);
			if (StringUtils.isNotEmpty(json)) {
				return json;
			}
		}
		return old;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

}
