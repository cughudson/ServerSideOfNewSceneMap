package com.cscec.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ExecuteSh {
    private static Logger logger = LoggerFactory.getLogger(ExecuteSh.class);

    public static String execute(String shPath) {
        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shPath});
            process.waitFor();
            SequenceInputStream sis = new SequenceInputStream(process.getInputStream(), process.getErrorStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(sis, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line + "\r\n");
            }
            br.close();
            logger.info("执行结果：" + result.toString());
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            return null;
        }
        return result.toString();
    }
}
