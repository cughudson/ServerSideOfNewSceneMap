package com.example.demo.system.config;

import com.example.demo.system.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
@Slf4j
public class FileUploadProgressListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String id=request.getHeader("id");
        log.info("setSession:"+id);
        session.setAttribute("upload_percent_"+id, 0);
        this.session = session;
    }

   long time=0;
    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        int percent = (int) (pBytesRead * 100.0 / pContentLength);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String id=request.getHeader("id");
        if(StringUtils.isEmpty(id)){
            id=request.getParameter("id");
        }
        session.setAttribute("upload_percent_"+id, percent);
        String  str=pItems+"更新：upload_percent_"+id+"::"+percent +"\t "+pBytesRead+"/"+pContentLength;
        log.info(str);
        long nowTime=new Date().getTime()/1000;
        if(nowTime != time){
            time=nowTime;
            log.info(str+"\t"+time);
        }
    }

}