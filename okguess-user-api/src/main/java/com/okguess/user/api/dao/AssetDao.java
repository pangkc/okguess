package com.okguess.user.api.dao;


import com.okguess.user.api.entity.Asset;

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
