package com.bdp.framework.mapper;

import java.util.List;

import com.bdp.framework.entity.User;
import com.bdp.framework.vo.MenuTree;

import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

	//List<MenuTree> getMenusByUsername(String userName);

}
