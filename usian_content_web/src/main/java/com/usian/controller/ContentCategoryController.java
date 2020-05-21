package com.usian.controller;

import com.usian.feign.ContentServiceFeignClient;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/content")
public class ContentCategoryController {

    @Autowired
    private ContentServiceFeignClient contentServiceFeignClient;

    /**
     * 分类内容管理查询
     * @param id
     * @return
     */
    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(defaultValue = "0") Long id){
        List<TbContentCategory> list = contentServiceFeignClient.selectContentCategoryByParentId(id);
        if(list != null && list.size() > 0){
            return Result.ok(list);
        }
        return Result.error("查询失败");
    }

    /**
     * 分类内容管理 添加
     * @param contentCategory
     * @return
     */
    @RequestMapping("/insertContentCategory")
    public Result insertContentCategory(TbContentCategory contentCategory){
        Integer contentCategoryNum = contentServiceFeignClient.insertContentCategory(contentCategory);
        if (contentCategoryNum == 1){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    /**
     * 分类内容管理删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteContentCategoryById")
    public Result deleteContentCategoryById(Long categoryId){
        Integer contentCategoryNum = contentServiceFeignClient.deleteContentCategoryById(categoryId);
        if(contentCategoryNum == 200){
            return Result.ok();
        }
        return Result.error("删除失败");
    }

    /**
     * 分类内容管理修改
     * @param contentCategory
     * @return
     */
    @RequestMapping("/updateContentCategory")
    public Result updateContentCategory(TbContentCategory contentCategory){
        Integer contentCategoryNum = contentServiceFeignClient.updateContentCategory(contentCategory);
        if(contentCategoryNum == 1){
            return Result.ok();
        }
        return Result.error("删除失败");
    }
}
