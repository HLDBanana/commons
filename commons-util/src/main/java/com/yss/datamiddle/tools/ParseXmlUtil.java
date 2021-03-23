package com.yss.datamiddle.tools;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：固定解析数据中台-数据服务-配置的XML文件
 * 此工具类并不是通用的XML解析类
 *
 * @author wangqiang at 2019/8/30 15:36
 * @version 1.0.0
 */
@Slf4j
public final class ParseXmlUtil {

    /**
     * 模板文件格式
     */
    private static final String XML_FORMAT = ".xml";

    private static final String QUERY_CODE = "QUERY_CODE";
    private static final String QUERY_MESSAGE = "QUERY_MESSAGE";

    public static Map<String, String> parseXml(String filePath, String templateName, String elementName) {

        Map<String, String> map = new HashMap<>(4);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filePath + File.separator + templateName + XML_FORMAT));
            NodeList node = document.getElementsByTagName(elementName);
            Element element = (Element) node.item(0);
            map.put(QUERY_CODE, element.getElementsByTagName(QUERY_CODE).item(0)
                    .getFirstChild().getNextSibling().getTextContent()
                    .replaceAll("\r|\n", "").trim());
            map.put(QUERY_MESSAGE, element.getElementsByTagName(QUERY_MESSAGE).item(0)
                    .getFirstChild().getNextSibling().getTextContent().trim());
        } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
            log.error("解析错误", e);
        }
        return map;
    }
}
