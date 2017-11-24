package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponsesCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2017/11/24 11:54
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;


    /**
     * 订单列表页
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            return iOrderService.manageOrderList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }


    /**
     * 订单详情
     *
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            return iOrderService.manageOrderDetail(orderNo);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }


    /**
     * 查询（兼容模糊查询---使用分页）
     *
     * @param session
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            return iOrderService.manageOrderSearch(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }


    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            return iOrderService.manageSendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }


}
