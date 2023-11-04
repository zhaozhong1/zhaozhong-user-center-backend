package com.zhaozhong.service;

import com.zhaozhong.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhaozhong.model.domain.request.UserRegisterRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 22843
* description 针对表【user】的数据库操作Service
* createDate 2023-10-19 14:10:02
*/
public interface UserService extends IService<User> {


    /**
     * @param registerRequest@return 返回用户id
     */
    long userRegister(UserRegisterRequest registerRequest);


    /**
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      请求
     * @return 返回用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * @param originUser 未脱敏对象
     * @return 安全对象
     */
    User getSafetyUser(User originUser);

    /**
     * 仅管理员可访问
     *
     * @param userName 搜索用户名（可模糊查询）
     * @return 返回查询用户列表
     */
    List<User> getUsersByName(String userName);

    /**
     * 仅管理员可访问
     *
     * @param id 删除id
     * @return 返回布尔值判断是否删除成功
     */
    Boolean deleteById(long id);

    boolean isNotAdmin(HttpServletRequest request);

    User getCurrentUser(HttpServletRequest request);
}
