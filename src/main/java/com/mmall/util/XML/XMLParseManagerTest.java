package com.mmall.util.XML;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2017/12/1 10:54
 */
import java.io.File;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XMLParseManagerTest {
    XMLParseManager xmlMgr;

    @Before
    public void setUp() throws Exception {
        xmlMgr = new XMLParseManager();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetMyBook() {
        String path ="D:\\Project\\JAVA\\imooc\\mmall_learning\\src\\main\\java\\com\\mmall\\util\\XML\\Book.xml";
        File xmlfile = new File(path);
        Book actualBook = xmlMgr.getBookByAuthor(xmlfile, "ChenFeng");
        System.out.println(JSON.toJSONString(actualBook));

        Assert.assertNotNull(actualBook);

        Book expectedBook = new Book("zh", "哲学：心灵，宇宙", "ChenFeng", 2010, 96.0);
        Assert.assertEquals(expectedBook, actualBook);
    }
}
