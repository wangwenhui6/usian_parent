package com.usian.service;

import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /**
     * 根据类目父节点查询子节点
     * @param id
     * @return
     */
    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        criteria.andStatusEqualTo(1);
        List<TbItemCat> list = this.tbItemCatMapper.selectByExample(example);
        return list;
    }

    /**
     * 查询首页商品分类
     * @return
     */
    @Override
    public CatResult selectItemCategoryAll() {
        CatResult catResult = new CatResult();
        catResult.setData(getCatList(0L));
        return catResult;
    }

    /**
     * 私有方法，查询商品分类
     */
    private List<?> getCatList(Long parentId){
        //根据父节点查询
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCatList = tbItemCatMapper.selectByExample(example);
        List catNodeList = new ArrayList<>();
        int count = 0;
        //遍历父节点集合 快捷键（itli）
        for (int i = 0; i < tbItemCatList.size(); i++) {
            TbItemCat tbItemCat = tbItemCatList.get(i);
            if (tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));
                catNodeList.add(catNode);
                count++;
                if(count == 18){
                    break;
                }
            }else{
                //若其没有字节点
                catNodeList.add(tbItemCat.getName());
            }

        }
        return catNodeList;
    }
}
