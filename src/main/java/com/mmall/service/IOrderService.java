package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2017/11/22 18:12
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer id, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Long orderNo, Integer userId);

    ServerResponse create(Integer shippingId, Integer userId);

    ServerResponse cancel(Long orderId, Integer useId);

    ServerResponse getOrderCartProduct(Integer useId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo> manageOrderList(int pageNum, int pageSize);

    ServerResponse manageOrderDetail(Long orderNo);

    ServerResponse<PageInfo> manageOrderSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);
}
