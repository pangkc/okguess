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

public class OKGuessBankerApi extends AbstractBaseOKGuessApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(OKGuessBankerApi.class);

    public static final String calculateOdds(String para) {

        String url = address + "/issuer/product/calcodds";

        return sendRequest(url, para);
    }

    public static Project preCreateProject(CreateProjectDto project, List<CreateSolutionDto> createSolutions) {

        Map<String, Object> para = new HashMap<String, Object>();

        Map<String, Object> projectMap = new HashMap<String, Object>();
        projectMap.put("plevel", project.getPlevel());
        projectMap.put("passet", project.getPasset());
        projectMap.put("pbegintime", project.getPbegintime());
        projectMap.put("pcreator", project.getPcreator());
        projectMap.put("pdecimal", project.getPdecimal());
        projectMap.put("pdescription", project.getPdescription());
        projectMap.put("pendtime", project.getPendtime());
        projectMap.put("pmargin", project.getPmargin());
        projectMap.put("pmaxusercount", project.getPmaxusercount());
        projectMap.put("pname", project.getPname());
        projectMap.put("proom", project.getProom());
        projectMap.put("pq", project.getPq());
        projectMap.put("puserinfo", project.getPuserinfo());
        projectMap.put("puserproductno", project.getPuserproductno());

        para.put("product", projectMap);

        List<Map<String, Object>> solutions = new ArrayList<Map<String, Object>>();
        for (CreateSolutionDto createSolution : createSolutions) {
            Map<String, Object> solution = new HashMap<String, Object>();
            solution.put("sodds", createSolution.getSodds());
            solution.put("sname", createSolution.getSname());
            solution.put("solutionid", createSolution.getSolutionid());
            solutions.add(solution);
        }

        para.put("solutions", solutions);

        String url = address + "/issuer/product/preadd";
        String resultStr = sendRequest(url, para);
        DataResult<JSONObject> result = JSON.parseObject(resultStr, DataResult.class);
        DataResult<Project> dataResult = convertDataResult(result, Project.class);
        if (StringUtils.isEmpty(dataResult.getResult().getPissue())) {
            LOGGER.info("preCreateProject  error , result {}", dataResult);
            throw new LogicException(Constants.OKGUESS_SERVER_ERR);
        }
        return dataResult.getResult();
    }

    public static void activeProject(String puserproductno) {

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("puserproductno", puserproductno);

        String url = address + "/issuer/product/active";
        sendRequest(url, para);
    }

    public static DataResult<Project> getProjectDetail(Integer pissue) {

        String url = address + "/issuer/product/detail?pissue=" + pissue;
        String resultStr = sendGetRequest(url);
        DataResult<JSONObject> result = JSON.parseObject(resultStr, DataResult.class);
        return convertDataResult(result, Project.class);
    }

    public static PageResult<Order> getProjectOrder(Integer pissue, int page, int pageSize, String ostatus) {
        Map<String, Object> requestPara = new HashMap<String, Object>();
        requestPara.put("opissue", pissue);
        requestPara.put("page", page);
        requestPara.put("size", pageSize);
        if (!StringUtils.isEmpty(ostatus)) {
            requestPara.put("ostatus", ostatus);
        }

        String url = address + "/issuer/product/orders";
        String resultStr = sendRequest(url, requestPara);
        PageResult<JSONObject> result = JSON.parseObject(resultStr, PageResult.class);
        return convertPageResult(result, Order.class);
    }

    public static void clearProject(int pissue, long bodId, int solutionId) {

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("pissue", pissue);
        para.put("solutionid", solutionId);

        String url = address + "/issuer/product/liquidate?boc=" + bodId;
        sendRequest(url, para);
    }

    public static void refund(int pissue, long bodId) {

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("pissue", pissue);

        String url = address + "/issuer/product/refund?boc=" + bodId;
        sendRequest(url, para);
    }

    public static ListResult<Solution> getProjectSolution(Integer pissue) {

        String url = address + "/issuer/product/solutions?pissue=" + pissue;
        String resultStr = sendGetRequest(url);
        ListResult<JSONObject> result = JSON.parseObject(resultStr, ListResult.class);
        return convertListResult(result, Solution.class);
    }


    public static PageResult<Project> getCreatorProjectList(String userid, String proom, int page, int pageSize, int status) {
        Map<String, Object> requestPara = new HashMap<String, Object>();
        requestPara.put("pcreator", userid);
        requestPara.put("type", status);
        requestPara.put("proom", proom);
        requestPara.put("page", page);
        requestPara.put("size", pageSize);


        String url = address + "/issuer/products/mycreate";
        String resultStr = sendRequest(url, requestPara);
        PageResult<JSONObject> result = JSON.parseObject(resultStr, PageResult.class);
        return convertPageResult(result, Project.class);
    }


}
