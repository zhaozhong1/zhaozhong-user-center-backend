package com.zhaozhong.service;

import com.zhaozhong.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhaozhong.model.domain.request.UserRegisterRequest;
import com.zhaozhong.model.domain.request.UserSearchRequest;

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
     * @param searchUser 前端提供的查询条件封装的对象
     * @return 返回查询用户列表
     */
    List<User> getUserByRequest(UserSearchRequest searchUser);

    /**
     * 仅管理员可访问
     *
     * @param id 删除id
     * @return 返回布尔值判断是否删除成功
     */
    Boolean deleteById(long id);

    /**
     * 判断会话对象是否为管理员
     * @param request 在请求中得到会话中的属性来判断对象是否为管理员
     * @return true：管理员 false：普通对象
     */
    boolean isNotAdmin(HttpServletRequest request);

    /**
     * 从会话属性中得到当前用户的信息
     * @param request 在请求中得到会话中的属性来取得当前用户
     * @return 返回当前用户对象
     */
    User getCurrentUser(HttpServletRequest request);


    /**
     * 仅管理员可访问
     * @param updatedUser 传入更新的用户对象
     * @return 返回boolean值 true为更新成功
     */
    boolean updateUser(User updatedUser);

    /**
     * 对选择的多个用户授权(将userRole字段改为1-管理员)
     * @param ids 选择用户的id数组
     * @return 返回boolean值 true为授权成功
     */
    boolean updateRoleByIds(Long[] ids);

    /**
     * 对选择的多个用户进行批量删除
     * @param ids 选择用户的id数组
     * @return 返回boolean值 true为删除成功
     */
    boolean deleteByIds(Long[] ids);

    /**
     * 新建一个用户
     * @param user 新建用户的信息
     * @return 返回boolean值 true为新建成功
     */
    boolean insertUser(User user);
}
