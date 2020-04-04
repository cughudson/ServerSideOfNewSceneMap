package com.example.demo.system.config;

import com.example.demo.system.util.StringUtils;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
public class FileUploadProgressListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String id=request.getHeader("id");
        session.setAttribute("upload_percent_"+id, 0);
        this.session = session;
//        System.out.println("设置 : upload_percent_"+request.getParameter("id"));
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
        String str=pItems+"更新：upload_percent_"+id+"::"+percent +"\t "+pBytesRead+"/"+pContentLength;
        long nowTime=new Date().getTime()/1000;
        if(nowTime != time){
            System.out.println(str);
            time=nowTime;
        }
    }

}