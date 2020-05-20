package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItemParam;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {

    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    /**
     * 根据商品分类 ID 查询规格参数模板
     * @param itemCatId
     * @return Result /backend/itemParam/selectItemParamByItemCatId/
     */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){
        TbItemParam tbItemParam = itemServiceFeignClient.selectItemParamByItemCatId(itemCatId);
        if (tbItemParam != null){
            return Result.ok(tbItemParam);
        }
        return Result.error("查无结果");
    }

    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer rows){
        System.out.println(page+"============================="+rows);
        PageResult pageResult = itemServiceFeignClient.selectItemParamAll(page,rows);
        if(pageResult.getResult().size() > 0){
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }
}
