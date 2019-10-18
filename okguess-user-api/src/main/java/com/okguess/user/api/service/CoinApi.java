package com.okguess.user.api.service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.okguess.common.utils.HttpUtil;
import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author hunter.pang
 * @Date 2018/10/7 下午3:59
 */
public class CoinApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinApi.class);

    private static Cache<String, Object> cache = DataCache.getOneMinCache();

    public static List<Map<String, Object>> getCoinQuote(String fromCoin, String toMoney) {

        List<Map<String, Object>> cacheResult = findFromCache(fromCoin, toMoney);
        if (cacheResult != null) {
            return cacheResult;
        }

        String url = OKGuessConfig.COIN_URL;
        if (!StringUtils.isEmpty(fromCoin)) {

            url += "?from=" + fromCoin;
        }
        if (!StringUtils.isEmpty(toMoney)) {
            url += "&to=" + toMoney;
        }
        try {
            LOGGER.info("reqeust coin quote url {}", url);
            String result = HttpUtil.get(url);
            Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
            LOGGER.info("reqeust coin quote result {}", resultMap);
            int err = (Integer) resultMap.get("err");
            if (err != 0) {
                String errMsg = (String) resultMap.get("errmsg");
                throw new LogicException(Constants.COIN_QUOTE_ERROR, String.valueOf(err), errMsg);
            }
            List<Map<String, Object>> data = (List) resultMap.get("quotes");
            saveToCache(fromCoin, toMoney, data);
            return data;
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new LogicException(Constants.COIN_QUOTE_ERROR);
        }
    }

    private static List<Map<String, Object>> findFromCache(String fromCoin, String toMoney) {
        String key = null;
        if (StringUtils.isEmpty(fromCoin) && StringUtils.isEmpty(toMoney)) {
            key = "coin_quote_all";
        } else {
            key = fromCoin + toMoney;
        }

        return (List<Map<String, Object>>) cache.getIfPresent(key);
    }

    private static void saveToCache(String fromCoin, String toMoney, List<Map<String, Object>> data) {
        String key = null;
        if (StringUtils.isEmpty(fromCoin) && StringUtils.isEmpty(toMoney)) {
            key = "coin_quote_all";
        } else {
            key = fromCoin + toMoney;
        }

        cache.put(key, data);
    }
}
