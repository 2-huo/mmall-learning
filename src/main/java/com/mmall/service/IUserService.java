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
    // 审核升级
    ServerResponse<PageInfo> getUserList(int pageNum, int pageSize);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse checkAdminRole(User user);

    ServerResponse checkAdminRoleTest(User user);
}
