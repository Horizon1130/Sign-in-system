package com.myway.platform.handler;

import com.myway.platform.api.ReturnResult;
import com.myway.platform.handler.exception.NotLoginException;
import com.myway.platform.mysql.authority.entity.SysUserInfo;
import com.myway.platform.utils.NetworkUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @description: 统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class InterfaceExceptionHandler {

    public HttpServletRequest request() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取系统用户会话信息
     *
     * @return
     */
    @SneakyThrows
    protected SysUserInfo getSessionSysUser() {
        SysUserInfo principal = (SysUserInfo) SecurityUtils.getSubject().getPrincipal();
        if (principal == null) {
            throw new NotLoginException();
        }
        return principal;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ReturnResult handleBindException(MethodArgumentNotValidException ex) {
        ReturnResult result = new ReturnResult();
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.warn("参数校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());
//        return ResponseEntity.ok(Response.fail(211,fieldError.getDefaultMessage(),fieldError.getDefaultMessage()));
        result.setErrorMsg(fieldError.getDefaultMessage());
        result.rollback();
        return result;
    }

    /**
     * 捕捉shiro无权限异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public ReturnResult handlerAuthorizationException(AuthorizationException e) {
        ReturnResult result = new ReturnResult();
        String operation = "用户请求无权限";
        try {
            SysUserInfo sessionSysUser = getSessionSysUser();
            HttpServletRequest request = request();

            if (sessionSysUser == null) {
                operation = "用户无登录";
                result.notLogin();
            } else {
                result.noAuthority();
            }
            result.setErrorMsg(operation);
            String ipAddress = NetworkUtil.getIpAddress(request);
            log.info("{}，IP：{}，URL：{}", operation, ipAddress, request.getRequestURI());
        } catch (IOException e1) {
            log.error("{}，获取请求IP地址异常。", operation);
        } catch (Exception e2) {
            log.error("{}，处理发生异常，{}。", operation, e2.getMessage());
        }
        result.rollback();
        return result;
    }

    @ExceptionHandler(NotLoginException.class)
    public ReturnResult handlerNotLoginException(NotLoginException e) {
        ReturnResult result = new ReturnResult();
        result.notLogin();
        log.debug(e.getMessage());
        return result;
    }

    /**
     * 捕捉全局出现的Exception异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ReturnResult handlerException(Exception e) {
        ReturnResult result = new ReturnResult();
        HttpServletRequest request = request();
        String requestURI = request.getRequestURI();
        log.error("接口地址：{}，请求失败。{}", requestURI, e.getMessage());
        result.setErrorMsg(String.format("操作异常：%s", e.getMessage()));
        result.rollback();
        return result;
    }

    /**
     * 处理http无请求服务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ReturnResult handlerNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ReturnResult result = new ReturnResult();
        result.setErrorMsg("无服务");
        result.rollback();
        return result;
    }

    /**
     * 处理shiro账号相关异常
     * 1、账号不存在
     * 2、密码不正确异常
     * 3、账号已被禁用
     *
     * @param e
     * @return
     */
    @ExceptionHandler({UnknownAccountException.class, IncorrectCredentialsException.class, DisabledAccountException.class})
    public ReturnResult handlerUnknownAccountException(Exception e) {
        ReturnResult result = new ReturnResult();
        if (e instanceof UnknownAccountException) {
            result.setErrorMsg("您输入的账号不存在。");
        } else if (e instanceof IncorrectCredentialsException) {
            result.setErrorMsg("您输入的账号与密码不正确。");
        } else if (e instanceof DisabledAccountException) {
            result.setErrorMsg("您的账号已被后台锁定，无法正常使用。");
        }
        result.rollback();
        return result;
    }

    /**
     * 数据校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({BindException.class})
    public ReturnResult handlerBindException(BindException e) {
        ReturnResult result = new ReturnResult();
        BindingResult bingResult = e.getBindingResult();
        if (bingResult.hasErrors()) {

            List<ObjectError> errors = bingResult.getAllErrors();

            if (!CollectionUtils.isEmpty(errors)) {
                FieldError fieldError = (FieldError) errors.get(0);
                result.setErrorMsg(fieldError.getDefaultMessage());
            }
        }
        result.rollback();
        return result;
    }
}
