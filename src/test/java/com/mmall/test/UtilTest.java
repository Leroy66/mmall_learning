package com.mmall.test;

import com.alibaba.fastjson.JSON;
import com.mmall.util.XMLHelperUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static com.mmall.util.XMLHelperUtil.*;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2018/4/10 10:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UtilTest {

    @Test
    public void test() {
        String str="<xml><appid>11</appid><mch_id>22</mch_id><body>Popon-充值</body></xml>";
        XMLHelperUtil xx = XMLHelperUtil.of(str);
        System.out.println("zzzzzzzzz"+JSON.toJSONString(xx.toMap()));
    }
    @Test
    public void test001(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", "11");
        params.put("mch_id", "22");
        params.put("body", "Popon-充值");
        String res = toXml(params);
        System.out.println(res);
    }

}
