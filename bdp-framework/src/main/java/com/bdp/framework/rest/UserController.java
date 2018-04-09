package com.bdp.framework.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bdp.common.rest.BaseController;
import com.bdp.framework.biz.UserBiz;
import com.bdp.framework.entity.User;
import com.bdp.framework.vo.FrontUser;
import com.bdp.framework.vo.MenuTree;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserBiz, User> {

	@RequestMapping(value = "/info/{userName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUserInfo(@PathVariable("userName") String userName) throws Exception {
		FrontUser userInfo = baseBiz.getUserInfo(userName);
		if (userInfo == null) {
			return ResponseEntity.status(401).body(false);
		} else {
			return ResponseEntity.ok(userInfo);
		}
	}

	@RequestMapping(value = "/menus/{userName}", method = RequestMethod.GET)
	@ResponseBody
	public List<MenuTree> getMenusByUsername(@PathVariable("userName") String userName) throws Exception {
		return baseBiz.getMenusByUsername(userName);
	}
}
