
package com.example.demo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.base.BaseController;
import com.example.demo.system.response.GenericResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class FileController extends BaseController {

    @PostMapping(value = "/upload")
    public GenericResponse upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        try {
            Calendar cal = Calendar.getInstance();
            Integer year = cal.get(Calendar.YEAR);
            Integer month = cal.get(Calendar.MONTH)+1;
            Integer day = cal.get(Calendar.DAY_OF_MONTH);

            String destPath = savePath + File.separator + year + File.separator + month + File.separator + day + File.separator;
            String destUrl =  "/" + year + "/" + month + "/" + day + "/";

            logger.info("目标路径："+destPath);
            File destFile = new File(destPath);
            if(!destFile.exists()){
                logger.info("目标路径不存在，去创建");
                destFile.mkdirs();
            }

            //获取文件后缀
            String sourceFileName=file.getOriginalFilename();
            String suffix=sourceFileName.substring(sourceFileName.lastIndexOf("."),sourceFileName.length());
            logger.info("上传文件名称："+sourceFileName);

            //写入目的文件
            String destFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
            file.transferTo(new File(destPath + destFileName));
//            return destUrl + destFileName;
            JSONObject result=new JSONObject();
            result.put("url",destUrl + destFileName);
            result.put("originalFilename",file.getOriginalFilename());
            return success(result);
        }catch (Exception e){
            e.printStackTrace();
           throw  e;
        }
    }

    @RequestMapping(value = "/uploadStatus")
    public GenericResponse uploadStatus(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object percent = session.getAttribute("upload_percent_"+request.getParameter("id"));
        System.out.println("uploadStatus : upload_percent_"+request.getParameter("id")+":"+percent);
        return success(null != percent ? (Integer) percent : 0);
    }
}
