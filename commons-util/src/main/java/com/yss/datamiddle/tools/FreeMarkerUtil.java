package com.yss.datamiddle.tools;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

/**
 * 描述：
 *
 * @author wangxincheng at 2019/9/2 15:11
 */
@Slf4j
public final class FreeMarkerUtil {

    /**
     * 模板文件格式
     */
    private static final String FTL_FORMAT = ".ftl";

    public static String executeStringFromXml(Map paramMap, String templateStr) {
        StringWriter stringWriter = new StringWriter();
        Template template;
        try {
            template = new Template("DataService", templateStr, configuration());
            template.process(paramMap, stringWriter);
        } catch (TemplateException | IOException e) {
            log.error("解析文件异常", e);
        }
        return stringWriter.toString();
    }

    private static Configuration configuration() {
        Configuration configuration = new Configuration(Configuration.getVersion());
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }

    /**
     * 匹配模板后生成字符串返回
     *
     * @param paramMap     参数
     * @param templatePath 模板地址
     * @param templateName 模板名称
     * @return 解析后的字符串
     */
    public static String executeString(Map paramMap, String templatePath, String templateName) {
        String resultString = null;
        try {
            // 获取模版
            Template template = getTemplate(templatePath, templateName);
            if (!Objects.isNull(template)) {
                // 创建一个StringWriter对象
                StringWriter writer = new StringWriter();
                // 匹配
                template.process(paramMap, writer);
                resultString = writer.toString();
                // 关闭流
                writer.close();
            }
        } catch (Exception e) {
            log.error("FreeMarkerUtil -> getResultString -> 生成字符串失败! paramMap:{}, templatePath:{}, templateName:{}",
                    paramMap, templatePath, templateName, e);
        }
        return resultString;

    }

    /**
     * 获取模板对象
     *
     * @param templatePath
     * @param templateName
     * @return
     * @throws Exception
     */
    private static Template getTemplate(String templatePath, String templateName) throws Exception {
        File templateDir;
        if (validTemplate(templatePath, templateName) && (templateDir = new File(templatePath)).exists()) {
            // 创建Configuration实例，指定版本
            Configuration configuration = new Configuration(Configuration.getVersion());
            // 指定configuration对象模板文件存放的路径
            configuration.setDirectoryForTemplateLoading(templateDir);
            // 设置config的默认字符集，一般是UTF-8
            configuration.setDefaultEncoding("UTF-8");
            // 设置错误控制器
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            // 获取模版
            return configuration.getTemplate(templateName);
        }
        return null;
    }

    /**
     * 校验模板数据
     *
     * @param templatePath 模板文件路径
     * @param templateName 模板名
     * @return boolean 是否校验通过
     */
    private static boolean validTemplate(String templatePath, String templateName) {
        return !StringUtils.isEmpty(templatePath) && !StringUtils.isEmpty(templateName)
                && templateName.contains(FTL_FORMAT);
    }
}
