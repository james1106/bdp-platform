package com.bdp.common.exception.auth;


import com.bdp.common.constant.CommonConstants;
import com.bdp.common.exception.BaseException;

public class UserInvalidException extends BaseException {
	private static final long serialVersionUID = 1L;

	public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
