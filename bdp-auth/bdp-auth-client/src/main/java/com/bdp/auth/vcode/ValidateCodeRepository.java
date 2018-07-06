package com.bdp.auth.vcode;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验码存取器
 */
public interface ValidateCodeRepository {

	/**
	 * 保存验证码
	 *
	 * @param request
	 * @param code
	 * @param validateCodeType
	 */
	void save(HttpServletRequest request, ValidateCode code);

	/**
	 * 获取验证码
	 *
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	ValidateCode get(HttpServletRequest request);

	/**
	 * 移除验证码
	 *
	 * @param request
	 * @param codeType
	 */
	void remove(HttpServletRequest request);
}
