package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/itemParam")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据商品分类 ID 查询规格参数模板
     * @param itemCatId
     * @return Result
     */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){
        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    @RequestMapping("/selectItemParamAll")
    public PageResult selectItemParamAll(Integer page,Integer rows){
        return itemParamService.selectItemParamAll(page,rows);
    }
}
