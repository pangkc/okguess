package com.okguess.user.api.aop;


import com.alibaba.fastjson.JSONObject;
import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.entity.UserInfo;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.DataCache;
import com.okguess.user.api.service.OKGuessService;
import com.okguess.user.api.service.bastionpay.BastionPayApi;
import com.okguess.user.api.utils.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Aspect
public class ControllerAspect implements Ordered {

    private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);


    @Autowired
    private OKGuessService okGuessService;


    @Pointcut("execution(* com.okguess.user.api.controller..*(..))")
    public void methodCall() {

    }

    @Around("methodCall()")
    private Object doIntercept(ProceedingJoinPoint pjp) throws Throwable {

        Object[] args = pjp.getArgs();
        HttpServletRequest request = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
            }
        }
        Object target = pjp.getTarget();
        MethodSignature msig = (MethodSignature) pjp.getSignature();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

        try {

            checkToken(currentMethod, request);

            Object methodResult = (Map<String, Object>) pjp.proceed();
            if (methodResult instanceof Map) {
                return constructResponse((Map<String, Object>) methodResult, null);
            } else if (methodResult instanceof String) {
                return constructResponse((String) methodResult, null);
            }
            throw new RuntimeException("unknown result type");
        } catch (LogicException e) {
            String errCode = e.getCode();
            String errMsg = e.getMsg();

            StringBuilder argStr = new StringBuilder();
            for (Object arg : args) {
                argStr.append(arg == null ? "null" : arg.toString());
                argStr.append(", ");
            }

            logger.warn(pjp.getSignature().toShortString() + " warn, args:{} , logic exception code:{}, message: {}", argStr
                    .toString(), errCode, errMsg, e);

            return constructErrResponse(errCode, errMsg);
        } catch (Exception e) {
            StringBuilder argStr = new StringBuilder();
            for (Object arg : args) {
                argStr.append(arg == null ? "null" : arg.toString());
                argStr.append(", ");
            }
            logger.error(pjp.getSignature().toShortString() + " error, args : {} ", argStr.toString(), e);

            return constructErrResponse("500", null);
        }
    }


    private Map<String, Object> constructErrResponse(String code, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("message", message == null ? "" : message);

        return result;
    }

    private Map<String, Object> constructResponse(Map<String, Object> dataMap, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (dataMap != null) {
            result.put("data", dataMap);
        }
        result.put("code", 0);
        result.put("message", message == null ? "" : message);

        return result;
    }

    private Map<String, Object> constructResponse(String data, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (data != null) {
            result.put("data", data);
        }
        result.put("code", 0);
        result.put("message", message == null ? "" : message);

        return result;
    }


    private void checkToken(Method currentMethod, HttpServletRequest request) throws Exception {
        if (currentMethod.isAnnotationPresent(TokenCheck.class)) {
            String token = getToken(request);
            JSONObject userInfo = getUserInfo(token);

            if (token == null || userInfo == null) {
                throw new LogicException(Constants.NEED_LOGIN, OKGuessConfig.BASTION_PAY_OAUTH_URL);
            }

            request.setAttribute(Constants.USER_ID, userInfo.get("user_id"));
            request.setAttribute(Constants.USER_NICK, userInfo.get("nick"));
            request.setAttribute(Constants.USER_PHOTO, userInfo.get("photo"));
            request.setAttribute("token", token);
        }
    }

    private JSONObject getUserInfo(String token) {
        Object userIdObj = DataCache.getOneDayCache().getIfPresent(token);
        if (userIdObj == null) {
            throw new LogicException(Constants.NEED_LOGIN, OKGuessConfig.BASTION_PAY_OAUTH_URL);
        }
        return (JSONObject) userIdObj;

    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("TOKEN");
        if (StringUtils.isEmpty(token)) {
            String authCode = request.getParameter("auth_code");
            if (StringUtils.isEmpty(authCode)) {
                throw new LogicException(Constants.NEED_LOGIN, OKGuessConfig.BASTION_PAY_OAUTH_URL);
            }

            JSONObject user = BastionPayApi.getUserInfo(authCode);
            if (user == null) {
                throw new LogicException(Constants.OAUTH_INFO_ERROR);
            }

            UserInfo userInfo = new UserInfo();
            userInfo.setBpUserId(Long.valueOf(user.get("user_id").toString()));
            userInfo.setBpNick((String)user.get("nick"));
            userInfo.setBpPhoto((String)user.get("photo"));
            okGuessService.saveByBpUserIdIfRequired(userInfo);

            token = UUID.randomUUID().toString();
            DataCache.getOneDayCache().put(token, user);
        } else {
            Object userIdObj = DataCache.getOneDayCache().getIfPresent(token);
            if (userIdObj == null) {
                throw new LogicException(Constants.NEED_LOGIN, OKGuessConfig.BASTION_PAY_OAUTH_URL);
            }
            DataCache.getOneDayCache().put(token, userIdObj);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}