package com.zhaozhong.controller;

import com.zhaozhong.common.BaseResponse;
import com.zhaozhong.utils.AliOSSProperties;
import com.zhaozhong.utils.AliOSSUtils;
import com.zhaozhong.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 上传文件接口
 * 使用阿里云OSS进行文件存储
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    AliOSSUtils aliOSSUtils;

    /**
     * 上传头像操作
     * @param avatarFile 头像文件
     * @return 返回头像的访问Url
     * @throws IOException
     */
    @PostMapping("/avatar")
    public BaseResponse uploadAvatar(@RequestPart("file") MultipartFile avatarFile) throws IOException {
        String avatarUrl = aliOSSUtils.putAvatar(avatarFile);
        log.info(avatarUrl);
        return ResultUtils.returnDataSuccess(avatarUrl);
    }
}
