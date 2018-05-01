package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.github.pagehelper.PageInfo;


/**
 * Created by geely
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    // 申请升级
    ServerResponse<String> userUpgrade(Integer id, User user);

    // 用户降级
    ServerResponse<String> userDowngrade(Integer userId);

    // 用户降级列表
    ServerResponse<PageInfo> getUserListToDown(int pageNum, int pageSize);

    // 审核升级
    ServerResponse<PageInfo> getUserList(int pageNum, int pageSize);

    // 18-05-01 注销用户
    ServerResponse<String> userDelete(Integer userId);
    // role为4的列表
    ServerResponse<PageInfo> getNormalUserList(int pageNum, int pageSize);
    // 18-05-01 end

    // 18-04-03
    ServerResponse<PageInfo> getPifaList(int pageNum, int pageSize);

    ServerResponse<PageInfo> getShitiList(int pageNum, int pageSize);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse checkAdminRole(User user);

    ServerResponse checkAdminRoleTest(User user);

    // 审核通过
    ServerResponse<String> setUserPass(Integer userId, String role, Integer status);

    // 0120
    ServerResponse<User> getShopOwner(String username);

}
