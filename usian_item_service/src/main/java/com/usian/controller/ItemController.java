package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.service.ItemService;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.utils.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/service/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 查询单个商品信息
     * @param itemId
     * @return TbItem
     */
    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(Long itemId){
        return this.itemService.selectItemInfo(itemId);
    }

    /**
     * 分页展示所有商品
     * @param page
     * @param rows
     * @return PageResult
     */
    @RequestMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(Integer page,Long rows){
        return this.itemService.selectTbItemAllByPage(page,rows);
    }

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/insertTbItem")
    public Integer insertTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return this.itemService.insertTbItem(tbItem,desc,itemParams);
    }

    /**
     * 商品删除
     * @param itemId
     * @return
     */
    @RequestMapping("/deleteItemById")
    public Integer deleteItemById(Long itemId){
        return this.itemService.deleteItemById(itemId);
    }

    /**
     * 预更新商品
     * @return
     */
    @RequestMapping("/preUpdateItem")
    public Map<String, Object> preUpdateItem(Long itemId){
        return itemService.preUpdateItem(itemId);
    }

    /**
     * 修改商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/updateTbItem")
    public Integer updateTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return itemService.updateTbItem(tbItem,desc,itemParams);
    }
}
