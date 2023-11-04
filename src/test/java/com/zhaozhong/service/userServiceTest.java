package com.zhaozhong.service;

import com.zhaozhong.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 用户服务测试
 *
 * @author zhaozhong
 */
@SpringBootTest
class userServiceTest {

    @Resource
    private UserService userService;
    @Test
    public void testAddUser(){

        User user = new User();
        user.setUserName("jiba");
        user.setUserAccount("123");
        user.setAvatarUrl("abc");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }


//    @Test
//    public void testRegister(){
//        //用户账号长度小于4
//        String userAccount = "a";
//        String userPassword = "12345678";
//        String checkPassword = "12345678";
//        long id = userService.userRegister(userAccount);
//        Assertions.assertEquals(-1,id);
//        //用户密码长度小于8
//        userAccount = "1234";
//        userPassword = "1234";
//        checkPassword = "1234";
//        id = userService.userRegister(userAccount);
//        Assertions.assertEquals(-1,id);
//        //用户账号含有特殊字符
//        userAccount = "a -?.,`";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        id = userService.userRegister(userAccount);
//        Assertions.assertEquals(-1,id);
//        //用户密码和校验密码不匹配
//        userAccount = "1234";
//        userPassword = "12345678";
//        checkPassword = "123456789";
//        id = userService.userRegister(userAccount);
//        Assertions.assertEquals(-1,id);
//        //用户账号在数据库中已存在
//        userAccount = "abcd";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        id = userService.userRegister(userAccount);
//        Assertions.assertEquals(-1,id);
//        //用户注册通过
//        userAccount = "1234";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        id = userService.userRegister(userAccount);
//        Assertions.assertTrue(id>0);
//    }

    @Test
    public void loginTest(){
        String userAccount = "1234";
        String userPassword = "12345678";
//        userService.doLogin(u)
//        Assertions.assertTrue();
    }
}