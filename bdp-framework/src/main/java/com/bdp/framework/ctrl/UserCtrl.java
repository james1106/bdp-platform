package com.bdp.framework.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bdp.framework.data.biz.impl.UserBiz;
import com.bdp.framework.data.entity.User;

@Controller
@RequestMapping("user")
public class UserCtrl extends BaseCtrl<UserBiz, User> {

	@RequestMapping(value = "/getUserByUserName/{username}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<User> getUserUserName(@PathVariable("username") String username) throws Exception {
		User user = baseBiz.getEntityByJpql("select u from User u where u.username=?", username);
		return ResponseEntity.ok(user);
	}
}
