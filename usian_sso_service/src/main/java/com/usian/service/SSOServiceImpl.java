package com.usian.service;

import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.pojo.TbUserExample;
import com.usian.redis.RedisClient;
import com.usian.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SSOServiceImpl implements SSOService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${SESSION_EXPIRE}")
    private Long SESSION_EXPIRE;

    //用户校验
    @Override
    public Boolean checkUserInfo(String checkValue, Integer checkFlag) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //1、checkflag 1为username 2为phone
        if (checkFlag == 1) {
            criteria.andUsernameEqualTo(checkValue);
        } else if (checkFlag == 2) {
            criteria.andPhoneEqualTo(checkValue);
        }
        // 2、从tb_user表中查询数据
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        // 3、判断查询结果，如果查询到数据返回false。
        if (list == null || list.size() == 0) {
            // 4、如果没有返回true。
            return true;
        }
        // 5、如果有返回false。
        return false;
    }

    //用户注册
    @Override
    public Integer userRegister(TbUser tbUser) {
        //补齐数据
        Date date = new Date();
        tbUser.setCreated(date);
        tbUser.setUpdated(date);
        //加密密码
        String pwd = MD5Utils.digest(tbUser.getPassword());
        tbUser.setPassword(pwd);
        return tbUserMapper.insert(tbUser);
    }


    //用户登录
    @Override
    public Map userLogin(String username, String password) {
        //1、密码加密
        String pwd = MD5Utils.digest(password);
        
        //2、用户名 密码 是否匹配
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(pwd);
        List<TbUser> tbUsersList = tbUserMapper.selectByExample(tbUserExample);
        if (tbUsersList == null || tbUsersList.size() == 0){
            return null;
        }

        //3、存到redis并设置失效时间
        TbUser tbUser = tbUsersList.get(0);
        tbUser.setPassword(null);//防偷窥
        String token = UUID.randomUUID().toString();
        redisClient.set(USER_INFO+":"+token,tbUser);
        redisClient.expire(USER_INFO+":"+token,SESSION_EXPIRE);

        //4、封装到map
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userid",tbUser.getId());
        map.put("username",username);
        return map;
    }

    //查询用户的登录是否过去哦（redis中的token是否过期）
    @Override
    public TbUser getUserByToken(String token) {
        //redis中查token是否存在
        TbUser tbUser = (TbUser) redisClient.get(USER_INFO+":"+token);
        if (tbUser!=null){
            //充值token的过期时间
            redisClient.expire(USER_INFO+":"+token,SESSION_EXPIRE);
            return tbUser;
        }
        return null;
    }

    //退出登录
    @Override
    public Boolean logOut(String token) {
        //删除token
        return redisClient.del(USER_INFO+":"+token);
    }
}
