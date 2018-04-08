package com.bdp.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bdp.common.constant.CommonConstants;
import com.bdp.common.exception.BaseException;
import com.bdp.common.msg.BaseResponse;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice("com.bdp")
@ResponseBody
public class GlobalExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BaseException.class)
	public BaseResponse baseExceptionHandler(HttpServletResponse response, BaseException ex) {
		logger.error(ex.getMessage(), ex);
		return new BaseResponse(ex.getStatus(), ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public BaseResponse otherExceptionHandler(HttpServletResponse response, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return new BaseResponse(CommonConstants.EX_OTHER_CODE, ex.getMessage());
	}
}
