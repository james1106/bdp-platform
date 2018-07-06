package com.bdp.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bdp.common.exception.BaseException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice("com.bdp")
@ResponseBody
public class GlobalExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<?> baseExceptionHandler(HttpServletResponse response, BaseException ex) {
		logger.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> otherExceptionHandler(HttpServletResponse response, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	}
}
