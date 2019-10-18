package com.okguess.admin.api.dao;


import com.okguess.admin.api.entity.UserInfo;

/**
 * @Author hunter.pang
 * @Date 2018/9/5 上午11:11
 */
@Mapper
public interface UserInfoDao {

    long insert(UserInfo userInfo);

    UserInfo findByBpUesrId(long bpUserId);

}
