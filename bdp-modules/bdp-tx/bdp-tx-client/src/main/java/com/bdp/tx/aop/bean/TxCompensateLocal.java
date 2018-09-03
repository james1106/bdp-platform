package com.bdp.tx.aop.bean;

/**
 * 分布式事务补偿远程调用控制对象<br/>
 * 事务补偿发生的条件:<br/>
 * 当一次分布式事务执行到最后，由事务发起方向TxMananger发送事务提交时，<br/>
 * TxManager向各事务的参与方法发送事务提交命令，如果此时TxManager无法通知到某些<br/>
 * 事务的参与方，即无法做到当前所有事务的参与方都执行了提交命令，此时这个分布式事务会被标记为补偿事务。<br/>
 * 接下来TxManager会做以下几个事情：<br/>
 * 1.执行一个URL回调，即TxManager中配置tm.compensate.notifyUrl，这个URL由各业务系统自已实现，主要是给实施人员一个通知<br/>
 * 让其知道现在存在补偿事务，在这个URL中可以做发邮件或者发短信通知给实施人员。<br/>
 * 2.根据配置，确定是否执行自动补偿，即tm.compensate.auto，如果该属性设置为true,则TxManager执行自动补偿操作，即给该事务的发起方发送<br/>
 * 事务补偿的通知，发起方接收到通知后再次执行整个业务过程，区别是在整个业务过程执行的最后发起方向TxManager发送事务提交后，TxManager会判断<br/>
 * 当前为补偿事务，然后根据记录的上次事务执行的结果，向所有事务的参与方发送事务提交或者回滚的命令，如果上次通知到了正确提交了，则这次回滚，否则这次提交。<br/>
 * 现在任何分布式事务框架都不是完美的，即使是JTA事务两段锁协议一样无法保证第二段阶段能通知到事务的各参与方，一样可能存在某些参与方无法通知到出现数据不一致的情况，只是这种概率极小罢了。<br/>
 * 另外：这里纠正一下以前使用hibernate时一个认识错误，在使用hibernate+spring做事务控制时总感觉是事务提交时才开始对数据库做操作，比如校验唯一性，非空等<br/>
 * 其实不是这样，给人这个错觉的原因是hibernate对实体对象所做的操作，或者执行的一些HQL语句其实是先作登记的，然后在session关闭，事务提交前统一执行，<br/>
 * 所以很多时候会发现一个方法里有update操作，但是执行完方法没有报错，最后在外层提交事务的方法中报错了，原因就是hibernate只是先把update做了登记，并未执行<br/>
 * 所以给人的错觉是提交事务时才做数据校验。当前这个分布式框架也是基于这个原则来做的，首先如果分布式事务的参与方都已经执行完成，剩下的只有事务的提交，此时出错的可能极小<br/>
 * 另外还需要注意：分布式事务各参与方的业务方法要保证多次执行效果是一致的，不要第一次执行时与补偿执行时两次执行的效果不一致，简单例子，不要在分布式事务中做全量update,<br/>
 * 同样的update语句，前后两次执行可能影响的行数是不一样的。如果遇到这种情况，想想看是不是因为你业务划分不清。
 */
public class TxCompensateLocal {

	public final static ThreadLocal<TxCompensateLocal> currentLocal = new InheritableThreadLocal<TxCompensateLocal>();

	private String groupId;

	private String type;

	private int startState;

	public int getStartState() {
		return startState;
	}

	public void setStartState(int startState) {
		this.startState = startState;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static TxCompensateLocal current() {
		return currentLocal.get();
	}

	public static void setCurrent(TxCompensateLocal current) {
		currentLocal.set(current);
	}

}
