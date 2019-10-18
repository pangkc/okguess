package com.okguess.user.api.controller;

import com.okguess.user.api.aop.TokenCheck;
import com.okguess.user.api.dao.AssetDao;
import com.okguess.user.api.entity.Asset;
import com.okguess.user.api.service.OKGuessService;
import com.okguess.user.api.service.bastionpay.BastionPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hunter.pang
 * @Date 2019/8/8 下午2:04
 */
@Controller
@RequestMapping(value = "/api")
public class CommonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private OKGuessService okGuessService;

    @Autowired
    private AssetDao assetDao;

    @RequestMapping(value = "/avail_assests", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> availAssets(HttpServletRequest request) throws Exception {

        LOGGER.info("availAssets start");

        List<Map<String, String>> assetsResult = new ArrayList<Map<String, String>>();
        List<Map<String, String>> assets = BastionPayApi.availAssets();

        List<Asset> assetsDb = assetDao.findAll();
        if (assets != null) {
            for (Map<String, String> assetMap : assets) {
                boolean open = false;
                for (Asset asset : assetsDb) {
                    if (asset.getAsset().equals(assetMap.get("assets")) && (asset.getStatus() == 1)) {
                        assetsResult.add(assetMap);
                        break;
                    }
                }
            }
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("assets", assetsResult);

        return result;
    }


    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> oauth(HttpServletRequest request) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("token", request.getAttribute("token"));

        return result;
    }
}
