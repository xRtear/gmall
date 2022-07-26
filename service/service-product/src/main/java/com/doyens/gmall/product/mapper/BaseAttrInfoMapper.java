package com.doyens.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doyens.gmall.model.product.BaseAttrInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    //自定义动态sql语句

    List<BaseAttrInfo> selectBaseAttrInfoList(@Param("category1Id")Long category1Id,
                                              @Param("category2Id")Long category2Id,
                                              @Param("category3Id")Long category3Id);
}
