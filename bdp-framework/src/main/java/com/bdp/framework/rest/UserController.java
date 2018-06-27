package com.bdp.framework.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bdp.common.rest.BaseController;
import com.bdp.framework.biz.UserBiz;
import com.bdp.framework.entity.User;

@Controller
@RequestMapping("user")
public class UserController extends BaseController<UserBiz, User> {

	@RequestMapping(value = "/info/{userName}", method = RequestMethod.GET)
	public String getUserInfo(@PathVariable("userName") String userName) throws Exception {
		return "framework/user/info";
	}
	
	@RequestMapping(value = "/getUserByUserName/{userName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUserInfoByUserName(@PathVariable("userName") String userName) throws Exception {
		User userInfo = baseBiz.getUserInfo(userName);
		if (userInfo == null) {
			return ResponseEntity.status(401).body(false);
		} else {
			return ResponseEntity.ok(userInfo);
		}
	}
}
