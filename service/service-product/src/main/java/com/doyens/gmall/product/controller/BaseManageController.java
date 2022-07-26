package com.doyens.gmall.product.controller;

import com.doyens.gmall.common.result.Result;
import com.doyens.gmall.model.product.*;
import com.doyens.gmall.product.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/product")
public class BaseManageController {

    @Autowired
    private ManageService manageService;

    //查询一级分类
    @GetMapping("getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> baseCategory1List = manageService.getCategory1();
        return Result.ok(baseCategory1List);
    }

    //根据一级分类id查询二级分类
    @GetMapping("getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable("category1Id")Long category1Id){
        List<BaseCategory2> baseCategory2List = manageService.getCategory2(category1Id);
        return Result.ok(baseCategory2List);
    }

    //根据二级分类id查询三级分类
    @GetMapping("getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable("category2Id")Long category2Id){
        List<BaseCategory3> baseCategory3List = manageService.getCategory3(category2Id);
        return Result.ok(baseCategory3List);
    }

    //根据分类id查询平台属性
    @GetMapping("attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result getAttrInfoList(@PathVariable("category1Id")Long category1Id,
                                  @PathVariable("category2Id")Long category2Id,
                                  @PathVariable("category3Id")Long category3Id){
        List<BaseAttrInfo> attrInfoList =manageService.getAttrInfoList(category1Id,category2Id,category3Id);
        return Result.ok(attrInfoList);
    }

    //保存平台属性
    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        //通过对象调用方法
        manageService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    //查询平台属性值
    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId){

        BaseAttrInfo baseAttrInfo=manageService.getBaseAttrInfo(attrId);
        if (baseAttrInfo!=null){
            return Result.ok(baseAttrInfo.getAttrValueList());
        }

        //List<BaseAttrValue> baseAttrValueList=manageService.getAttrValueList(attrId);
        return Result.ok();
    }
}
