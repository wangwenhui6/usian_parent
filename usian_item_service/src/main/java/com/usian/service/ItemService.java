package com.usian.service;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.utils.PageResult;
import com.usian.utils.UpdateResult;

import java.util.Map;

public interface ItemService {

    TbItem selectItemInfo(Long itemId);

    PageResult selectTbItemAllByPage(Integer page, Long rows);

    Integer insertTbItem(TbItem tbItem, String desc, String itemParams);

    Integer deleteItemById(Long itemId);

    Map<String, Object> preUpdateItem(Long itemId);

    Integer updateTbItem(TbItem tbItem, String desc, String itemParams);

    TbItemDesc seleteItemDescByItemId(Long itemId);

    Integer updateTbItemByOrderId(String orderId);
}
