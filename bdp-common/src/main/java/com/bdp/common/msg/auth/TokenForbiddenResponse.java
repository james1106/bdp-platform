package com.bdp.common.msg.auth;

import com.bdp.common.constant.RestCodeConstants;
import com.bdp.common.msg.BaseResponse;

public class TokenForbiddenResponse  extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
