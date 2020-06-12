package com.usian.feign;

import com.usian.pojo.TbItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface CartServiceFeign {

    @RequestMapping("/service/cart/selectCarByUserId")
    Map<String, TbItem> selectCartByUserId(@RequestParam String userId);

    @RequestMapping("/service/cart/insertCart")
    Boolean insertCart(@RequestParam String userId, Map<String, TbItem> cart);
}
