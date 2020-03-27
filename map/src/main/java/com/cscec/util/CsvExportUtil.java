package com.cscec.util;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * 导出csv文件的工具类
 * @author : youxiaowei
 * @date : 2019/7/29 11:09
 */
public class CsvExportUtil {

     private static Logger logger= LoggerFactory.getLogger(CsvExportUtil.class);

    /**
     * 导出csv文件的outputStream
     * @param list 要写入csv的集合
     * @param headerEnum csv文件的头部，
     * @param attributeSet 要提取的属性的属性名
     * @return outputStream
     */
    public static   void export(List<JSONObject> list, Class<? extends Enum<?>> headerEnum, String[] attributeSet, HttpServletRequest request, HttpServletResponse response,String fileName) throws IOException {
        OutputStream outputStream=response.getOutputStream();
        FileUtil.downloadFileHttpHeaderSetting(request.getHeader("User-Agent").toLowerCase(), response, fileName);

        String tempFileName = UUID.randomUUID().toString().replaceAll("-", ""); //防止临时文件名冲突
        try {
            CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(headerEnum);
            File tempFile = File.createTempFile(tempFileName, ".csv");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(fileOutputStream, "GBK"), csvFormat);
            for(JSONObject json:list){
                String[] values = getAttributesToStringArray(json,attributeSet);
                csvPrinter.printRecord(values);
            }
            csvPrinter.flush();
            csvPrinter.close();
            fileOutputStream.close();
            FileInputStream fis = new FileInputStream(tempFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            tempFile.delete(); //删除临时文件
            outputStream.write(bos.toByteArray());
        } catch (IOException e) {
            logger.info(e.getMessage(),e);
        }
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 逐个解析list中的属于，按行写入
     * @param t
     * @param attributeSet
     * @return
     */
    public static  String[] getAttributesToStringArray(JSONObject t,String[] attributeSet) {
        String[] values = new String[attributeSet.length];
        for(int i=0;i<attributeSet.length;i++){
            String name = attributeSet[i];
            try {
                values[i] =t.getString(name);
            }catch (Exception e){
                values[i] = "";
                logger.info("获取属性时出错!!!"+name);
                e.printStackTrace();
            }
        }

        return values;
    }

}
