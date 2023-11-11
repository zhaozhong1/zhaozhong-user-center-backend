package com.zhaozhong.utils;


import com.zhaozhong.common.BaseResponse;
import com.zhaozhong.constant.ResponseConstant.Code;
import com.zhaozhong.constant.ResponseConstant.Description;
import com.zhaozhong.constant.ResponseConstant.Msg;
import com.zhaozhong.exception.BusinessException;

public class ResultUtils {
    private static final Object DATA_NULL = null;

    public static BaseResponse commonSuccess(){
        return new BaseResponse(Code.SUCCESS_DEFAULT, DATA_NULL, Msg.SUCCESS, Description.BLANK);
    }
    public static BaseResponse commonSuccess(String description){
        return new BaseResponse(Code.SUCCESS_DEFAULT, DATA_NULL, Msg.SUCCESS, description);
    }

    public static BaseResponse returnDataSuccess(Object data){
        return new BaseResponse(Code.SUCCESS_DEFAULT, data, Msg.SUCCESS, Description.BLANK);
    }
    public static BaseResponse returnDataSuccess(Object data,String description){
        return new BaseResponse(Code.SUCCESS_DEFAULT, data, Msg.SUCCESS, description);
    }



    public static BaseResponse commonError(){
        return new BaseResponse(Code.ERROR_DEFAULT,DATA_NULL,Msg.ERROR,Description.BLANK);
    }
    public static BaseResponse commonError(int code){
        return new BaseResponse(code,DATA_NULL,Msg.ERROR,Description.BLANK);
    }
    public static BaseResponse commonError(String description){
        return new BaseResponse(Code.ERROR_DEFAULT,DATA_NULL,Msg.ERROR,description);
    }
    public static BaseResponse commonError(int code,String description){
        return new BaseResponse(code,DATA_NULL,Msg.ERROR,description);
    }

    public static BaseResponse commonError(int code, String message, String description) {
        return new BaseResponse(code,DATA_NULL,message,description);
    }
}
