package com.okguess.user.api.service.okservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okguess.common.utils.HttpUtil;
import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.dto.CreateOrderDto;
import com.okguess.user.api.dto.CreateProjectDto;
import com.okguess.user.api.dto.CreateSolutionDto;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.okservice.data.Order;
import com.okguess.user.api.service.okservice.data.Project;
import com.okguess.user.api.service.okservice.data.Solution;
import com.okguess.user.api.service.okservice.result.BaseResult;
import com.okguess.user.api.service.okservice.result.DataResult;
import com.okguess.user.api.service.okservice.result.ListResult;
import com.okguess.user.api.service.okservice.result.PageResult;
import com.okguess.user.api.utils.Constants;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author hunter.pang
 * @Date 2019/8/9 下午2:18
 */

public class OKGuessPlayerApi extends AbstractBaseOKGuessApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(OKGuessPlayerApi.class);

    public static PageResult<Project> getInProcessingProjectList(String proom, int page, int pageSize) {
        Map<String, Object> requestPara = new HashMap<String, Object>();
        requestPara.put("page", page);
        requestPara.put("size", pageSize);
        requestPara.put("proom", proom);

        String url = address + "/player/products/going";
        String resultStr = sendRequest(url, requestPara);
        PageResult<JSONObject> result = JSON.parseObject(resultStr, PageResult.class);
        return convertPageResult(result, Project.class);
    }


    public static DataResult<Project> getProjectDetail(Integer pissue) {

        String url = address + "/player/product/detail?pissue=" + pissue;
        String resultStr = sendGetRequest(url);
        DataResult<JSONObject> result = JSON.parseObject(resultStr, DataResult.class);
        return convertDataResult(result, Project.class);
    }


    public static ListResult<Solution> getProjectSolution(Integer pissue) {

        String url = address + "/player/product/solutions?pissue=" + pissue;
        String resultStr = sendGetRequest(url);
        ListResult<JSONObject> result = JSON.parseObject(resultStr, ListResult.class);
        return convertListResult(result, Solution.class);
    }


    public static Order preCreateOrder(CreateOrderDto createOrderDto, Integer bocId) {

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("opissue", createOrderDto.getOpissue());
        para.put("osolutionid", createOrderDto.getOsolutionid());
        para.put("ousercount", createOrderDto.getOusercount());
        para.put("ouserid", createOrderDto.getOuserid());
        para.put("ousername", createOrderDto.getOusername());
        para.put("ouserorderno", createOrderDto.getOuserorderno());

        String url = address + "/player/order/preadd?boc=" + bocId;
        String resultStr = sendRequest(url, para);
        DataResult<JSONObject> result = JSON.parseObject(resultStr, DataResult.class);
        DataResult<Order> dataResult = convertDataResult(result, Order.class);
//        if (StringUtils.isEmpty(dataResult.getResult().getOrderno())) {
//            LOGGER.info("preCreateOrder  error , result {}", dataResult);
//            throw new LogicException(Constants.OKGUESS_SERVER_ERR);
//        }
        return dataResult.getResult();
    }

    public static Order activeOrder(String ouserorderno, int bodId) {

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("ouserorderno", ouserorderno);

        String url = address + "/player/order/active?boc=" + bodId;
        String resultStr = sendRequest(url, para);

        DataResult<JSONObject> result = JSON.parseObject(resultStr, DataResult.class);

        return convertDataResult(result, Order.class).getResult();
    }


    public static DataResult<Order> getOrderDetail(String ouserorderno) {

        String url = address + "/player/order/detail?ouserorderno=" + ouserorderno;
        String resultStr = sendGetRequest(url);
        DataResult<JSONObject> result = JSON.parseObject(resultStr, DataResult.class);
        return convertDataResult(result, Order.class);
    }


    public static PageResult<Order> getPlayerParticipateProjectOrder(String ouserid, String proom, Integer opissue, int page, int pageSize) {
        Map<String, Object> requestPara = new HashMap<String, Object>();
        requestPara.put("ouserid", ouserid);
        requestPara.put("opissue", opissue);
        requestPara.put("page", page);
        requestPara.put("size", pageSize);
        if (!StringUtils.isEmpty(proom)) {
            requestPara.put("proom", proom);
        }

        String url = address + "/player/product/orders";
        String resultStr = sendRequest(url, requestPara);
        PageResult<JSONObject> result = JSON.parseObject(resultStr, PageResult.class);
        return convertPageResult(result, Order.class);
    }


    public static PageResult<Project> getPlayerParticipateList(String ouserid, String proom, int page, int pageSize, int status) {
        Map<String, Object> requestPara = new HashMap<String, Object>();
        requestPara.put("ouserid", ouserid);
        requestPara.put("type", status);
        requestPara.put("oproom", proom);
        requestPara.put("page", page);
        requestPara.put("size", pageSize);


        String url = address + "/player/products/myjoin";
        String resultStr = sendRequest(url, requestPara);
        PageResult<JSONObject> result = JSON.parseObject(resultStr, PageResult.class);
        return convertPageResult(result, Project.class);
    }


    public static void main(String[] args) {

        System.out.println(getPlayerParticipateList("55", "11", 1, 10, 2));
    }
}
