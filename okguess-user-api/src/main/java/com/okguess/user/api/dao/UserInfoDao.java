package com.okguess.user.api.dao;

import com.okguess.user.api.entity.PayOrder;
import com.okguess.user.api.entity.UserInfo;

/**
 * @Author hunter.pang
 * @Date 2018/9/5 上午11:11
 */
@Mapper
public interface UserInfoDao {

    long insert(UserInfo userInfo);

    UserInfo findByBpUesrId(long bpUserId);

}
