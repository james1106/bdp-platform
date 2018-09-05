package com.bdp.tx.annotation;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TxTransaction {


    /**
     * 是否为分布式事务的发起方，如果要启用一个分布式事务，必须得有一个发起方
     * 因为不明确指定发起方则程序<b>很难</b>根据其它条件来判读是否要开始一个分布式事务
     * 究竟难在哪里？这样设计的目的是什么？
     * @return true 是:是发起方 false 否:是参与方
     */
    boolean isStart() default false;


    /**
     * 回滚的异常
     * @return
     */
    Class<? extends Throwable>[] rollbackFor() default {};


    /**
     * 不回滚的异常
     * @return
     */
    Class<? extends Throwable>[] noRollbackFor() default {};

}
