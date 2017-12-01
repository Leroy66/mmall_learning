package com.mmall.util.XML;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2017/12/1 10:47
 */

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解析XML的工具类，使用XPath
 *
 * @author ChenFeng
 * @version [版本号, 2009-12-22]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class XMLParseUtil {
    private DocumentBuilder builder;

    private XPath xpath;

    /**
     * 默认构造函数
     *
     * @throws ParserConfigurationException 创建XML解析器出错！
     */
    public XMLParseUtil() throws ParserConfigurationException {
        DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
        builder = domfactory.newDocumentBuilder();

        XPathFactory xpfactory = XPathFactory.newInstance();
        xpath = xpfactory.newXPath();
    }

    /**
     * 根据路径解析XML文档
     * <p>
     * <pre>
     * 可能产生而没有显式抛出的异常：
     * 1) MalformedURLException ：传入文件路径错误！找不到要解析的文件！
     * 2) SAXParseException ： 文件格式错误！无法解析！
     * </pre>
     *
     * @param path 文件路径
     * @return Document对象
     * @throws IOException  IO异常
     * @throws SAXException SAX异常
     * @see [类、类#方法、类#成员]
     */
    public Document parseDocument(String path) throws IOException, SAXException {
        return builder.parse(path);
    }

    /**
     * 根据文件解析XML文档
     * <p>
     * <pre>
     * 可能产生而没有显式抛出的异常：
     * 1) IllegalArgumentException ：传入参数错误！如对象不能为空！
     * 2) SAXParseException ： 文件格式错误！无法解析！
     * </pre>
     *
     * @param file 文件
     * @return Document对象
     * @throws IOException  IO异常
     * @throws SAXException SAX异常
     * @see [类、类#方法、类#成员]
     */
    public Document parseDocument(File file) throws IOException, SAXException {
        return builder.parse(file);
    }

    /**
     * 根据输入流解析XML文档
     * <p>
     * <pre>
     * 可能产生而没有显式抛出的异常：
     * 1) IllegalArgumentException ：传入参数错误！如对象不能为空！
     * 2) SAXParseException ： 文件格式错误！无法解析！
     * </pre>
     *
     * @param is 输入流
     * @return Document对象
     * @throws IOException  IO异常
     * @throws SAXException SAX异常
     * @see [类、类#方法、类#成员]
     */
    public Document parseDocument(InputStream is) throws IOException, SAXException {
        return builder.parse(is);
    }

    /**
     * 通过xpath取得节点列表
     *
     * @param node       节点
     * @param expression XPath表达式
     * @return NodeList
     * @throws XPathExpressionException XPath表达式异常
     * @see [类、类#方法、类#成员]
     */
    public NodeList selectNodes(Node node, String expression) throws XPathExpressionException {
        // XPath对象编译XPath表达式
        XPathExpression xpexpreesion = this.xpath.compile(expression);
        Object object = xpexpreesion.evaluate(node, XPathConstants.NODESET);
        return (NodeList) object;
    }

    /**
     * 通过xpath取得单个节点
     *
     * @param node       节点
     * @param expression XPath表达式
     * @return Node
     * @throws XPathExpressionException XPath表达式异常
     * @see [类、类#方法、类#成员]
     */
    public Node selectSingleNode(Node node, String expression) throws XPathExpressionException {
        XPathExpression xpexpreesion = this.xpath.compile(expression);
        Object object = xpexpreesion.evaluate(node, XPathConstants.NODE);
        return (Node) object;
    }

    /**
     * 根据xpath取得节点的文本值
     *
     * @param node       节点
     * @param expression XPath表达式
     * @return String
     * @throws XPathExpressionException XPath表达式异常
     * @see [类、类#方法、类#成员]
     */
    public String getNodeStringValue(Node node, String expression) throws XPathExpressionException {
        XPathExpression xpexpreesion = this.xpath.compile(expression);
        Object object = xpexpreesion.evaluate(node, XPathConstants.STRING);
        return (String) object;
    }
}
