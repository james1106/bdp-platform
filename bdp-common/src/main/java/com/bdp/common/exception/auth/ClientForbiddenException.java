package com.bdp.common.exception.auth;


import com.bdp.common.constant.CommonConstants;
import com.bdp.common.exception.BaseException;

/**
 * Created by ace on 2017/9/12.
 */
public class ClientForbiddenException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ClientForbiddenException(String message) {
        super(message, CommonConstants.EX_CLIENT_FORBIDDEN_CODE);
    }

}
