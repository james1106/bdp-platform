package com.bdp.common.exception.auth;


import com.bdp.common.constant.CommonConstants;
import com.bdp.common.exception.BaseException;

public class TokenErrorException extends BaseException {
	private static final long serialVersionUID = 1L;

	public TokenErrorException(String message, int status) {
        super(message, CommonConstants.EX_TOKEN_ERROR_CODE);
    }
}
