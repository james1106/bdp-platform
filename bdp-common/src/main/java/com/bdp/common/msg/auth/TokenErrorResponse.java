package com.bdp.common.msg.auth;

import com.bdp.common.constant.RestCodeConstants;
import com.bdp.common.msg.BaseResponse;

public class TokenErrorResponse extends BaseResponse {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
