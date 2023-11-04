package com.zhaozhong.model.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author zhaozhong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1594829768835219152L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}

