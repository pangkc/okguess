package com.okguess.user.api.service.okservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okguess.common.utils.HttpUtil;
import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.okservice.result.BaseResult;
import com.okguess.user.api.service.okservice.result.DataResult;
import com.okguess.user.api.service.okservice.result.ListResult;
import com.okguess.user.api.service.okservice.result.PageResult;
import com.okguess.user.api.utils.Constants;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hunter.pang
 * @Date 2019/9/28 下午1:45
 */
public class AbstractBaseOKGuessApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseOKGuessApi.class);

    public static final String address = OKGuessConfig.OKGUESS_SERVER_URL;


    protected static String sendRequest(String url, String para) {

        String result = null;
        BaseResult baseResult = null;
        try {
            StringEntity entity = new StringEntity(para, Charset.forName("utf-8"));
            result = HttpUtil.postWithPara(url, entity);
            LOGGER.info("url {}, result {}", url, result);

            baseResult = JSON.parseObject(result, BaseResult.class);
            result = JSON.toJSONString(baseResult.getResult());
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new LogicException(Constants.OKGUESS_SERVER_ERR);
        }

        if (!"0".equals(baseResult.getErr())) {
            throw new LogicException(Constants.OKGUESS_SERVER_ERR + "-" + baseResult.getErr());
        }

        return result;
    }


    protected static String sendRequest(String url, Map<String, Object> requestPara) {

        String result = null;
        BaseResult baseResult = null;
        try {
            StringEntity entity = new StringEntity(JSON.toJSONString(requestPara), Charset.forName("utf-8"));
            result = HttpUtil.postWithPara(url, entity);
            LOGGER.info("url {}, result {}", url, result);

            baseResult = JSON.parseObject(result, BaseResult.class);
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new LogicException(Constants.OKGUESS_SERVER_ERR);
        }

        if (!"0".equals(baseResult.getErr())) {
            throw new LogicException(Constants.OKGUESS_SERVER_ERR, baseResult.getErr(), baseResult.getErrmsg());
        }

        return result;
    }

    protected static String sendGetRequest(String url) {

        String result = null;
        BaseResult baseResult = null;
        try {
            result = HttpUtil.get(url);
            LOGGER.info("url {}, result {}", url, result);

            baseResult = JSON.parseObject(result, BaseResult.class);
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new LogicException(Constants.OKGUESS_SERVER_ERR);
        }

        if (!"0".equals(baseResult.getErr())) {
            throw new LogicException(Constants.OKGUESS_SERVER_ERR, baseResult.getErr(), baseResult.getErrmsg());
        }

        return result;
    }

    protected static <T> PageResult<T> convertPageResult(PageResult<JSONObject> para, Class<T> tClass) {
        List<JSONObject> jsonObjects = para.getResult().getList();
        List<T> objects = new ArrayList<T>();

        PageResult<T> result = new PageResult<T>();
        BeanUtils.copyProperties(para, result);

        if (jsonObjects != null) {
            for (JSONObject jsonObject : jsonObjects) {
                objects.add(JSONObject.parseObject(jsonObject.toJSONString(), tClass));
            }
            result.getResult().getList().clear();
            result.getResult().getList().addAll(objects);
        }
        return result;
    }

    protected static <T> DataResult<T> convertDataResult(DataResult<JSONObject> para, Class<T> tClass) {

        DataResult<T> result = new DataResult<T>();
        BeanUtils.copyProperties(para, result);

        JSONObject data = para.getResult();
        if (data != null) {
            result.setResult(JSON.parseObject(data.toJSONString(), tClass));
        }

        return result;
    }

    protected static <T> ListResult<T> convertListResult(ListResult<JSONObject> para, Class<T> tClass) {
        Map<String, List<JSONObject>> mapResult = para.getResult();


        Map<String, List<T>> newMapResult = new HashMap<String, List<T>>();
        List<T> dataList = new ArrayList<T>();
        if (mapResult != null && mapResult.get("list") != null) {
            for (JSONObject jsonObject : mapResult.get("list")) {
                dataList.add(JSON.parseObject(jsonObject.toJSONString(), tClass));
            }
        }
        newMapResult.put("list", dataList);

        ListResult<T> result = new ListResult<T>();
        BeanUtils.copyProperties(para, result);
        result.setResult(newMapResult);

        return result;
    }
}
