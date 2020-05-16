package com.usian.feign;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemParam;
import com.usian.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "usian-item-service")
public interface ItemServiceFeignClient {

    /**
     * 单个商品查询
     * @param itemId
     * @return TbItem
     */
    @RequestMapping("/service/item/selectItemInfo")
    TbItem selectItemInfo(@RequestParam("itemId") Long itemId);

    /**
     * 分页展示商品信息
     * @param page
     * @param rows
     * @return PageResult
     */
    @RequestMapping("/service/item/selectTbItemAllByPage")
    PageResult selectTbItemAllByPage(@RequestParam Integer page,@RequestParam Long rows);

    /**
     * 根据类目 ID 查询当前类目的子节点
     * @param id
     * @return List<TbItem>
     */
    @RequestMapping("/service/itemCategory/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam Long id);

    /**
     * 根据商品分类 ID 查询规格参数模板
     * @param itemCatId
     * @return TbItemParam
     */
    @RequestMapping("/service/itemParam/selectItemParamByItemCatId/{itemCatId}")
    TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId);
}
