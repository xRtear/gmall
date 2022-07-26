package com.doyens.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doyens.gmall.model.product.*;
import com.doyens.gmall.product.mapper.*;
import com.doyens.gmall.product.service.ManageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;
    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;
    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    //查询一级分类
    @Override
    public List<BaseCategory1> getCategory1() {
        return baseCategory1Mapper.selectList(null);
    }
    //根据一级分类id查询二级分类
    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        return baseCategory2Mapper.selectList(new QueryWrapper<BaseCategory2>().eq("category1_id",category1Id));
    }
    //根据二级分类id查询三级分类
    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        return baseCategory3Mapper.selectList(new QueryWrapper<BaseCategory3>().eq("category2_id",category2Id));
    }

    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.selectBaseAttrInfoList(category1Id, category2Id, category3Id);
        return baseAttrInfoList;
    }

    //保存平台属性和属性值 需同时保存到两张表 使用事务
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {

        if (baseAttrInfo.getId()!=null){
            baseAttrInfoMapper.updateById(baseAttrInfo);
        }else {
            baseAttrInfoMapper.insert(baseAttrInfo);
        }

        //先删除后新增
        QueryWrapper<BaseAttrValue> queryWrapper=new QueryWrapper<BaseAttrValue>().eq("attr_id",baseAttrInfo.getId());
        //执行删除
        baseAttrValueMapper.delete(queryWrapper);

        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

        if (!CollectionUtils.isEmpty(attrValueList)){
            //循环集合，保存数据
            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(baseAttrInfo.getId());//拿到上一张表里递增后的id
                //通过对象调用方法
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }

    }

    //查询平台属性值
    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        List<BaseAttrValue> baseAttrValueList=baseAttrValueMapper.selectList(new QueryWrapper<BaseAttrValue>().eq("attr_id",attrId));
        return baseAttrValueList;
    }

    //完善查询平台属性和属性值
    @Override
    public BaseAttrInfo getBaseAttrInfo(Long attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        if (baseAttrInfo!=null){
            List<BaseAttrValue> baseAttrValueList = getAttrValueList(attrId);
            if (!CollectionUtils.isEmpty(baseAttrValueList)){
                baseAttrInfo.setAttrValueList(baseAttrValueList);
            }
        }
        return baseAttrInfo;
    }
}
