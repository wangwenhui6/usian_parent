package com.usian.controller;

import com.usian.feign.CartServiceFeign;
import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItem;
import com.usian.utils.CookieUtils;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/frontend/cart")
public class CartController {

    @Value("${CART_COOKIE_KEY}")
    private String CART_COOKIE_KEY;

    @Value("${CART_COOKIE_EXPIRE}")
    private Integer CART_COOKIE_EXPIRE;

    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    @Autowired
    private CartServiceFeign cartServiceFeign;


    /**
     * 删除购物车
     * @param itemId 商品id
     * @param userId 用户id
     * @param request 请求
     * @param response 响应
     * @return
     */
    @RequestMapping("/deletemFromCart")
    public Result deleteItemFromCart(Long itemId,String userId,HttpServletRequest request,HttpServletResponse response){
        try {
            if (StringUtils.isNotBlank(userId)) {
                //======================未登录状态===================
                Map<String, TbItem> cart = getCartFromCoolie(request);
                cart.remove(itemId.toString());
                addCliebtCookie(request,response,cart);
            }else {
                //======================登录状态=====================、
                //删除Reids缓存数据
                Map<String, TbItem> cart = getCartFromRedis(userId);
                cart.remove(itemId);
                //将新的购物车缓存到redis
                addCartToRedis(userId,cart);
            }
            return Result.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error("error");
        }
    }

    /**
     * 修改购物车
     * @param userId 用户id
     * @param itemId 商品id
     * @param num 商品数量
     * @param request 请求
     * @param response 响应
     * @return
     */
    @RequestMapping("/updateItemNum")
    public Result updateItemNum(String userId,Long itemId,Integer num,HttpServletRequest request,HttpServletResponse response){
        try {
            if (StringUtils.isNotBlank(userId)){
                //==================用户未登录===============
                //1、从cookie中获取购物车
                Map<String, TbItem> cart = getCartFromCoolie(request);
                //2、修改购物车中商品
                TbItem tbItem = cart.get(itemId.toString());
                tbItem.setNum(num);
                cart.put(itemId.toString(),tbItem);
                //3、把购物车写入cookie
                addCliebtCookie(request,response,cart);
            }else {
                //用户已登录
                Map<String, TbItem> cart = getCartFromRedis(userId);
                TbItem tbItem = cart.get(itemId.toString());
                if (tbItem != null) {
                    tbItem.setNum(num);
                }
                //将新的购物车缓存到Redis
                addCartToRedis(userId,cart);
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("error");
        }
    }

    /**
     * 查看购物车
     * @param userId 用户id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/showCart")
    public Result showCart(String userId,HttpServletRequest request,HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(userId)){
                //用户未登录状态
                List<TbItem> tbItemList = new ArrayList<>();
                Map<String, TbItem> cart = getCartFromCoolie(request);
                Set<String> keys = cart.keySet();
                for (String key :keys){
                    tbItemList.add(cart.get(key));
                }
            }else {
                //用户登录状态
                List<TbItem> itemList = new ArrayList<>();
                Map<String, TbItem> cart = getCartFromRedis(userId);
                Set<String> keySet = cart.keySet();
                for (String key :keySet) {
                    itemList.add(cart.get(key));
                }
                return Result.ok();
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("error");
        }
    }

    /**
     * 将商品加入购物车
     * @param itemId 商品id
     * @param userId 用户id
     * @param num 商品数量
     * @param request 请求
     * @param response 响应
     * @return
     */
    @RequestMapping("/addItem")
    public Result addItem(Long itemId, String userId, @RequestParam(defaultValue = "1")Integer num,
                          HttpServletRequest request, HttpServletResponse response){
        try {
            if (StringUtils.isBlank(userId)) {
                //==========================用户未登录==============================
                //1、从cookie中查询商品列表
                Map<String, TbItem> cart = getCartFromCoolie(request);

                //2、添加商品到购物车
                addItemToCart(cart, itemId, num);

                //3、把购物车商品列表写入cookie
                addCliebtCookie(request, response, cart);
            }else {
                //==========================用户已登录==============================
                //1、从redis中查询商品列表
                Map<String,TbItem> cart = getCartFromRedis(userId);
                //2、将商品添加到购物车
                addItemToCart(cart,itemId,num);
                //3、将购物车缓存到redis中
                Boolean addcartToRedis = addCartToRedis(userId,cart);
                if (addcartToRedis){
                    return Result.ok();
                }
                return Result.error("error");
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("error");
        }
    }

    /**
     * 把购物车商品列表写入Reids
     * @param userId
     * @param cart
     * @return
     */
    private Boolean addCartToRedis(String userId, Map<String, TbItem> cart) {
        return cartServiceFeign.insertCart(userId, cart);
    }

    /**
     * 从redis中查询购物车
     * @param userId
     * @return
     */
    private Map<String, TbItem> getCartFromRedis(String userId) {
        Map<String, TbItem> cart = cartServiceFeign.selectCartByUserId(userId);
        if (cart != null && cart.size() > 0) {
            return cart;
        }
        return new HashMap<String, TbItem>();
    }

    //把购物车商品列表写入cookie
    private void addCliebtCookie(HttpServletRequest request, HttpServletResponse response, Map<String, TbItem> cart) {
        String cartJson = JsonUtils.objectToJson(cart);
        CookieUtils.setCookie(request,response,CART_COOKIE_KEY,cartJson,CART_COOKIE_EXPIRE,true);
    }

    //添加商品到购物车
    private void addItemToCart(Map<String, TbItem> cart, Long itemId, Integer num) {
        //从购物车中取商品
        TbItem tbItem = cart.get(itemId.toString());

        if (tbItem != null) {
            //商品存在则数量像加
            tbItem.setNum(tbItem.getNum()+num);
        }else {
            //商品不存在则则添加商品
            tbItem = itemServiceFeignClient.selectItemInfo(itemId);
            tbItem.setNum(num);
        }
        cart.put(itemId.toString(),tbItem);
    }

    //从cookie中查询商品列表
    private Map<String, TbItem> getCartFromCoolie(HttpServletRequest request) {
        String cartJson = CookieUtils.getCookieValue(request, CART_COOKIE_KEY, true);
        if (StringUtils.isNotBlank(cartJson)) {
            //购物车已存在
            Map<String, TbItem> map = JsonUtils.jsonToMap(cartJson, TbItem.class);
            return map;
        }
        //购物车不存在
        return new HashMap<String,TbItem>();
    }

}
