package com.bdp.framework.biz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.data.biz.impl.BaseBizImpl;
import com.bdp.framework.entity.User;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBizImpl<User> {

	public User readByName(String username) {
		return getEntityByJpql("select u from User u where u.username=?", username);
	}
	
}
