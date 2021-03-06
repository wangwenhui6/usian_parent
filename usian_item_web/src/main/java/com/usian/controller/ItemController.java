package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItem;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.utils.UpdateResult;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/backend/item")
public class ItemController {

    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    /**
     * 查询单个商品信息
     * @param itemId
     * @return Result
     */
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId){
        TbItem tbItem = itemServiceFeignClient.selectItemInfo(itemId);
        if(tbItem != null){
            return Result.ok(tbItem);
        }
        return Result.error("查无此结果");
    }

    /**
     * 分页展示所有商品
     * @param page
     * @param rows
     * @return Result
     */
    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "2") Long rows){
        PageResult pageResult = itemServiceFeignClient.selectTbItemAllByPage(page,rows);
        if (pageResult.getResult() != null && pageResult.getResult().size() > 0){
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return backend/item/insertTbItem?
     */
    @RequestMapping("/insertTbItem")
    public Result insertTbItem(TbItem tbItem,String desc,String itemParams){
        Integer insertTbItemNum = itemServiceFeignClient.insertTbItem(tbItem,desc,itemParams);
        if(insertTbItemNum == 3){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    /**
     * 商品删除
     * @param itemId
     * @return
     */
    @RequestMapping("/deleteItemById")
    public Result deleteItemById(Long itemId){
        Integer itemNum = itemServiceFeignClient.deleteItemById(itemId);
        if(itemNum == 3){
            return Result.ok();
        }
        return Result.error("删除失败");
    }

    /**
     * 预更新商品
     * @return
     */
    @RequestMapping("/preUpdateItem")
    public Result preUpdateItem(Long itemId){
        Map<String, Object> map = itemServiceFeignClient.preUpdateItem(itemId);
        if (map.get("item") != null){
            return Result.ok(map);
        }
        return Result.error("预更新失败");
    }

    /**
     * 修改商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/updateTbItem")
    public Result updateTbItem(TbItem tbItem,String desc,String itemParams){
        Integer itemNum = itemServiceFeignClient.updateTbItem(tbItem,desc,itemParams);
        if(itemNum == 3){
            return Result.ok();
        }
        return Result.error("修改失败");
    }
}
