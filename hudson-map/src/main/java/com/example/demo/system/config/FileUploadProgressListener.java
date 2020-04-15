package com.example.demo.system.config;

import com.example.demo.system.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author zhangxg
 * @since 2020-03-31 23:05:31
 */
@Component
@Slf4j
public class FileUploadProgressListener implements ProgressListener {

  private HttpSession session;

  public void setSession(HttpSession session) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String id = request.getHeader("id");
    log.info("setSession:" + id);
    session.setAttribute("upload_percent_" + id, 0);
    this.session = session;
  }

  long time = 0;

  @Override
  public void update(long bytesRead, long contentLength, int items) {
    int percent = (int) (bytesRead * 100.0 / contentLength);
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String id = request.getHeader("id");
    if (StringUtils.isEmpty(id)) {
      id = request.getParameter("id");
    }
    session.setAttribute("upload_percent_" + id, percent);
    String str = items + "更新：upload_percent_" + id + "::" + percent + "\t " + bytesRead + "/" + contentLength;
    log.info(str);
    long nowTime = System.currentTimeMillis() / 1000;
    if (nowTime != time) {
      time = nowTime;
      log.info(str + "\t" + time);
    }
  }

}