package com.mmall.controller.backend;


import com.mmall.common.Const;
import com.mmall.common.ResponsesCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;


    /**
     * 添加商品分类
     *
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            //是管理员   //增加分类的逻辑
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }

    /**
     * 修改商品分类的名字
     *
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            //是管理员   //增加分类的逻辑
            return iCategoryService.setCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }

    /**
     * 获取品类子节点(平级)
     *
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            //查询子节点的category信息，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }


    /**
     * 获取当前分类id及递归子节点
     *
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponsesCode.NEED_LOGIN.getCode(), "用户未登陆，请登陆");
        }
        //校验一下是不是管理员
        ServerResponse adminRole = iUserService.checkAdminRole(user);
        if (adminRole.isSuccess()) {
            //查询当前结点的子id和递归子节点的子id
            return iCategoryService.selectCategoryAndChildById(categoryId);
        } else {
            return ServerResponse.createByErrorMessagr("无权限操作，需要管理员权限！");
        }
    }


}
