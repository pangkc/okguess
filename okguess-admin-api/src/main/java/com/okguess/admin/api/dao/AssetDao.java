package com.okguess.admin.api.dao;


import com.okguess.admin.api.entity.Asset;
import com.okguess.admin.api.entity.UserInfo;

import java.util.List;

/**
 * @Author hunter.pang
 * @Date 2018/9/5 上午11:11
 */
@Mapper
public interface AssetDao {

    long insert(Asset assetInfo);

    List<Asset> findAll();
}
