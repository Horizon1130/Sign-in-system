package com.myway.platform.controller.v1.restful;

import com.myway.platform.api.ReturnResult;
import com.myway.platform.controller.BaseController;
import com.myway.platform.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController extends BaseController {

    @PostMapping("login")
    public ReturnResult login(String account, String password, String code) {
        ReturnResult result = new ReturnResult();

//        Object verificationCode = request().getSession().getAttribute("code");
//        if (!StringUtils.isEmpty(verificationCode) && verificationCode.equals(code.toUpperCase())){
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(account, MD5Util.MD5Encode(password, "utf-8"));
            subject.login(usernamePasswordToken);
            String token = SecurityUtils.getSubject().getSession().getId().toString();
            result.set("token", token);
            result.set("mapper", "/master");
//        }else {
//            result.setErrorMsg("验证码错误");
//            result.rollback();
//        }
        return result;
    }

    @PostMapping("logout")
    public ReturnResult logout() {
        ReturnResult result = new ReturnResult();
        SecurityUtils.getSubject().logout();
        result.commit();
        return result;
    }

}
