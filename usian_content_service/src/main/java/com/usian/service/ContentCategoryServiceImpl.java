package com.usian.service;

import com.usian.mapper.TbContentCategoryMapper;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    /**
     * 分类内容管理 展示
     * @param id
     * @return
     */
    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        return list;
    }

    /**
     * 分类内容管理 添加
     * @param contentCategory
     * @return
     */
    @Override
    public Integer insertContentCategory(TbContentCategory contentCategory) {
        //补充数据 并添加
        Date date = new Date();
        contentCategory.setIsParent(false);
        contentCategory.setCreated(date);
        contentCategory.setUpdated(date);
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        Integer insertSelective = tbContentCategoryMapper.insertSelective(contentCategory);
        //查询父节点 并 判断是否修改is_Parent
        TbContentCategory parentTbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        if(parentTbContentCategory.getIsParent() == false){
            parentTbContentCategory.setIsParent(true);
            parentTbContentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKeySelective(parentTbContentCategory);
        }
        return insertSelective;
    }

    /**
     * 分类内容管理删除
     * @param id
     * @return
     */
    @Override
    public Integer deleteContentCategoryById(Long categoryId) {
        //查询并判断其是否为父节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        if (tbContentCategory.getIsParent()){
            return 0;
        }
        //删除数据
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        //查询其父节点是否只有他一个子节点 并作出相应得修改
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        if (list == null || list.size() == 0){
            TbContentCategory parentTbContentCategory = new TbContentCategory();
            parentTbContentCategory.setId(tbContentCategory.getParentId());
            parentTbContentCategory.setIsParent(false);
            parentTbContentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKeySelective(parentTbContentCategory);
        }

        return 200;
    }

    /**
     * 分类内容管理修改
     * @param contentCategory
     * @return
     */
    @Override
    public Integer updateContentCategory(TbContentCategory contentCategory) {
        contentCategory.setUpdated(new Date());
        return tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
    }
}
