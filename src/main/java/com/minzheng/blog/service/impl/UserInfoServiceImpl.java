package com.minzheng.blog.service.impl;

import com.minzheng.blog.constant.PathConst;
import com.minzheng.blog.entity.UserInfo;
import com.minzheng.blog.dao.UserInfoDao;
import com.minzheng.blog.exception.ServeException;
import com.minzheng.blog.service.FileStorageService;
import com.minzheng.blog.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.minzheng.blog.vo.UserInfoVO;
import com.minzheng.blog.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        userInfoDao.updateById(new UserInfo(userInfoVO));
    }

    @Transactional(rollbackFor = ServeException.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        //头像上传oss，返回图片地址
//        String avatar = OSSUtil.upload(file, PathConst.AVATAR);
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = file.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));//.jpg
        try {
            String avatar = fileStorageService.uploadImgFile(PathConst.ARTICLE,fileName+postfix,file.getInputStream());
            userInfoDao.updateById(new UserInfo(avatar));
            return avatar;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = ServeException.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        userInfoDao.updateById(new UserInfo(userRoleVO));
    }

    @Transactional(rollbackFor = ServeException.class)
    @Override
    public void updateUserSilence(Integer userInfoId, Integer isSilence) {
        userInfoDao.updateById(new UserInfo(userInfoId, isSilence));
    }

}
