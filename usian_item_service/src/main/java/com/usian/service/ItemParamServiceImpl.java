package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamExample;
import com.usian.pojo.TbItemParamItem;
import com.usian.pojo.TbItemParamItemExample;
import com.usian.redis.RedisClient;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemParamServiceImpl implements ItemParamService {



    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${PARAM}")
    private String PARAM;

    @Value("${ITEM_INFO_EXPIRE}")
    private Long ITEM_INFO_EXPIRE;

    @Value("${SETNX_PARAM_LOCK_KEY}")
    private Long SETNX_PARAM_LOCK_KEY;

    /**
     * 根据商品分类 ID 查询规格参数模板
     * @param itemCatId
     * @return Result
     */
    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 规格参数查询
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult selectItemParamAll(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        TbItemParamExample example = new TbItemParamExample();
        example.setOrderByClause("updated desc");
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
        PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }

    /**
     * 规格参数添加
     * @param itemCatId
     * @param paramData
     * @return
     */
    @Override
    public Integer insertItemParam(Long itemCatId, String paramData) {
        //判断类型是否已经存在
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExample(tbItemParamExample);
        if (tbItemParams.size() > 0) {
            return 0;
        }
        //若不存在则添加
        Date date = new Date();
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setCreated(date);
        tbItemParam.setUpdated(date);
        tbItemParam.setParamData(paramData);
        int itemParamNum = tbItemParamMapper.insertSelective(tbItemParam);

        return itemParamNum;
    }

    /**
     * 规格参数删除
     * @param id
     * @return
     */
    @Override
    public Integer deleteItemParamById(Long id) {
        return tbItemParamMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
        //1、查询缓存
        TbItemParamItem tbItemParamItem = (TbItemParamItem) redisClient.get(
                ITEM_INFO + ":" + itemId + ":"+ PARAM);
        if(tbItemParamItem!=null){
            return tbItemParamItem;
        }
        if(redisClient.setnx(SETNX_PARAM_LOCK_KEY+":"+itemId,itemId,30L)){
            //2、再查询mysql,并把查询结果缓存到redis,并设置失效时间
            TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
            TbItemParamItemExample.Criteria criteria =
                    tbItemParamItemExample.createCriteria();
            criteria.andItemIdEqualTo(itemId);
            List<TbItemParamItem> tbItemParamItems =
                    tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample);
            if(tbItemParamItems!=null && tbItemParamItems.size()>0){
                tbItemParamItem = tbItemParamItems.get(0);
                redisClient.set(ITEM_INFO + ":" + itemId + ":" + PARAM,tbItemParamItem);
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" +
                        PARAM,ITEM_INFO_EXPIRE);

            }else{
                redisClient.set(ITEM_INFO + ":" + itemId + ":" + PARAM,null);
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + PARAM,30L);
            }
            redisClient.del(SETNX_PARAM_LOCK_KEY+":"+itemId);
            return  tbItemParamItem;
        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return selectTbItemParamItemByItemId(itemId);
        }
    }

}
