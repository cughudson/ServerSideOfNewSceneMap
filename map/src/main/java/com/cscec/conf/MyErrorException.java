package com.cscec.conf;

import com.cscec.util.response.ErrorCode;
import com.cscec.util.response.ResponseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 报错实现错误代码的处理
 */
@ApiIgnore
@Controller
public class MyErrorException implements ErrorController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 异常的分别处理--------------------------------》》》》》》》》》》》》
     */
    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        logger.info("statusCode;" + statusCode);
        if (statusCode != 404) {
            statusCode = 500;
        }
        if("POST".equals(request.getMethod())){
            response.getWriter().print(ResponseFormat.error(statusCode==404?ErrorCode.NOT_FOUND:ErrorCode.UNKONW_ERROR,""+statusCode));
            return;
        }
        response.sendRedirect("/" + statusCode);
    }

    @Override
    public String getErrorPath() { // 没有实际用途 必须要重写
        return null;
    }
}