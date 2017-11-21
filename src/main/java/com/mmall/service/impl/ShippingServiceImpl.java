package com.mmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        //insert被修改,返回影响行数，id会返回到shipping里面
        int rowCount = shippingMapper.insert(shipping);

        // System.out.println("----rowCount-----" + rowCount);
        //    1
        // System.out.println("----shipping-----" + JSON.toJSONString(shipping));
        //----shipping-----{"id":34,"receiverAddress":"杨浦区","receiverCity":"上海","receiverMobile":"11111111","receiverName":"dadada","receiverPhone":"1212121","receiverProvince":"上海","receiverZip":"11111","userId":1}

        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功！", result);
        }
        return ServerResponse.createByErrorMessagr("新建地址失败！");
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //横向越权，下面这个方法不能用
        //int resultCount = shippingMapper.deleteByPrimaryKey(shippingId);
        int rowCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("删除地址成功！");
        }
        return ServerResponse.createByErrorMessagr("删除地址失败！");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功！");
        }
        return ServerResponse.createByErrorMessagr("更新地址失败！");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessagr("无法查询到该地址！");
        }
        return ServerResponse.createBySuccess("查询成功！", shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
