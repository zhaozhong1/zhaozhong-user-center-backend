package com.zhaozhong.common.utils;

import com.zhaozhong.constant.ResponseConstant.Code;
import com.zhaozhong.constant.ResponseConstant.Description;
import com.zhaozhong.constant.ResponseConstant.Msg;
import com.zhaozhong.exception.BusinessException;
/**
 * 由全局异常托管的Error方法。
 */
public class ThrowUtils {
    public static void error(){
        throw new BusinessException(Code.ERROR_DEFAULT, Msg.ERROR, Description.BLANK);
    }
    public static void error(int code){
        throw new BusinessException(code, Msg.ERROR, Description.BLANK);
    }
    public static void error(String description){
        throw new BusinessException(Code.ERROR_DEFAULT, Msg.ERROR, description);
    }
    public static void error(int code,String description){
        throw new BusinessException(code, Msg.ERROR, description);
    }

    public static void error(int code, String msg, String description) {
        throw new BusinessException(code, msg, description);
    }
}
