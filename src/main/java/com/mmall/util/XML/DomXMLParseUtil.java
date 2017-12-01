package com.mmall.util.XML;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2017/12/1 14:16
 */
public class DomXMLParseUtil {

    public static void main(String[] args) {
        DomXMLParseUtil dom = new DomXMLParseUtil();
        //创建xml
        dom.createXML();
        //解析xml
        dom.parseXML();
    }

    public DocumentBuilder getDocumentBuilder() {
        //创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建一个newDocumentBuilder的对象
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return db;
    }

    public void createXML() {
        DocumentBuilder db = this.getDocumentBuilder();
        Document document = db.newDocument();
        document.setXmlStandalone(true);


        Element bookstore = document.createElement("bookStore");
        Element book = document.createElement("book");
        Element name = document.createElement("name");
        name.setTextContent("我是一本好书");
        book.appendChild(name);
        book.setAttribute("id", "1");
        bookstore.appendChild(book);
        document.appendChild(bookstore);


        TransformerFactory tff = TransformerFactory.newInstance();
        try {
            Transformer tf = tff.newTransformer();
            //产生换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(new File("src\\main\\java\\com\\mmall\\util\\XML\\book2.xml")));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void parseXML() {
        try {
            //通过DocumentBuilder 的parse方法加载xml
            DocumentBuilder db = this.getDocumentBuilder();
            Document document = db.parse("D:\\Project\\JAVA\\imooc\\mmall_learning\\src\\main\\java\\com\\mmall\\util\\book2.xml");
            //获取所有book节点的集合
            NodeList nodeList = document.getElementsByTagName("book");
            System.out.println("书店一共有书：" + nodeList.getLength() + "本！");
            //遍历每一个book节点
            for (int i = 0; i < nodeList.getLength(); i++) {

                //方式一：不知道book的属性的时候
                //通过 item(i) 方法 获取book节点，
                Node book = nodeList.item(i);
                //获取book节点的 所有 属性 集合
                NamedNodeMap attrs = book.getAttributes();
                System.out.println("第" + (i + 1) + "本书共有" + attrs.getLength() + "属性");
                for (int j = 0; j < attrs.getLength(); j++) {
                    //通过item(j)方法获取book节点某一个属性
                    Node attr = attrs.item(j);
                    System.out.print("属性名：" + attr.getNodeName());
                    System.out.println("----属性值：" + attr.getNodeValue());
                }

            /*    //方式二：前提条件book的属性有且只有一个确切的时候
                Element book = (Element) nodeList.item(i);
                String attrValue = book.getAttribute("id");
                System.out.println(attrValue);*/

                NodeList childNodes = book.getChildNodes();
                for (int k = 0; k < childNodes.getLength(); k++) {
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println(childNodes.item(k).getNodeName());
                        System.out.println(childNodes.item(k).getTextContent());
                        System.out.println(childNodes.item(k).getFirstChild().getNodeValue());
                        System.out.println(childNodes.item(k).getChildNodes().item(0).getTextContent());
                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}











































