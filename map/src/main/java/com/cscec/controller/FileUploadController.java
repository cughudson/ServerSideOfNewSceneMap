package com.cscec.controller;

import com.cscec.util.response.GenericResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@RestController
//@RequestMapping("/test")
public class FileUploadController extends  BaseController {

    private final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    /**
     * upload  上传文件
     */
//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @PostMapping(value = "/upload")
    public GenericResponse upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("update:start:"+ LocalDateTime.now());
        final HttpSession hs = request.getSession();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

//        String _name=request.getParameter("name");
//        String id=request.getParameter("id");
//        logger.info("name:"+_name+"\tid:"+id);
        if(!isMultipart){
            return error("不能为空");
        }
        // Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        //解决上传文件名的中文乱码
        upload.setHeaderEncoding("UTF-8");

        upload.setProgressListener(new ProgressListener(){
            public void update(long pBytesRead, long pContentLength, int pItems) {
                ProcessInfo pri = new ProcessInfo();
                pri.itemNum = pItems;
                pri.readSize = pBytesRead;
                pri.totalSize = pContentLength;
                pri.show = pBytesRead+"/"+pContentLength+" byte";
                pri.rate = Math.round(new Float(pBytesRead) / new Float(pContentLength)*100);
                hs.setAttribute("proInfo", pri);
            }
        });
        //     Parse the request
        List items = upload.parseRequest(request);
        //   Process the uploaded items
        Iterator iter = items.iterator();
        String result=null;
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField()) {
                String name = item.getFieldName();
                String value = item.getString("utf-8");
                System.out.println("this is common feild!"+name+"="+value);
            } else {
                System.out.println("this is file feild!");
                String fieldName = item.getFieldName();
                String fileName = item.getName().substring(item.getName().lastIndexOf("\\")+1);;
                String contentType = item.getContentType();
                boolean isInMemory = item.isInMemory();
                long sizeInBytes = item.getSize();
                result="D://temp/"+fileName;
                File uploadedFile = new File(result);
                item.write(uploadedFile);
            }
        }
        logger.info(result+"update:end:"+ LocalDateTime.now());
        return success(result);
    }


    /**
     * process 获取进度
     */
    @RequestMapping(value = "/uploadStatus")
    public  GenericResponse process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String _name=request.getParameter("name");
        String id=request.getParameter("id");
        logger.info("name:"+_name+"\tid:"+id);
        ProcessInfo processInfo=(ProcessInfo)request.getSession().getAttribute("proInfo");
        return success(processInfo);
    }
    //精度条pojo
    class ProcessInfo{
        public long totalSize = 1;
        public long readSize = 0;
        public String show = "";
        public int itemNum = 0;
        public int rate = 0;
    }
}