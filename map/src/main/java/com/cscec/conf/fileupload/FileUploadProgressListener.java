package com.cscec.conf.fileupload;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
public class FileUploadProgressListener implements ProgressListener {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    private HttpSession session;

    //TODO 这块改造成  通过url  传输id    比如 :  /upload/xxx 获取的时候直接截取后面所有的
    public void setSession(HttpSession session) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logger.info("设置 : setSession"+request.getRequestURI());
        this.session = session;
        session.setAttribute("upload_file_"+request.getParameter("id"), 0);
        logger.info("设置 : upload_file_"+request.getParameter("id"));
    }

   long time=0;
    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        int percent = (int) (pBytesRead * 100.0 / pContentLength);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        session.setAttribute("upload_file_"+request.getParameter("id"), percent);
        String str=pItems+"更新：upload_file_"+request.getParameter("id")+"::"+percent +"\t "+pBytesRead+"/"+pContentLength;
        long nowTime=new Date().getTime()/1000;
        logger.info(str);
//        if(nowTime -5> time){
//            time=nowTime;
//            logger.info(str);
//        }

    }

}