package com.okguess.user.api.service.bastionpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okguess.common.utils.HttpUtil;
import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.DataCache;
import com.okguess.user.api.utils.Constants;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author hunter.pang
 * @Date 2019/9/2 下午1:55
 */
public class BastionPayApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(BastionPayApi.class);

    private static final String BASE_URL = OKGuessConfig.BASTION_PAY_OPEN_API;

    private static final String MERCHANT_ID = OKGuessConfig.BASTION_PAY_MERCHANT_ID;

    public static final Map<String, String> HEADER = new HashMap<String, String>();

    static {
        HEADER.put("Content-Type", "application/json; charset=utf-8");
    }

    public static List<Map<String, String>> availAssets() {

        String key = "bastion.avail.assets";
        Object obj = DataCache.getTenMinCache().getIfPresent(key);
        if (obj != null) {
            return (List<Map<String, String>>) obj;
        }

        String api = "/open-api/trade/avail_assets";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();

        Map<String, Object> resultMap = sendRequest(url, businessPara);
        List datalist = (List) ((JSONObject) resultMap.get("data")).get("assets");
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (Object data : datalist) {
            if (data instanceof JSONObject) {
                Map<String, String> mapData = JSON.parseObject(((JSONObject) data).toJSONString(), Map.class);
                result.add(mapData);
            } else if (data instanceof Map) {
                Map<String, String> mapData = (Map) data;
                result.add(mapData);
            } else {
                throw new RuntimeException();
            }
        }
        DataCache.getTenMinCache().put(key, result);
        return result;
    }

    public static Map<String, String> getAssets(String assets) {
        List<Map<String, String>> avaliAssets = availAssets();
        if (CollectionUtils.isEmpty(avaliAssets)) {
            return null;
        }
        for (Map<String, String> assetsMap : avaliAssets) {
            if (assets.equals(assetsMap.get("assets"))) {
                return assetsMap;
            }
        }
        return null;
    }

    public static String createWapOrder(String orderNo,
                                        String assets,
                                        String amount,
                                        String productName,
                                        String productDetail,
                                        String returnUrl,
                                        String remark) {
        String api = "/open-api/trade/create_wap_trade";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();

        businessPara.put("merchant_id", MERCHANT_ID);
        businessPara.put("timestamp", System.currentTimeMillis());
        businessPara.put("notify_url", OKGuessConfig.OKGUESS_API_URL + "/api/notify/bastion_pay");
        businessPara.put("merchant_trade_no", orderNo);
        businessPara.put("payee_id", OKGuessConfig.BASTION_PAY_PAYEE_ID);
        businessPara.put("assets", assets);
        businessPara.put("amount", amount);
        businessPara.put("product_name", productName);
        businessPara.put("product_detail", productDetail);
        businessPara.put("expire_time", 1000);
        if (!StringUtils.isEmpty(remark)) {
            businessPara.put("remark", remark);
        }
        if (!StringUtils.isEmpty(returnUrl)) {
            businessPara.put("return_url", returnUrl);
        }

        //businessPara.put("show_url", "http://example.com/show_url");
        Map<String, Object> result = sendRequest(url, businessPara);
        JSONObject jsonObject = (JSONObject) result.get("data");
        return (String) jsonObject.get("form");
    }


    public static JSONObject getUserInfo(String auth_code) {
        String api = "/open-api/oauth/code_to_token";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();

        businessPara.put("merchant_id", MERCHANT_ID);
        businessPara.put("timestamp", System.currentTimeMillis());
        businessPara.put("auth_code", auth_code);

        //businessPara.put("signature", signature(businessPara));
        //businessPara.put("sign_type", "RSA");

        Map<String, Object> result = sendRequest(url, businessPara);
        return (JSONObject) result.get("data");
    }


    public static void refund(String orderNo, String remark) {
        String api = "/open-api/trade/refund";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();

        businessPara.put("merchant_id", MERCHANT_ID);
        //businessPara.put("sign_type", "RSA");
        businessPara.put("timestamp", System.currentTimeMillis());
        businessPara.put("original_merchant_trade_no", orderNo);
        businessPara.put("merchant_refund_no", orderNo);
        if (!StringUtils.isEmpty(remark)) {
            businessPara.put("remark", remark);
        }

        //String signatue = signature(businessPara);
        //businessPara.put("signature", signatue);

        Map<String, Object> result = sendRequest(url, businessPara);
        JSONObject jsonObject = (JSONObject) result.get("data");
        String status = jsonObject.get("status").toString();
        if (!Constants.BASTIONPAY_REFUND.equals(status)) {
            throw new LogicException(Constants.BASTOIN_PAY_ERR);
        }
    }

    private static Map<String, Object> sendRequest(String url, Map<String, Object> businessPara) {

        if (!businessPara.isEmpty()) {

            String signature = signature(businessPara);
            businessPara.put("signature", signature);
        }

        String body = JSON.toJSONString(businessPara);

        try {
            String result = HttpUtil.postWithStringEntityParaAndHeader(url, new StringEntity(body, Charset.forName("utf-8")), HEADER);

            LOGGER.info("bastion pay url {}, result {}", url, result);

            Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
            if (!"0".equals(resultMap.get("code").toString())) {
                throw new LogicException(Constants.BASTOIN_PAY_ERR, "" + resultMap.get("code"), (String) resultMap.get("message"));
            }

            return resultMap;
        } catch (LogicException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new LogicException(Constants.BASTOIN_PAY_ERR);
        }
    }


    private static String signature(Map<String, Object> parameters) {
        StringBuilder tempString = new StringBuilder();
        for (String key : parameters.keySet()) {
            tempString.append(key);
            tempString.append("=");
            tempString.append(parameters.get(key));
            tempString.append("&");
        }

        String str = tempString.substring(0, tempString.lastIndexOf("&"));

        RSASign merchantSign = new RSASign(OKGuessConfig.BASTION_OPEN_API_PUB_KEY, OKGuessConfig.OKGUESS_TO_BASTION_OPEN_API_PRI_KEY);
        return merchantSign.sign(str);
    }
}
