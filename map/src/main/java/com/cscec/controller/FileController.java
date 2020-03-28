
package com.cscec.controller;

import com.alibaba.fastjson.JSONObject;
import com.cscec.util.Constant;
import com.cscec.util.response.ErrorCode;
import com.cscec.util.response.GenericResponse;
import com.cscec.util.response.ResponseFormat;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
@Api(tags = {"文件文件上传"})
@RestController
public class FileController extends BaseController {

    @PostMapping(value = "/upload")
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = Constant.id, value = "随机id")
    })
    public GenericResponse upload(MultipartFile file) throws IOException {
        String id=request.getParameter("id");
        if(file==null || StringUtils.isEmpty(id)){
            return ResponseFormat.error(ErrorCode.PARAM_ERROR,"file 和id 不能为空");
        }
        try {
            Calendar cal = Calendar.getInstance();
            Integer year = cal.get(Calendar.YEAR);
            Integer month = cal.get(Calendar.MONTH)+1;
            Integer day = cal.get(Calendar.DAY_OF_MONTH);

            String destPath = savePath + File.separator + year + File.separator + month + File.separator + day + File.separator;
            String destUrl =  "/imagefolder/" + year + "/" + month + "/" + day + "/";

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
            JSONObject result=new JSONObject();
            result.put("url",destUrl + destFileName);
            result.put("originName",sourceFileName);
            return ResponseFormat.success(result);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(value = "/uploadStatus")
    @ApiOperation(value = "获取文件上传进度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.id, value = "随机id")
    })
    public Integer uploadStatus(HttpServletRequest request,String id){
        HttpSession session = request.getSession();
//        String fileId=request.getParameter("id");
        Object percent = session.getAttribute("upload_file_"+id);
        logger.info("uploadStatus : upload_file_"+id+":"+percent);
        return null != percent ? (Integer) percent : 0;
    }
}
