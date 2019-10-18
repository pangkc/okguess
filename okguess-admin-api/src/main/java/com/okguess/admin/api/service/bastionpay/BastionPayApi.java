package com.okguess.admin.api.service.bastionpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okguess.admin.api.OKGuessConfig;
import com.okguess.admin.api.exception.LogicException;
import com.okguess.admin.api.utils.Constants;
import com.okguess.common.utils.HttpUtil;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

        return result;
    }

    public static void transfer(String orderNo,
                                long payeeUserId,
                                String asset,
                                BigDecimal amount,
                                String remark) {
        String api = "/open-api/trade/transfer";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();

        businessPara.put("merchant_id", MERCHANT_ID);
        businessPara.put("timestamp", System.currentTimeMillis());
        businessPara.put("notify_url", "http://example.com/notify_url");
        businessPara.put("merchant_transfer_no", orderNo);
        businessPara.put("payee_id", payeeUserId);
        businessPara.put("assets", asset);
        businessPara.put("amount", amount);
        businessPara.put("product_name", "transfer");
        businessPara.put("product_detail", "transfer");
        businessPara.put("remark", remark);

        Map<String, Object> result = sendRequest(url, businessPara);
        JSONObject jsonObject = (JSONObject) result.get("data");
        String status = jsonObject.get("status") == null ? null : String.valueOf(jsonObject.get("status"));
        if (!"3".equals(status)) {
            throw new LogicException(Constants.BASTOIN_PAY_ERR);
        }
    }


    public static void refund(String orderNo, String remark) {
        String api = "/open-api/trade/refund";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();

        businessPara.put("merchant_id", MERCHANT_ID);
        businessPara.put("timestamp", System.currentTimeMillis());
        businessPara.put("original_merchant_trade_no", orderNo);
        businessPara.put("merchant_refund_no", orderNo);
        if(!StringUtils.isEmpty(remark)){
            businessPara.put("remark", remark);
        }

        Map<String, Object> result = sendRequest(url, businessPara);
        JSONObject jsonObject = (JSONObject) result.get("data");
        String status = jsonObject.get("status") == null ? null : String.valueOf(jsonObject.get("status"));
        if (!"5".equals(status)) {
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
