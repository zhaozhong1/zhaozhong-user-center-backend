package com.zhaozhong.dbTest;

import com.zhaozhong.model.domain.request.UserRegisterRequest;
import com.zhaozhong.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
@Slf4j
@SpringBootTest
public class AliyunMySQLConnectTest {
    @Resource
    UserService userService;
    @Test
    public void registerTest(){
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("1111","11111111","11111111","1");
        long l = userService.userRegister(userRegisterRequest);
        log.info("{}",l);
    }

}
