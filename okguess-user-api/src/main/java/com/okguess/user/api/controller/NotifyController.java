package com.okguess.user.api.controller;

import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.OKGuessService;
import com.okguess.user.api.service.bastionpay.RSASign;
import com.okguess.user.api.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author hunter.pang
 * @Date 2019/8/8 下午2:04
 */
@Controller
@RequestMapping(value = "/api")
public class NotifyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyController.class);

    @Autowired
    private OKGuessService okGuessService;


    @RequestMapping(value = "/notify/bastion_pay", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Map<String, Object> bastionPayNotify(@RequestBody String notifyContent, HttpServletRequest request) throws Exception {

        LOGGER.info("bastionPayNotify start, notify content{}", notifyContent);

        if (StringUtils.isEmpty(notifyContent)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        Map<String, String> paraMap = new HashMap<String, String>();
        String[] paraArr = notifyContent.split("&");
        for (String para : paraArr) {
            String[] keyValuePair = para.split("=");
            paraMap.put(keyValuePair[0], keyValuePair[1]);
        }

        String signature = paraMap.get("signature");
        String merchantOrderNo = paraMap.get("merchantOrderNo");
        String tradeOrderNo = paraMap.get("tradeOrderNo");
        String status = paraMap.get("status");
        String assets = paraMap.get("assets");
        String amount = paraMap.get("amount");

        Map<String, Object> signMap = new TreeMap<String, Object>();
        signMap.put("merchantOrderNo", merchantOrderNo);
        signMap.put("tradeOrderNo", tradeOrderNo);
        signMap.put("status", status);
        signMap.put("assets", assets);
        signMap.put("amount", amount);

        validateSign(signMap, signature);

        okGuessService.activePayOrder(merchantOrderNo, assets, amount);


        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", "SUCCESS");
        return result;
    }

    private void validateSign(Map<String, Object> parameters, String signature) {
        StringBuilder tempString = new StringBuilder();
        for (String key : parameters.keySet()) {
            tempString.append(key);
            tempString.append("=");
            tempString.append(parameters.get(key));
            tempString.append("&");
        }
        String str = tempString.substring(0, tempString.lastIndexOf("&"));

        RSASign rsaSign = new RSASign(OKGuessConfig.BASTION_OPEN_API_PUB_KEY, OKGuessConfig.OKGUESS_TO_BASTION_OPEN_API_PRI_KEY);
        try {
            boolean result = rsaSign.verify(str, signature);
            if (!result) {
                throw new LogicException(Constants.BASTIONAPY_CALLBACK__ERR);
            }
        } catch (Exception e) {
            throw new LogicException(Constants.BASTIONAPY_CALLBACK__ERR);
        }
    }

}
