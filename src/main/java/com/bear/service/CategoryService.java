package com.bear.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bear.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
