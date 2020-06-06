package com.usian.controller;

import com.usian.feign.SearchItemFeign;
import com.usian.pojo.SearchItem;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/searchItem")
public class SearchItemController {

    @Autowired
    private SearchItemFeign searchItemFeign;

    /**
     * 索引
     * @return
     */
    @RequestMapping("/importAll")
    public Result importAll() {
        boolean bool = searchItemFeign.importAll();
        if (bool){
            return Result.ok(200);
        }
        return Result.error("500");
    }

    /**
     * 查询索引库
     * @param q
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/list")
    public List<SearchItem> selectByQ(String q, @RequestParam(defaultValue = "1") Long page,
                                      @RequestParam(defaultValue = "20") Integer pagesize){
        return searchItemFeign.selectByQ(q,page,pagesize);
    }
}

