package com.zhaozhong.controller;

import com.zhaozhong.common.BaseResponse;
import com.zhaozhong.common.utils.ResultUtils;
import com.zhaozhong.common.utils.ThrowUtils;
import com.zhaozhong.constant.ResponseConstant.Code;
import com.zhaozhong.constant.UserConstant;
import com.zhaozhong.model.domain.User;
import com.zhaozhong.model.domain.request.UserLoginRequest;
import com.zhaozhong.model.domain.request.UserRegisterRequest;
import com.zhaozhong.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author zhaozhong
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    HttpServletRequest request;
    /**
     * 注册接口
     * @param userRegisterRequest 注册信息类
     * @return 返回注册后的用户id
     */
    @PostMapping("/register")
    public BaseResponse register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            ThrowUtils.error(Code.ERROR_PARAMS,"传入值错误","请检查输入是否正确。");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            ThrowUtils.error(Code.ERROR_PARAMS,"传入值错误","输入中有空值。");
        }
        Long registerId = userService.userRegister(userRegisterRequest);
        if(registerId == -1){
            ThrowUtils.error(Code.ERROR_DEFAULT,"错误","该用户名已存在。");
        }
        return ResultUtils.returnDataSuccess(registerId);

    }

    /**
     * 登录接口
     * @param userLoginRequest 登录请求类
     * @param request 前端请求
     * @return 返回登录用户信息
     */
    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            ThrowUtils.error(Code.ERROR_PARAMS,"传入值错误","请检查输入是否正确。");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            ThrowUtils.error(Code.ERROR_PARAMS,"传入值错误","输入中有空值。");
        }

        User safetyUser = userService.userLogin(userAccount, userPassword, request);
        if(safetyUser == null){
            ThrowUtils.error(Code.ERROR_DEFAULT,"错误","未知错误");
        }
        log.info("用户名为{}的用户登录成功。",userAccount);
        return ResultUtils.returnDataSuccess(safetyUser);

    }

    /**
     * 用户注销（登出）
     * @param request 前端请求（清登录消息）
     */
    @PostMapping("/outLogin")
    public BaseResponse userOutLogin(HttpServletRequest request){
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultUtils.commonSuccess();
    }

    /**
     * 得到当前用户接口
     * @param request 前端请求
     * @return 返回当前用户信息
     */
    @GetMapping("/current")
    public BaseResponse getCurrentUser(HttpServletRequest request){

        User currentUser = userService.getCurrentUser(request);
        Long currentUserId = currentUser.getId();
        if(currentUserId<=0) {
            ThrowUtils.error(Code.ERROR_NULL,"错误：无结果","异常，请重新登录。");
        }
        User originUser = userService.getById(currentUserId);
        //脱敏
        return ResultUtils.returnDataSuccess(userService.getSafetyUser(originUser));
    }


    /**
     * 根据用户名搜索用户(支持模糊查询）
     * @param userName 用户名
     * @param request 前端请求(得到用户是否为管理员)
     * @return 返回用户列表
     */
    @GetMapping("/search")
    public BaseResponse searchUser(String userName, HttpServletRequest request) {
        log.info("用户名查询操作：");
        if(userService.isNotAdmin(request)){
            ThrowUtils.error(Code.ERROR_NO_AUTH,"错误：无权限","没有管理员权限。");
        }
        List<User> usersByName = userService.getUsersByName(userName);
        if(usersByName == null){
            ThrowUtils.error(Code.ERROR_NULL,"错误：无结果","请检查用户名是否填写错误。");
        }
        return ResultUtils.returnDataSuccess(usersByName);

    }

    /**
     * 根据id删除用户
     * @param id 用户id
     * @param request 前端请求(得到用户是否为管理员)
     * @return 返回删除是否成功布尔值
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse deleteById(@PathVariable long id, HttpServletRequest request) {
        log.info("删除操作：");
        if(userService.isNotAdmin(request)){
            ThrowUtils.error(Code.ERROR_NO_AUTH,"错误：无权限","没有管理员权限。");
        }
        Boolean isDelete = userService.deleteById(id);
        if(!isDelete){
            ThrowUtils.error(Code.ERROR_NULL,"错误：无结果","请检查删除id");
        }
        return ResultUtils.commonSuccess();
    }

}
