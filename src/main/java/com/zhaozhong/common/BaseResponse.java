package com.zhaozhong.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 后端统一返回类
 * code：状态码
 * data：返回内容
 * msg：状态信息
 * description：追加信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 8317520948207487031L;
    private int code;
    private Object data;
    private String msg;
    private String description;
}
