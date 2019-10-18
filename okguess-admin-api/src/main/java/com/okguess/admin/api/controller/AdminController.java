package com.okguess.admin.api.controller;

import com.alibaba.fastjson.JSON;
import com.okguess.admin.api.dao.AssetDao;
import com.okguess.admin.api.entity.Asset;
import com.okguess.admin.api.exception.LogicException;
import com.okguess.admin.api.service.bastionpay.BastionPayApi;
import com.okguess.admin.api.utils.Constants;
import org.apache.commons.collections.MapUtils;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hunter.pang
 * @Date 2019/10/1 下午8:45
 */
@Controller
@RequestMapping(value = "/api")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AssetDao assetDao;


    @RequestMapping(value = "/assets", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> availAssets(HttpServletRequest request) throws Exception {

        LOGGER.info("availAssets start");

        List<Map<String, String>> assets = BastionPayApi.availAssets();
        List<Asset> assetsDb = assetDao.findAll();
        if (assets != null) {
            for (Map<String, String> assetMap : assets) {
                boolean open = false;
                for (Asset asset : assetsDb) {
                    if (asset.getAsset().equals(assetMap.get("assets")) && (asset.getStatus() == 1)) {
                        open = true;
                        break;
                    }
                }
                assetMap.put("open", open ? "1" : "0");
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("assets", assets);

        return result;
    }

    @RequestMapping(value = "/open_asset", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> openAsset(HttpServletRequest request) throws Exception {

        LOGGER.info("openAsset start");

        String assetName = request.getParameter("asset");
        if (StringUtils.isEmpty(assetName)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        Asset asset = new Asset();
        asset.setAsset(assetName);
        asset.setStatus(1);
        assetDao.insert(asset);

        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }

    @RequestMapping(value = "/close_asset", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> closeAsset(HttpServletRequest request) throws Exception {

        LOGGER.info("closeAsset start");

        String assetName = request.getParameter("asset");
        if (StringUtils.isEmpty(assetName)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        Asset asset = new Asset();
        asset.setAsset(assetName);
        asset.setStatus(0);
        assetDao.insert(asset);

        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }

    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> refund(HttpServletRequest request) throws Exception {

        String orderNo = request.getParameter("orderno");

        LOGGER.info("refund start , order no {}", orderNo);

        if (StringUtils.isEmpty(orderNo)) {
            throw new LogicException(Constants.PARA_ERR);
        }
        String remark = request.getParameter("remark");

        BastionPayApi.refund(orderNo, remark);

        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Map<String, Object> transfer(@RequestBody String para, HttpServletRequest request) throws Exception {

        Map<String, Object> paraMao = JSON.parseObject(para, Map.class);
        String orderNo = MapUtils.getString(paraMao, "orderno");

        LOGGER.info("transfer start, order no {}", orderNo);

        if (StringUtils.isEmpty(para)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        if (StringUtils.isEmpty(orderNo)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String payee = MapUtils.getString(paraMao, "payee");
        if (StringUtils.isEmpty(payee)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String asset = MapUtils.getString(paraMao, "asset");
        if (StringUtils.isEmpty(asset)) {
            throw new LogicException(Constants.PARA_ERR);
        }
        String amount = MapUtils.getString(paraMao, "amount");
        if (StringUtils.isEmpty(amount)) {
            throw new LogicException(Constants.PARA_ERR);
        }
        BigDecimal amountValue = new BigDecimal(amount);
        if (amountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String remark = MapUtils.getString(paraMao, "remark");

        BastionPayApi.transfer(orderNo, Long.valueOf(payee), asset, amountValue, remark);

        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }


}
