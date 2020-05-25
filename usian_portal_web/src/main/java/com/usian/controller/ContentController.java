package com.usian.controller;

import com.usian.feign.ContentServiceFeignClient;
import com.usian.utils.AdNode;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/content")
public class ContentController {
    @Autowired
    private ContentServiceFeignClient contentServiceFeignClient;

    /**
     * 查询首页大广告
     * @return
     */
    @RequestMapping("/selectFrontendContentByAD")
    public Result selectFrontendContentByAD(){
        List<AdNode> list = contentServiceFeignClient.selectFrontendContentByAD();
        if (list != null && list.size() > 0) {
            return Result.ok(list);
        }
        return Result.error("查无数据");
    }
}
