
package com.example.demo.system.config;

import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.MyException;
import com.example.demo.system.response.ResponseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({MyException.class})
    @ResponseBody
    public GenericResponse bizExceptionHandler(MyException my) {
        GenericResponse response = my.getResponse();
        logger.error("发生业务异常！原因是： {}", my.getResponse());
        return ResponseFormat.error(response.getCode(), response.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public GenericResponse exceptionHandler(Exception e) {
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return null;
        }
        logger.error("未知异常！原因是:", e);
        return ResponseFormat.error(ErrorCode.UNKNOWN_ERROR, e.getMessage(), e);
    }
}
