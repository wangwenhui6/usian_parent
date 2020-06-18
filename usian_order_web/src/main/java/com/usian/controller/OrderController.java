package com.usian.controller;

import com.usian.feign.CartServiceFeign;
import com.usian.feign.OrderServiceFeign;
import com.usian.pojo.OrderInfo;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend/order")
public class OrderController {

    @Autowired
    private CartServiceFeign cartServiceFeign;

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    /**
     * 展示购物车，验证用户登录
     * @param ids
     * @param userId
     * @return
     */
    @RequestMapping("/goSettlement")
    public Result goSettlement(String[] ids, String userId) {
        //获取购物车
        Map<String, TbItem> cart = cartServiceFeign.selectCartByUserId(userId);
        //从购物车中获取选中的商品
        List<TbItem> list = new ArrayList<>();
        for (String id : ids) {
            list.add(cart.get(id));
        }
        if (list.size()>0){
            return Result.ok(list);
        }
        return Result.error("error");
    }

    /**
     * 创建订单
     * @param orderItem
     * @param tbOrder
     * @param tbOrderShipping
     * @return
     */
    @RequestMapping("/insertOrder")
    public Result insertOrder(String orderItem, TbOrder tbOrder, TbOrderShipping tbOrderShipping) {
        //一个request中只包含一个requestbiody，所以feign不支持多个@requestbody
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderItem(orderItem);
        orderInfo.setTbOrder(tbOrder);
        orderInfo.setTbOrderShipping(tbOrderShipping);
        String orderId = orderServiceFeign.insertOrder(orderInfo);
        if (orderId != null) {
            //返回订单编号
            return Result.ok(orderId);
        }
        return Result.error("error");
    }
}
