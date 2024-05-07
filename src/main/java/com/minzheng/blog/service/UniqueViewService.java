package com.minzheng.blog.service;

import com.minzheng.blog.entity.UniqueView;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 统计每日用户量
     */
    void saveUniqueView();

}
