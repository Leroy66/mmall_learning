package com.mmall.service;

import com.mmall.common.ServerResponse;

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
}
