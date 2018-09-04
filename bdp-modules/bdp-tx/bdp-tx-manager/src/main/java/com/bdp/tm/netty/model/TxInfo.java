package com.bdp.tm.netty.model;

import com.alibaba.fastjson.JSONObject;
import com.bdp.tm.model.ChannelSender;
import com.bdp.tx.commons.model.JsonModel;

/**
 * 对事务信息的封装
 * 
 * @author jack
 *
 */
public class TxInfo extends JsonModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 任务唯一标示
	 */
	private String kid;

	/**
	 * 模块管道名称（netty管道名称）
	 */
	private String channelAddress;

	/**
	 * 是否通知成功
	 */
	private int notify;

	/**
	 * 0 不在事务组 1 已在事务组 <br/>
	 * 即表示参与方模块是否已经在事务组中了,A模块f0方法作为事务发起方先后调用事务参与方B模块的f1,f2方法
	 * 调用f1此时不在事务组中，再调用f2时则已经事务组中了。这个属性很重要，
	 * 最后事务确认时的TxManagerSenderServiceImpl.transaction()中会对这个属性进行判断
	 */
	private int isGroup;

	/**
	 * tm识别标示
	 */
	private String address;

	/**
	 * tx识别标示
	 */
	private String uniqueKey;

	/**
	 * 管道发送数据
	 */
	private ChannelSender channel;

	/**
	 * 业务方法名称
	 */
	private String methodStr;

	/**
	 * 模块名称
	 */
	private String model;

	/**
	 * 模块地址
	 */
	private String modelIpAddress;

	/**
	 * 是否提交（临时数据），在事务补偿时会临时借用该变量
	 */
	private int isCommit;

	public int getIsCommit() {
		return isCommit;
	}

	public void setIsCommit(int isCommit) {
		this.isCommit = isCommit;
	}

	public String getMethodStr() {
		return methodStr;
	}

	public void setMethodStr(String methodStr) {
		this.methodStr = methodStr;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelIpAddress() {
		return modelIpAddress;
	}

	public void setModelIpAddress(String modelIpAddress) {
		this.modelIpAddress = modelIpAddress;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public ChannelSender getChannel() {
		return channel;
	}

	public void setChannel(ChannelSender channel) {
		this.channel = channel;
	}

	public String getChannelAddress() {
		return channelAddress;
	}

	public void setChannelAddress(String channelAddress) {
		this.channelAddress = channelAddress;
	}

	public int getNotify() {
		return notify;
	}

	public void setNotify(int notify) {
		this.notify = notify;
	}

	public int getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(int isGroup) {
		this.isGroup = isGroup;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("kid", getKid());
		jsonObject.put("channelAddress", getChannelAddress());
		jsonObject.put("notify", getNotify());
		jsonObject.put("isGroup", getIsGroup());
		jsonObject.put("address", getAddress());
		jsonObject.put("uniqueKey", getUniqueKey());

		jsonObject.put("model", getModel());
		jsonObject.put("modelIpAddress", getModelIpAddress());
		jsonObject.put("methodStr", getMethodStr());

		return jsonObject.toString();
	}
}
