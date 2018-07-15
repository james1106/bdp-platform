package com.bdp.framework.data.biz.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.framework.data.entity.User;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBizImpl<User> {

	public User readByName(String username) {
		return getEntityByJpql("select u from User u where u.username=?", username);
	}
	
}
