package com.usian.service;

import com.usian.pojo.TbContentCategory;
import com.usian.utils.Result;

import java.util.List;

public interface ContentCategoryService {

    List<TbContentCategory> selectContentCategoryByParentId(Long id);

    Integer insertContentCategory(TbContentCategory contentCategory);

    Integer deleteContentCategoryById(Long categoryId);

    Integer updateContentCategory(TbContentCategory contentCategory);
}
