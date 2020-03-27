package com.cscec.conf;

import com.cscec.util.response.ErrorCode;
import com.cscec.util.response.GenericResponse;
import com.cscec.util.response.MyException;
import com.cscec.util.response.ResponseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public  GenericResponse bizExceptionHandler(MyException my){
        logger.error("发生业务异常！原因是：{}",my.getMessage());
        return my.getResponse();
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public GenericResponse exceptionHandler( Exception e){
        logger.error("未知异常！原因是:",e);
        return ResponseFormat.error(ErrorCode.UNKONW_ERROR,e.getMessage(),e);
    }
}