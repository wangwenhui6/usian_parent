package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.*;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import com.usian.utils.UpdateResult;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public TbItem selectItemInfo(Long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Long rows) {
        PageHelper.startPage(page,rows.intValue());
        TbItemExample example = new TbItemExample();
        example.setOrderByClause("updated desc");
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> list = this.itemMapper.selectByExample(example);
        for (int i = 0; i < list.size(); i++) {
            TbItem tbItem =  list.get(i);
            tbItem.setPrice(tbItem.getPrice()/100);
        }
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(page);
        pageResult.setTotalPage(pageInfo.getTotal());
        pageResult.setResult(list);
        return pageResult;
    }

    @Override
    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        //补齐Tbitem数据
        long itemId = IDUtils.genItemId(); //工具类获取id
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date); //添加时间
        tbItem.setCreated(date); //修改时间
        tbItem.setPrice(tbItem.getPrice()*100);
        int tbItemNum = itemMapper.insertSelective(tbItem);

        //补齐商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int tbItemDescNum = tbItemDescMapper.insertSelective(tbItemDesc);

        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);
        int tbItemParamItemNum = tbItemParamItemMapper.insertSelective(tbItemParamItem);

        //添加商品发布到rabbitmq
        amqpTemplate.convertAndSend("item_exchage","item.add",itemId);

        return tbItemNum + tbItemDescNum + tbItemParamItemNum;
    }

    @Override
    public Integer deleteItemById(Long itemId) {
        //商品表删除
        int tbItemNum = itemMapper.deleteByPrimaryKey(itemId);
        //商品描述删除
        TbItemDescExample tbItemDescExample = new TbItemDescExample();
        TbItemDescExample.Criteria tbItemDescExampleCriteria = tbItemDescExample.createCriteria();
        tbItemDescExampleCriteria.andItemIdEqualTo(itemId);
        int tbItemDescNum = tbItemDescMapper.deleteByExample(tbItemDescExample);
        //商品规格删除
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria tbItemParamItemExampleCriteria = tbItemParamItemExample.createCriteria();
        tbItemParamItemExampleCriteria.andItemIdEqualTo(itemId);
        int tbItemParamItemNum = tbItemParamItemMapper.deleteByExample(tbItemParamItemExample);
        //返回
        return tbItemNum + tbItemDescNum + tbItemParamItemNum;
    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        //{“itemCat”:”xxxx”,”item”:{xxxx},”itemDesc”:”xxxx”,”itemParamItem”:”xxxxx”}
        Map<String, Object> map = new HashMap<>();
        //查item对象
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        map.put("item", tbItem);
        //查itemCat
        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());
        map.put("itemCat", tbItemCat.getName());
        //查itemDesc
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc", tbItemDesc.getItemDesc());
        //查itemparamitem
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            map.put("itemParamItem", list.get(0).getParamData());
        }
        return map;
    }

    @Override
    public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
        //补齐Tbitem数据
        Date date = new Date();
        tbItem.setUpdated(date); //修改时间
        tbItem.setPrice(tbItem.getPrice()*100);
        int tbItemNum = itemMapper.updateByPrimaryKeySelective(tbItem);

        //补齐商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setUpdated(date);
        int tbItemDescNum = tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);

        //补齐商品规格参数
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria tbItemParamItemExampleCriteria = tbItemParamItemExample.createCriteria();
        tbItemParamItemExampleCriteria.andItemIdEqualTo(tbItem.getId());
        List<TbItemParamItem> tbItemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample);
        TbItemParamItem tbItemParamItem = tbItemParamItems.get(0);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        int tbItemParamItemNum = tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);

        return tbItemNum + tbItemDescNum + tbItemParamItemNum;
    }
}
