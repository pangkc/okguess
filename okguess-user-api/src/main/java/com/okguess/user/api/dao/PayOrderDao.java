package com.okguess.user.api.dao;

import com.okguess.user.api.entity.PayOrder;

import java.util.List;
import java.util.Map;

/**
 * @Author hunter.pang
 * @Date 2018/9/5 上午11:11
 */
@Mapper
public interface PayOrderDao {

    int insert(PayOrder order);

    PayOrder findByOrderNo(String orderNo);

    int updateByOrderNo(String orderNo, PayOrder order);
}
