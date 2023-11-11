package com.zhaozhong.controller;

import com.zhaozhong.common.BaseResponse;
import com.zhaozhong.constant.ResponseConstant.Msg;
import com.zhaozhong.utils.ResultUtils;
import com.zhaozhong.utils.ThrowUtils;
import com.zhaozhong.constant.ResponseConstant.Code;
import com.zhaozhong.constant.UserConstant;
import com.zhaozhong.model.domain.User;
import com.zhaozhong.model.domain.request.UserLoginRequest;
import com.zhaozhong.model.domain.request.UserRegisterRequest;
import com.zhaozhong.model.domain.request.UserSearchRequest;
import com.zhaozhong.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
     * 搜索用户(支持模糊查询）
     * @param searchUser 前端传入的用户查询对象。
     * @param request 前端请求(得到用户是否为管理员)
     * @return 返回用户列表
     */
    @PostMapping("/search")
    public BaseResponse searchUser(@RequestBody UserSearchRequest searchUser, HttpServletRequest request) {
        log.info("用户名查询操作：");
        if(userService.isNotAdmin(request)){
            ThrowUtils.error(Code.ERROR_NO_AUTH,"错误：无权限","没有管理员权限。");
        }
        List<User> usersByName = userService.getUserByRequest(searchUser);
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
        return ResultUtils.commonSuccess("删除成功");
    }

    /**
     * 通过id来修改用户
     * @param updatedUser 修改的用户信息
     * @param request 前端请求(得到用户是否为管理员)
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@RequestBody User updatedUser, HttpServletRequest request){
        log.info("修改操作：");
        if(userService.isNotAdmin(request)){
            ThrowUtils.error(Code.ERROR_NO_AUTH,"错误：无权限","没有管理员权限。");
        }
        boolean isUpdate = userService.updateUser(updatedUser);
        if(!isUpdate){
            ThrowUtils.error(Code.ERROR_NULL,"错误：无结果","请检查更新用户是否存在或正确。");
        }
        return ResultUtils.commonSuccess("更新成功");
    }


    /**
     * 对表格的复选框进行多选来对多个用户进行授权
     * @param ids 选中的id
     * @param request 前端请求(得到用户是否为管理员)
     * @return 返回授权是否成功的布尔值
     */
    @PostMapping("/authorized")
    public BaseResponse authorize(@RequestBody Long[] ids,HttpServletRequest request){
        log.info("授权操作：");
        if(userService.isNotAdmin(request)){
            ThrowUtils.error(Code.ERROR_NO_AUTH,"错误：无权限","没有管理员权限。");
        }
        boolean isAuthorized = userService.updateRoleByIds(ids);
        if(!isAuthorized){
            ThrowUtils.error(Code.ERROR_NULL,"错误：无结果","请检查更新用户是否存在或正确。");
        }
        return ResultUtils.commonSuccess("授权成功");
    }

    /**
     * 对表格的复选框进行多选来删除多列
     * @param ids 选中的id
     * @param request 前端请求(得到用户是否为管理员)
     * @return 返回删除是否成功的布尔值
     */
    @PostMapping("/deleteByIds")
    public BaseResponse deleteByIds(@RequestBody Long[] ids,HttpServletRequest request){

        log.info("删除多项操作：");
        if(userService.isNotAdmin(request)){
            ThrowUtils.error(Code.ERROR_NO_AUTH,"错误：无权限","没有管理员权限。");
        }
        boolean isDelete = userService.deleteByIds(ids);
        if(!isDelete){
            ThrowUtils.error(Code.ERROR_NULL,"错误：无结果","请检查更新用户是否存在或正确。");
        }
        return ResultUtils.commonSuccess("删除多个用户成功");
    }

    /**
     * 新增操作(重要：涉及到云存储等知识)
     * @param insertUser 插入的用户信息
     * @param request 前端请求(得到用户是否为管理员)
     * @return 返回新增是否成功的布尔值
     */
    @PostMapping("/addUser")
    public BaseResponse addUser(@RequestBody User insertUser,HttpServletRequest request){
        log.info("添加新用户操作");

        boolean isInsert = userService.insertUser(insertUser);
        if(!isInsert){
            ThrowUtils.error(Code.ERROR_NULL,"错误：添加失败","请检查添加用户信息是否正确。");
        }
        return ResultUtils.commonSuccess();
    }
}
