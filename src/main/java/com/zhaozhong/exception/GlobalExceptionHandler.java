package com.zhaozhong.exception;

import com.zhaozhong.common.BaseResponse;
import com.zhaozhong.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BusinessException处理方法
     * @param e BusinessException异常
     * @return 返回BaseResponse类
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:"+e.getCode()+" "+e.getMessage()+" "+e.getDescription(),e);
        return ResultUtils.commonError(e.getCode(),e.getMessage(),e.getDescription());
    }

    /**
     * RuntimeException处理方法
     * @param e RuntimeException异常
     * @return 返回BaseResponse类
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException:",e);
        return ResultUtils.commonError();
    }

}
