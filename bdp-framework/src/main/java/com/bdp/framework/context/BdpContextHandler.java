package com.bdp.framework.context;

import java.util.HashMap;
import java.util.Map;

import com.bdp.framework.constant.UserConstant;
import com.bdp.framework.entity.User;

public class BdpContextHandler {

	public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

	public static void set(String key, Object value) {
		Map<String, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			threadLocal.set(map);
		}
		map.put(key, value);
	}

	public static Object get(String key) {
		Map<String, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			threadLocal.set(map);
		}
		return map.get(key);
	}

	public static User getLoginUser() {
		return (User) get(UserConstant.USER_IN_SESSION_KEY);
	}

	public static void setLoginUser(User user) {
		set(UserConstant.USER_IN_SESSION_KEY, user);
	}

	public static void remove() {
		threadLocal.remove();
	}
}
