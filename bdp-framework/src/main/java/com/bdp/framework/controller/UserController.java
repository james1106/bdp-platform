package com.bdp.framework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bdp.common.rest.BaseController;
import com.bdp.framework.biz.UserBiz;
import com.bdp.framework.entity.User;

@Controller
@RequestMapping("user")
public class UserController extends BaseController<UserBiz, User> {

	@RequestMapping(value = "/getUserByUserName/{username}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<User> getUserUserName(@PathVariable("username") String username) throws Exception {
		User user = baseBiz.getEntityByJpql("from User u where u.username", username);
		return ResponseEntity.ok(user);
	}
}
