package com.bdp.framework.biz;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.biz.BaseBiz;
import com.bdp.framework.entity.User;
import com.bdp.framework.mapper.UserMapper;
import com.bdp.framework.vo.MenuTree;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User> {

	public User getUserInfo(String userName) {
		Example example=new Example(User.class);
		example.createCriteria().andEqualTo("username", userName);
		User user=selectOneByExample(example);
		return user;
	}

	public List<MenuTree> getMenusByUsername(String userName) {
		return null;//mapper.getMenusByUsername(userName);
	}

}
