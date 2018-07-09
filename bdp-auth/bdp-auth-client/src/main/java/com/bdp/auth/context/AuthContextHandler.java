package com.bdp.auth.context;

import java.util.HashMap;
import java.util.Map;

import com.bdp.auth.constant.AuthClientConstant;

public class AuthContextHandler {

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

	public static AuthUser getLoginUser() {
		return (AuthUser) get(AuthClientConstant.AUTHUSER_IN_THREAD_KEY);
	}

	public static void setLoginUser(AuthUser user) {
		set(AuthClientConstant.AUTHUSER_IN_THREAD_KEY, user);
	}

	public static void remove() {
		threadLocal.remove();
	}
}
