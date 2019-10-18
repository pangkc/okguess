package com.okguess.user.api.aop;

import java.lang.annotation.*;

/**
 * @Author hunter.pang
 * @Date 2018/9/10 下午9:35
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TokenCheck {

//    /boolean required() default false;
}

