package com.usian.controller;

import com.usian.feign.SSOServiceFeign;
import com.usian.pojo.TbUser;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/frontend/sso")
public class SSOController {
    @Autowired
    private SSOServiceFeign ssoServiceFeign;

    /**
     * 用户校验
     * @param checkValue
     * @param checkFlag
     * @return
     */
    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable String checkValue,
                                @PathVariable Integer checkFlag){
        Boolean bool = ssoServiceFeign.checkUserInfo(checkValue,checkFlag);
        if (bool){
            return Result.ok();
        }
        return Result.error("校验失败");
    }

    /**
     * 用户注册
     * @param tbUser
     * @return
     */
    @RequestMapping("/userRegister")
    public Result userRegister(TbUser tbUser){
        Integer inte = ssoServiceFeign.userRegister(tbUser);
        if (inte == 1){
            return Result.ok();
        }
        return Result.error("注册失败");
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/userLogin")
    public Result userLogin(String username,String password){
        Map map = ssoServiceFeign.userLogin(username,password);
        if (map != null){
            return Result.ok(map);
        }
        return Result.error("登录失败");
    }

    /**
     * 查询token是否过期
     * @param token
     * @return
     */
    @RequestMapping("/getUserByToken/{token}")
    public Result getUserByToken(@PathVariable String token){
        TbUser tbUser = ssoServiceFeign.getUserByToken(token);
        if (tbUser != null) {
            return Result.ok(200);
        }
        return Result.error("登录过期");
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @RequestMapping("/logOut")
    public Result logOut(String token){
        Boolean bool = ssoServiceFeign.logOut(token);
        if (bool){
            return Result.ok();
        }
        return Result.error("退出失败");
    }
}
