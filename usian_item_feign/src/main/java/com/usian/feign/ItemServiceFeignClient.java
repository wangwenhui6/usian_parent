package com.usian.feign;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemParam;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;
import com.usian.utils.UpdateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/service/item/insertTbItem")
    Integer insertTbItem(TbItem tbItem,@RequestParam String desc,@RequestParam String itemParams);

    /**
     * 删除商品
     * @param itemId
     * @return
     */
    @RequestMapping("/service/item/deleteItemById")
    Integer deleteItemById(@RequestParam Long itemId);

    /**
     * 规格参数查询
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/service/itemParam/selectItemParamAll")
    PageResult selectItemParamAll(@RequestParam Integer page,@RequestParam Integer rows);

    /**
     * 商品规格模板添加
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping("/service/itemParam/insertItemParam")
    Integer insertItemParam(@RequestParam Long itemCatId,@RequestParam String paramData);

    /**
     * 规格参数删除
     * @param id
     * @return
     */
    @RequestMapping("/service/itemParam/deleteItemParamById")
    Integer deleteItemParamById(@RequestParam Long id);

    /**
     * 预更新商品
     * @return
     */
    @RequestMapping("/service/item/preUpdateItem")
    Map<String, Object> preUpdateItem(@RequestParam Long itemId);

    /**
     * 首页商品分类
     * @return
     */
    @RequestMapping("/service/itemCategory/selectItemCategoryAll")
    CatResult selectItemCategoryAll();

    /**
     * 修改商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/service/item/updateTbItem")
    Integer updateTbItem(TbItem tbItem,@RequestParam String desc,@RequestParam String itemParams);
}
