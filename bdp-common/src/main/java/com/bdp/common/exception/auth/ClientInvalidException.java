package com.bdp.common.exception.auth;


import com.bdp.common.constant.CommonConstants;
import com.bdp.common.exception.BaseException;

public class ClientInvalidException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ClientInvalidException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
