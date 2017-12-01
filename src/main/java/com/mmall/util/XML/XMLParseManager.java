package com.mmall.util.XML;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2017/12/1 10:54
 */


import com.mmall.common.ServerResponse;
import com.mmall.service.impl.CategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

public class XMLParseManager {
    private XMLParseUtil xmlParser;
    private Logger logger = LoggerFactory.getLogger(XMLParseManager.class);

    public XMLParseManager() {
        try {
            xmlParser = new XMLParseUtil();
        } catch (ParserConfigurationException e) {
            logger.info("XMLParseManager@XMLParseManager(): " + "Failed to create XML parser!" + e);
        }
    }

    /**
     * 初始化方法，通过文件对象初始化XML解析器和文档对象
     *
     * @param file
     * @see [类、类#方法、类#成员]
     */
    private Document getDocument(File file) {
        Document document = null;
        try {
            document = xmlParser.parseDocument(file);
        } catch (IllegalArgumentException e) {
            logger.info("XMLParseManager@getDocument: " + "An illegal or inappropriate argument!" + e);
        } catch (SAXParseException e) {
            logger.info("XMLParseManager@getDocument: " + "XML file error, can not parse!" + e);
        } catch (SAXException e) {
            logger.info("XMLParseManager@getDocument: " + "There is a SAXException！" + e);
        } catch (IOException e) {
            logger.info("XMLParseManager@getDocument: " + "There is an IOException!" + e);
        }

        return document;
    }

    /**
     * 获取节点的值
     *
     * @return nodeValue
     * @see [类、类#方法、类#成员]
     */
    private String getNodeValue(Node node, String xpath) {
        String nodeValue = null;
        try {
            nodeValue = xmlParser.getNodeStringValue(node, xpath);
        } catch (XPathExpressionException e) {
            logger.info("XMLParseManager@getNodeValue: " + "XPath expression [" + xpath + "] error！" + e);
        }
        return nodeValue;
    }

    /**
     * 根据作者姓名获取书籍
     *
     * @param file XML文件对象
     * @param name 书籍作者姓名
     * @return myBook
     * @see [类、类#方法、类#成员]
     */
    public Book getBookByAuthor(File file, String name) {
        Book myBook = null;

        if (null != file) {
            Document doc = getDocument(file);
            if (null != doc) {
                /*
                 * [author='" + name + "'] 表示只取author为name参数值的book节点
                 */
                String title = getNodeValue(doc, "//book[author='" + name + "']/title");
                String lang = getNodeValue(doc, "//book[author='" + name + "']/title/@lang");
                String author = getNodeValue(doc, "//book[author='" + name + "']/author");
                String year = getNodeValue(doc, "//book[author='" + name + "']/year");
                String price = getNodeValue(doc, "//book[author='" + name + "']/price");
                myBook = new Book(lang, title, author, year, price);
            }
        } else {
            logger.info("XMLParseManager@getBookByAuthor: " + "File is null!");
        }

        return myBook;
    }

}
