package com.yss.datamiddle.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @description: file工具类
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@Slf4j
public final class FileUtil {

    /**
     * 文件转字节数组
     *
     * @param filePath
     * @return
     */
    public static byte[] transformByteArray(String filePath) {
        File file = null;

        if (StringUtils.isEmpty(filePath) || !(file = new File(filePath)).exists()) {
            return null;
        }

        return transformByteArray(file);
    }

    /**
     * 文件转字节数组
     *
     * @param file
     * @return
     */
    public static byte[] transformByteArray(File file) {

        byte[] buffer = null;
        ByteArrayOutputStream bos = null;

        try (FileInputStream fis = new FileInputStream(file)) {

            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int n;

            while (-1 != (n = fis.read(b))) {
                bos.write(b, 0, n);
            }

            bos.close();

            buffer = bos.toByteArray();
        } catch (Exception e) {
            log.info("文件流转字节数组异常!", e);
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("FileUtil-->ByteArrayOutputStream-->close异常!", e);
                }
            }
        }

        return buffer;
    }

    /**
     * 上传文件
     *
     * @param file
     * @param filePath 路径
     * @param fileName 文件名，如有后缀，带后缀
     */
    public static void uploading(InputStream file, String filePath, String fileName) throws Exception {
        Objects.requireNonNull(file, "上传文件为null!");

        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileName)) {
            throw new Exception(String.format("上传参数缺失! filePath:%s, fileName:%s", filePath, fileName));
        }

        Files.copy(file, Paths.get(filePath).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 描述：文件下载
     *
     * @param response
     * @param inputStream
     * @param fileName
     * @return void
     * @author fangzhao at 2020/6/5 16:32
     */
    public static void download(HttpServletResponse response, InputStream inputStream, String fileName) {

        try (OutputStream out = response.getOutputStream()) {
            // 设置响应消息头，告诉浏览器当前响应是一个下载文件
            response.setContentType("application/x-msdownload");
            // 告诉浏览器，当前响应数据要求用户干预保存到文件中，以及文件名是什么 如果文件名有中文，必须URL编码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            // 写文件
            int b;
            while (-1 != (b = inputStream.read())) {
                out.write(b);
            }
        } catch (Exception e) {
            log.error("下载失败：" + e.getMessage(), e);
        }
    }
}
