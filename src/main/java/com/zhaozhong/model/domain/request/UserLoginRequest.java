package com.zhaozhong.model.domain.request;

import lombok.Data;


import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author zhaozhong
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1550823892671945512L;

    private String userAccount;

    private String userPassword;
}
