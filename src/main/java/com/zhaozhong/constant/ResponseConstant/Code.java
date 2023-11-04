package com.zhaozhong.constant.ResponseConstant;
/**
 * 该接口用于定义统一返回类的状态码常量
 * SUCCESS_DEFAULT： 成功默认返回0
 * ERROR_DEFAULT： 失败默认返回-1
 * ERROR_PARAMS: 客户端传值错误码：40000;
 * ERROR_NULL: 服务器查询为空错误码：40001;
 * ERROR_EXIST: 服务器查询已存在错误码：40002;
 * ERROR_NOT_MATCH: 用户名和密码不匹配错误码: 40003;
 * ERROR_NOT_LOGIN: 客户端未登录错误码：40100;
 * ERROR_NO_AUTH: 无权限错误码：40300;
 * ERROR_DB_FAIL: 数据库处理失败错误码：50000;
 */
public interface Code {
    static final int SUCCESS_DEFAULT = 0;
    static final int ERROR_DEFAULT = -1;
    static final int ERROR_PARAMS = 40000;
    static final int ERROR_NULL = 40001;
    static final int ERROR_EXIST = 40002;
    static final int ERROR_NOT_MATCH = 40003;
    static final int ERROR_NOT_LOGIN = 40100;
    static final int ERROR_NO_AUTH = 40300;

    static final int ERROR_DB_FAIL = 50000;

}
