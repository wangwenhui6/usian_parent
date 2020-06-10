package com.usian.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//redisTemplate封装
@Component
public class RedisClient {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 设置key的失效时间
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key,Long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取key的过期时间
     * @param key
     * @return
     */
    public Long ttl(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key){
        return redisTemplate.hasKey(key);
    }

    //++++++++++++++++++++++++++++++String+++++++++++++++++++++++++++

    /**
     * String取值
     * @param key
     * @return
     */
    public Object get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通存值
     * @param key
     * @param value
     * @return
     */
    public Boolean set(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key
     * @return
     */
    public Boolean del(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 递增
     * @param key
     * @param delta
     * @return
     */
    public Long incr(String key,Long delta){
        if (delta<0) {
            throw  new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,delta);
    }

    /**
     * 递减
     * @param key
     * @param delta
     * @return
     */
    public Long decr(String key,Long delta) {
        if (delta<0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, -delta);
    }

    //***********************************hash***********************

    /**
     * hashget
     * @param key
     * @param item
     * @return
     */
    public Object hget(String key,String item) {
        return redisTemplate.opsForHash().get(key,item);
    }

    /**
     * 向hash表中放入数据，如果不存在将创建
     * @param key
     * @param item
     * @param value
     * @return
     */
    public Boolean hset(String key,String item,Object value) {
        try {
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key
     * @param item
     */
    public void hdel(String key,Object... item) {
        redisTemplate.opsForHash().delete(key,item);
    }

    //====================================set==================================

    /**
     * 根据key获取set中所有值
     * @param key
     * @return
     */
    public Set<Object> smembers(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将数据放入 set缓存
     * @param key
     * @param value
     * @return
     */
    public Long sadd(String key, Object... value) {
        try {
            return redisTemplate.opsForSet().add(key,value);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除值为value的
     * @param key
     * @param value
     * @return
     */
    public Long srem(String key,Object... value) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, value);
            return count;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&list&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&}

    /**
     * 获取kust缓存的内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lrang(String key, long start, long end){
        try {
            return redisTemplate.opsForList().range(key, start, end);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key
     * @param value
     * @return
     */
    public Boolean lpush(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key
     * @param value
     * @return
     */
    public Boolean push(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lrem(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 分布式锁
     * @param key
     * @param value
     * @param time
     * @return
     */
    public Boolean setnx(String key,Object value,long time) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}