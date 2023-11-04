package com.zhaozhong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhaozhong.common.BaseResponse;
import com.zhaozhong.common.utils.ThrowUtils;
import com.zhaozhong.constant.ResponseConstant.Code;
import com.zhaozhong.constant.ResponseConstant.Msg;
import com.zhaozhong.constant.UserConstant;
import com.zhaozhong.model.domain.User;
import com.zhaozhong.model.domain.request.UserRegisterRequest;
import com.zhaozhong.service.UserService;
import com.zhaozhong.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 22843
 * 针对表【user】的数据库操作Service实现
 * createDate 2023-10-19 14:10:02
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(UserRegisterRequest registerRequest) {

        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        String planetCode = registerRequest.getPlanetCode();

        // 1. 对信息进行校验
        //检验用户账号、用户密码、校验密码是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            log.info("用户账号、密码、校验密码未填写");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户账号、密码、校验密码未填写。");
        }

        //检验用户账号长度是否不小于4
        if (userAccount.length() < 4) {
            log.info("用户账号长度小于4");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户账号长度小于4。");
        }

        //检验用户密码长度是否不小于8
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            log.info("用户密码长度小于8");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户密码长度小于8。");
        }

        //特殊字符校检
        String validPattern = "^[a-zA-Z0-9_-]{3,16}$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if(!matcher.find()){
            log.info("用户账号中发现特殊字符");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户账号中发现特殊字符。");
        }

        //输入密码和校验密码是否相同
        if(!userPassword.equals(checkPassword)){
            log.info("输入密码和校验密码不相同");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:输入密码和校验密码不相同。");
        }

        //创建user查询条件，对user表中的用户账号为输入账号的行进行数量统计
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(userQueryWrapper);
        //当返回行数大于0，则说明表中已有该账号用户，注册失败
        if(count>0){
            log.info("用户账号已存在。");
            ThrowUtils.error(Code.ERROR_EXIST, Msg.ERROR,"错误:用户账号已存在。");
        }
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        count = userMapper.selectCount(userQueryWrapper);
        //当返回行数大于0，则说明表中已有该账号用户，注册失败
        if(count>0){
            log.info("星球码已存在。");
            ThrowUtils.error(Code.ERROR_EXIST, Msg.ERROR,"错误:星球码已存在。");
        }

        // 2. 对密码进行加密（不可将明文密码放入数据库！）
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT+userPassword).getBytes());

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userAccount);
        user.setPlanetCode(planetCode);

        boolean saveResult = this.save(user);
        if(!saveResult){
            ThrowUtils.error(Code.ERROR_DB_FAIL, Msg.ERROR,"错误:数据库处理失败。");
        }

        return user.getId();
    }


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验用户拒账户和密码是否合法

        // 非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            log.info("用户账号、密码、校验密码未填写");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户账号、密码、校验密码未填写。");
        }

        //检验账户长度是否不小于4
        if (userAccount.length() < 4) {
            log.info("用户账号长度小于4");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户账号长度小于4。");
        }

        //检验密码长度是否不小于8
        if (userPassword.length() < 8) {
            log.info("用户密码长度小于8");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户密码长度小于8。");
        }

        //特殊字符校检
        String validPattern = "^[a-zA-Z0-9_-]{3,16}$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if(!matcher.find()){
            log.info("用户账号中发现特殊字符");
            ThrowUtils.error(Code.ERROR_PARAMS, Msg.ERROR,"错误:用户账号中发现特殊字符。");
        }

        // 2. 校验密码是否输入正确，要和数据库的密文密码去比对（访问数据库）
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT+userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);

        User user = userMapper.selectOne(queryWrapper);

        //用户名和密码不匹配
        if(user == null){
            log.info("user login failed,userAccount cannot match userPassword");
            ThrowUtils.error(Code.ERROR_NOT_MATCH, Msg.ERROR,"错误:用户名和密码不匹配。");
        }

        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);

        // 4. 记录用户的登录态，将其存到服务器端
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,user);

        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){

        if(originUser == null){
            ThrowUtils.error();
        }

        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;

    }

    @Override
    public List<User> getUsersByName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            queryWrapper.like("userName",userName);
        }
        List<User> originList = this.list(queryWrapper);
        return originList.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(id<0){
            ThrowUtils.error(Code.ERROR_NULL, Msg.ERROR,"错误：不存在");
        }
        return true;
    }

    @Override
    public boolean isNotAdmin(HttpServletRequest request){
        User user = getCurrentUser(request);
        //用户权限不足，返回空
        if (user.getUserRole() != UserConstant.ADMIN_ROLE) {
            log.info("用户权限不足。");
            ThrowUtils.error(Code.ERROR_NO_AUTH, Msg.ERROR,"错误：权限不足");
        }
        log.info("成功。");
        return false;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(userObj == null){
            log.info("传入值为空。");
            ThrowUtils.error(Code.ERROR_NOT_LOGIN,"错误：未登录","请先登录。");
        }
        User user = null;
        if (userObj instanceof User) {
            user = (User) userObj;
        } else {
            log.info("传入类型属性不匹配。");
            ThrowUtils.error(Code.ERROR_PARAMS,"错误：未登录","传入类型属性不匹配。");
        }
        return user;
    }
}




