package com.usian.feign;

import com.usian.pojo.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("usian-order-service")
public interface OrderServiceFeign {

    @RequestMapping("/order/service/insertOrder")
    String insertOrder(OrderInfo orderInfo);
}
