package com.usian.interceptor;

import com.usian.feign.SSOServiceFeign;
import com.usian.pojo.TbUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserLoginInterception implements HandlerInterceptor {

    @Autowired
    private SSOServiceFeign ssoServiceFeign;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //对用户token做判断
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)){
            return false;
        }
        //如果用户token不为空，则校验用户再redis中是否失败
        TbUser tbUser = ssoServiceFeign.getUserByToken(token);
        if (tbUser == null){
            return false;
        }
        return true;
    }
}
