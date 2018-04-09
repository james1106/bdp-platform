package com.bdp.framework.biz;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.biz.BaseBiz;
import com.bdp.framework.entity.User;
import com.bdp.framework.mapper.UserMapper;
import com.bdp.framework.vo.FrontUser;
import com.bdp.framework.vo.MenuTree;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User> {

	public FrontUser getUserInfo(String userName) {
		return null;
	}

	public List<MenuTree> getMenusByUsername(String userName) {
		return null;//mapper.getMenusByUsername(userName);
	}

}
