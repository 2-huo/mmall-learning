package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.ProductMapper;
import com.mmall.dao.ShopMapper;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.Shop;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.vo.UserListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by geely
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user){
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        // 用户初始身份
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> userUpgrade(Integer id, User user) {
        User originUser = userMapper.selectByPrimaryKey(id);
//         新
        if (originUser.getRole().equals(Const.Role.ROLE_CUSTOMER)) {
            originUser.setName(user.getName());
            originUser.setPhone(user.getPhone());
            originUser.setProvince(user.getProvince());
            originUser.setCity(user.getCity());
            originUser.setDistrict(user.getDistrict());
            originUser.setAddr(user.getAddr());
            originUser.setLvl(user.getLvl());
            originUser.setRole(user.getRole());
            originUser.setShopname(user.getShopname());
            Shop shop = new Shop();
            shop.setShopname(user.getShopname());
            shop.setUsername(user.getUsername());
            int shopCount = shopMapper.insert(shop);
            Shop shop2 = shopMapper.selectByShopnameUsername(originUser.getShopname(), originUser.getUsername());
            originUser.setShopId(shop2.getId());
        } else {
            originUser.setLvl(user.getLvl());
            originUser.setRole(user.getRole());
        }
        // 0116 end
        if(originUser.getRole().equals(Const.Role.ROLE_PIFA)||originUser.getRole().equals(Const.Role.ROLE_ST)) {
            User userTwice = userMapper.selectCheckByPrimaryKey(id);
            if(userTwice != null) {
                return ServerResponse.createByErrorMessage("请不要重复申请");
            }
            int upgradeCount = userMapper.insertCheck(originUser);
            if(upgradeCount == 0) {
                return ServerResponse.createByErrorMessage("申请失败");
            }
            return ServerResponse.createBySuccessMessage("申请成功，请耐心等待审核！");
        }
        return ServerResponse.createByErrorMessage("申请失败");
    }

    // 用户降级 降为普通用户
    @Override
    public ServerResponse<String> userDowngrade(Integer userId) {
        if(userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (userId==1) {
            return ServerResponse.createByErrorMessage("降级失败, 不能降级管理员!");
        }

        User originUser = userMapper.selectByPrimaryKey(userId);
        if (originUser != null) {
            originUser.setName(null);
            originUser.setPhone(null);
            originUser.setProvince(null);
            originUser.setCity(null);
            originUser.setDistrict(null);
            originUser.setAddr(null);
            originUser.setLvl(null);
            originUser.setRole(Const.Role.ROLE_CUSTOMER);
            originUser.setAnswer(null);
//            删掉店铺
            shopMapper.deleteByAdmin(originUser.getShopId());
//            删掉商品
            productMapper.deleteByShopname(originUser.getShopname());
            originUser.setShopname(null);
            originUser.setShopId(null);
            userMapper.updateByPrimaryKey(originUser);
            return ServerResponse.createBySuccessMessage("降级成功!!");
        } else {
            // 找不到id
            return ServerResponse.createByErrorMessage("操作失败!");
        }
    }


    // 用户/会员 注销
    @Override
    public ServerResponse<String> userDelete(Integer userId) {
        if(userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (userId==1) {
            return ServerResponse.createByErrorMessage("注销失败, 不能降级管理员!");
        }
        int deleteCount = userMapper.deleteByPrimaryKey(userId);
        if (deleteCount > 0) {
            return ServerResponse.createBySuccessMessage("注销成功!!");
        } else {
            return ServerResponse.createByErrorMessage("操作失败!");
        }
    }

    // 0515 头像上传
    @Override
    public ServerResponse<User> saveAvatar(User user, String image) {
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if(StringUtils.isNotBlank(image)){
            String[] subImageArray = image.split(",");
            if(subImageArray.length > 0){
                user.setAvatar(subImageArray[0]);
            }
            return ServerResponse.createBySuccessMessage("上传成功!!");
        } else {
            // 找不到id
            return ServerResponse.createByErrorMessage("操作失败!");
        }
    }

    // 审核通过
    @Override
    public ServerResponse<String> setUserPass(Integer userId, String role, Integer status) {
        if(userId == null || role == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        // status 1代表审核通过 2代表审核失败, 直接删去表数据
        // user表
        User originUser = userMapper.selectByPrimaryKey(userId);
        if((role.equals(Const.Role.ROLE_PIFA)||role.equals(Const.Role.ROLE_ST)) && status == 1) {
            // user_check表
            User user = userMapper.selectCheckByPrimaryKey(userId);
            if(user == null) {
                return ServerResponse.createByErrorMessage("ID错误, 未找到该用户");
            }
            originUser.setName(user.getName());
            originUser.setPhone(user.getPhone());
            originUser.setProvince(user.getProvince());
            originUser.setCity(user.getCity());
            originUser.setDistrict(user.getDistrict());
            originUser.setAddr(user.getAddr());
            originUser.setLvl(user.getLvl());
            originUser.setRole(user.getRole());
            originUser.setShopname(user.getShopname());
            originUser.setShopId(user.getShopId());
            int upgradeCount = userMapper.insertOri(originUser);
            int deleteCount = userMapper.deleteCheckByPrimaryKey(userId);
            if(upgradeCount == 0 && deleteCount == 0) {
                return ServerResponse.createByErrorMessage("审核操作失败");
            }
            return ServerResponse.createBySuccessMessage("审核操作成功");
        } else if ((role.equals(Const.Role.ROLE_PIFA)||role.equals(Const.Role.ROLE_ST)) && status == 2) {
            User user = userMapper.selectCheckByPrimaryKey(userId);
            if(user == null) {
                return ServerResponse.createByErrorMessage("ID错误, 未找到该用户");
            }
            // 审核不通过, 直接删掉表数据即可
            int deleteCount = userMapper.deleteCheckByPrimaryKey(userId);
            if(deleteCount == 0) {
                return ServerResponse.createByErrorMessage("审核操作失败");
            }
            return ServerResponse.createBySuccessMessage("审核操作成功");
        }
        return ServerResponse.createByErrorMessage("审核操作失败");
    }

    public ServerResponse<PageInfo> getUserListToDown(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<User> userList = userMapper.selectListToDown();
        List<UserListVo> userListVoList = Lists.newArrayList();

        for(User userItem : userList){
            UserListVo userListVo = assembleUserListVo(userItem);
            userListVoList.add(userListVo);
        }

        PageInfo pageResult = new PageInfo(userList);
        pageResult.setList(userListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<PageInfo> getUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<User> userList = userMapper.selectList();
        List<UserListVo> userListVoList = Lists.newArrayList();

        for(User userItem : userList){
            UserListVo userListVo = assembleUserListVo(userItem);
            userListVoList.add(userListVo);
        }

        PageInfo pageResult = new PageInfo(userList);
        pageResult.setList(userListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    // 0501
    public ServerResponse<PageInfo> getNormalUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> userList = userMapper.selectNormalUser();
        List<UserListVo> userListVoList = Lists.newArrayList();
        for(User userItem : userList){
            UserListVo userListVo = assembleUserListVo(userItem);
            userListVoList.add(userListVo);
        }
        PageInfo pageResult = new PageInfo(userList);
        pageResult.setList(userListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }


    // 18-04-03
    public ServerResponse<PageInfo> getShitiList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<User> userList = userMapper.selectShitiList();
        List<UserListVo> userListVoList = Lists.newArrayList();

        for(User userItem : userList){
            UserListVo userListVo = assembleUserListVo(userItem);
            userListVoList.add(userListVo);
        }

        PageInfo pageResult = new PageInfo(userList);
        pageResult.setList(userListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<PageInfo> getPifaList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<User> userList = userMapper.selectPifaList();
        List<UserListVo> userListVoList = Lists.newArrayList();

        for(User userItem : userList){
            UserListVo userListVo = assembleUserListVo(userItem);
            userListVoList.add(userListVo);
        }

        PageInfo pageResult = new PageInfo(userList);
        pageResult.setList(userListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private UserListVo assembleUserListVo(User user) {
        UserListVo userListVo = new UserListVo();
        userListVo.setId(user.getId());
        userListVo.setUsername(user.getUsername());
        userListVo.setEmail(user.getEmail());
        userListVo.setName(user.getName());
        userListVo.setPhone(user.getPhone());
        userListVo.setProvince(user.getProvince());
        userListVo.setCity(user.getCity());
        userListVo.setDistrict(user.getDistrict());
        userListVo.setAddr(user.getAddr());
        userListVo.setRole(user.getRole());
        userListVo.setLvl(user.getLvl());
        userListVo.setShopname(user.getShopname());
        userListVo.setShopId(user.getShopId());
        return userListVo;
    }



    public ServerResponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse selectQuestion(String username){

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案是这个用户的,并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    public ServerResponse<User> updateInformation(User user){
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    //backend

    /**
     * 校验是否是管理员 / 批发商
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().equals(Const.Role.ROLE_ADMIN)){
            return ServerResponse.createBySuccess();
        } else if (user != null && user.getRole().equals(Const.Role.ROLE_PIFA)) {
            return ServerResponse.createBySuccess();
        } else if (user != null && user.getRole().equals(Const.Role.ROLE_ST)) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    public ServerResponse checkAdminRoleTest(User user){
        if(user != null && user.getRole().equals(Const.Role.ROLE_ADMIN)){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    // 0120
    public ServerResponse<User> getShopOwner(String username) {
        User user = userMapper.selectByUsername(username);
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        user.setQuestion(org.apache.commons.lang3.StringUtils.EMPTY);
        user.setAnswer(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

}
