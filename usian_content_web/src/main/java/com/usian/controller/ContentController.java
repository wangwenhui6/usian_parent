package com.usian.controller;

import com.usian.feign.ContentServiceFeignClient;
import com.usian.pojo.TbContent;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/content")
public class ContentController {

    @Autowired
    private ContentServiceFeignClient contentServiceFeignClient;

    /**
     * 内容管理 查询
     * @param page
     * @param rows
     * @param categoryId
     * @return
     */
    @RequestMapping("/selectTbContentAllByCategoryId")
    public Result selectTbContentAllByCategoryId(@RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "20") Integer rows,Long categoryId){
        PageResult pageResult = contentServiceFeignClient.selectTbContentAllByCategoryId(page,rows,categoryId);
        if(pageResult != null && pageResult.getResult().size() > 0){
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }

    /**
     * 根据分类添加内容
     * @param tbContent
     * @return
     */
    @RequestMapping("/insertTbContent")
    public Result insertTbContent(TbContent tbContent){
        Integer tbContentNum = contentServiceFeignClient.insertTbContent(tbContent);
        if (tbContentNum == 1){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    /**
     * 删除分类的内容
     * @param ids
     * @return
     */
    @RequestMapping("/deleteContentByIds")
    public Result deleteContentByIds(Long ids){
        Integer tbContentNum = contentServiceFeignClient.deleteContentByIds(ids);
        if(tbContentNum == 1){
            return Result.ok();
        }
        return Result.error("删除失败");
    }
}
