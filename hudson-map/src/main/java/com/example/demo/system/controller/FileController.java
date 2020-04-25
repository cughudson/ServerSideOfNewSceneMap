
package com.example.demo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.base.BaseController;
import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.MyException;
import com.example.demo.system.response.ResponseFormat;
import com.example.demo.system.util.MD5Util;
import com.example.demo.system.util.StringUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangxg
 * @since 2020-03-31 23:05:31
 */
@RestController
public class FileController extends BaseController {

    @Value("${spring.profiles.active}")
    private String active;

    @PostMapping(value = "/upload")
    public GenericResponse upload(MultipartFile file) throws IOException {
        if (file == null) {
            throw new MyException(ResponseFormat.error(ErrorCode.PARAM_ERROR, "文件不能为空"));
        }
        try {
            Calendar cal = Calendar.getInstance();
            Integer year = cal.get(Calendar.YEAR);
            Integer month = cal.get(Calendar.MONTH) + 1;
            Integer day = cal.get(Calendar.DAY_OF_MONTH);

            //String destPath = savePath ;//+ File.separator + year + File.separator + month + File.separator + day + File.separator;
            //String destUrl =  "/" + year + "/" + month + "/" + day + "/";

            String destPath = savePath + "/image_folder/";
            String destUrl = "/";
            logger.info("目标路径：" + destPath);
            File destFile = new File(destPath);
            if (!destFile.exists()) {
                logger.info("目标路径不存在，去创建");
                destFile.mkdirs();
            }

            //获取文件后缀
            String sourceFileName = file.getOriginalFilename();
            String suffix = sourceFileName.substring(sourceFileName.lastIndexOf("."), sourceFileName.length());
            logger.info("上传文件名称：" + sourceFileName);
            String fileId = MD5Util.getMd5(file.getBytes());
            if ("test".equals(active)) {//TODO 测试用
                fileId = UUID.randomUUID().toString().replaceAll("-", "");
            }
            String destFileName = fileId + suffix;
            File newFile = new File(destPath + destFileName);
            if (newFile.exists()) {
                logger.info("已经存在相同的文件:" + destFileName);
                throw new MyException(ResponseFormat.error(ErrorCode.DATA_EXISTS, "已经存在相同的文件"));
            }
            file.transferTo(newFile);
            JSONObject result = new JSONObject();

            BufferedImage srcImage = ImageIO.read(newFile);
            int imageWidth = srcImage.getWidth();
            if (imageWidth > 2048) {// 压缩成不同尺寸的大小
                processImg(2048, destPath, fileId, newFile, suffix, destUrl, result);
                processImg(300, destPath, fileId, newFile, suffix, destUrl, result);
                processImg(32, destPath, fileId, newFile, suffix, destUrl, result);
            }
            result.put("url", imgFolder + destUrl + destFileName);
            result.put("originalFilename", file.getOriginalFilename());
            result.put("id", MD5Util.getMd5(newFile));
            request.getSession().removeAttribute("upload_percent_" + getId());
            return success(result);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            throw e;
        }
    }

    private void processImg(int maxWidth, String destPath, String fileId, File newFile, String suffix, String destUrl, JSONObject result) {
        try {
            BufferedImage srcImage = ImageIO.read(newFile);
            int imageHeight = srcImage.getHeight();
            int imageWidth = srcImage.getWidth();
            int height = new BigDecimal(imageHeight).divide(new BigDecimal(imageWidth).divide(new BigDecimal(maxWidth), 4, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP).intValue();//这里不通过压缩比来计算 高度 是因为图片没有严格的长宽比，计算出来的会有个1个像素点的误差
            File outFilePath = new File(destPath + fileId + "_" + maxWidth + suffix);
            Thumbnails.of(newFile).size(maxWidth, height).toFile(outFilePath);
            result.put("url_" + maxWidth, imgFolder + destUrl + fileId + "_" + maxWidth + suffix);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public String getId() {
        String id = request.getHeader("id");
        if (StringUtils.isEmpty(id)) {
            id = request.getParameter("id");
        }
        return id;
    }

    @RequestMapping(value = "/uploadStatus")
    public GenericResponse uploadStatus(HttpServletRequest request) {
        Object percent = request.getSession().getAttribute("upload_percent_" + getId());
        logger.info("uploadStatus : upload_percent_" + getId() + ":" + percent);
        return success(null != percent ? (Integer) percent : 0);
    }
}
