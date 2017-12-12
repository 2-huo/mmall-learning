package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteStByPrimaryKey(Integer id);

    int deletePfByPrimaryKey(Integer id);

    int insert(User record);

    // 升级会员 插入批发表 实体店表
    int insertPf(User record);

    // 列出待审核用户表
    List<User> selectList();

    // 审核通过, 插入原表
    int insertOri(User record);

    int insertSt(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectStByPrimaryKey(Integer id);

    User selectPfByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    int checkPassword(@Param(value = "password") String password, @Param("userId") Integer userId);

    int checkEmailByUserId(@Param(value = "email") String email, @Param(value = "userId") Integer userId);
}