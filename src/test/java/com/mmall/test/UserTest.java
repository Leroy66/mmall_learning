package com.mmall.test;

import com.alibaba.fastjson.JSON;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import junit.runner.BaseTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2017/12/4 11:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserTest{

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        User xx = userMapper.selectByPrimaryKey(1);
        System.out.println(JSON.toJSONString(xx));
    }

}
