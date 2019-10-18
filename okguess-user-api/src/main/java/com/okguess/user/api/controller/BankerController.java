package com.okguess.user.api.controller;

import com.alibaba.fastjson.JSON;
import com.okguess.user.api.aop.TokenCheck;
import com.okguess.user.api.dto.CreateProjectSolutionDto;
import com.okguess.user.api.dto.CreateSolutionDto;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.bastionpay.BastionPayApi;
import com.okguess.user.api.service.okservice.OKGuessBankerApi;
import com.okguess.user.api.service.okservice.data.Order;
import com.okguess.user.api.service.okservice.data.Project;
import com.okguess.user.api.service.okservice.data.Solution;
import com.okguess.user.api.service.okservice.result.DataResult;
import com.okguess.user.api.service.okservice.result.ListResult;
import com.okguess.user.api.service.okservice.result.PageData;
import com.okguess.user.api.utils.Constants;
import com.okguess.user.api.dto.CreateProjectDto;
import com.okguess.user.api.service.OKGuessService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author hunter.pang
 * @Date 2019/8/8 下午2:04
 */
@Controller
@RequestMapping(value = "/api")
public class BankerController extends AbstractBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankerController.class);

    @Autowired
    private OKGuessService okGuessService;

    @RequestMapping(value = "/project/calculate_odds", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    @ResponseBody
    @TokenCheck
    public Map<String, Object> calculateOdds(@RequestBody String calculatePara, HttpServletRequest request) throws Exception {

        LOGGER.info("calculateOdds start");

        if (StringUtils.isEmpty(calculatePara)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String result = OKGuessBankerApi.calculateOdds(calculatePara);
        return JSON.parseObject(result, Map.class);
    }

    @RequestMapping(value = "/project/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    @TokenCheck
    @ResponseBody
    public Map<String, Object> createProject(@RequestBody CreateProjectSolutionDto createProjectSolutionDto, HttpServletRequest request) throws Exception {

        LOGGER.info("createProject start");

        checkCreateProjectPara(createProjectSolutionDto);

        String userId = (String) request.getAttribute("userId");

        CreateProjectDto createProjectDto = createProjectSolutionDto.getProject();
        createProjectDto.setPcreator(userId);

        List<CreateSolutionDto> createSolutionDtos = createProjectSolutionDto.getSolutions();

        Map<String, String> assetsMap = BastionPayApi.getAssets(createProjectDto.getPasset());
        createProjectDto.setPdecimal(MapUtils.getInteger(assetsMap, "precision"));

        String payForm = okGuessService.createProject(createProjectDto, createSolutionDtos);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("form", payForm);

        return result;
    }

    @RequestMapping(value = "/project/my_created/list", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> myCreatedProject(HttpServletRequest request) throws Exception {

        LOGGER.info("myCreatedProject start");

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
        int pageSize = request.getParameter("page_size") == null ? 10 : Integer.valueOf(request.getParameter("page_size"));
        String roomId = request.getParameter("proom");
        Integer status = request.getParameter("status") == null ? 0 : Integer.valueOf(request.getParameter("status"));

        String userId = (String) request.getAttribute("userId");

        PageData<Project> pageData = okGuessService.getBankerCreatedList(userId, roomId, page, pageSize, status);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("has_next", pageData.getHas_next());
        result.put("page", pageData.getPage());
        result.put("page_size", pageData.getSize());
        result.put("total", pageData.getTotal_result());
        result.put("lists", filterBankerProjectAttr(pageData.getList()));

        return result;
    }

    @RequestMapping(value = "/project/my_created/detail", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> myCreatedDetail(HttpServletRequest request) throws Exception {

        LOGGER.info("projectDetail start");

        String pissue = request.getParameter("pissue");
        if (StringUtils.isEmpty(pissue)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        Map<String, Object> result = new HashMap<String, Object>();

        String userId = (String) request.getAttribute("userId");

        DataResult<Project> dataResult = okGuessService.getBankerProjectDetail(Integer.valueOf(pissue));
        if (dataResult.getResult() != null) {

            if (!userId.equals(dataResult.getResult().getPcreator())) {
                throw new LogicException(Constants.PARA_ERR);
            }

            ListResult<Solution> listResult = okGuessService.getBankerProjectSolution(Integer.valueOf(pissue));

            Project project = dataResult.getResult();
            result.put("project", filterBankerProjectAttr(project));

            List<Solution> solutions = listResult.getResult().get("list");

            result.put("solutions", solutions);
        }

        return result;
    }


    @RequestMapping(value = "/clear_my_project", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> clearMyProject(HttpServletRequest request) throws Exception {

        LOGGER.info("projectDetail start");

        String pissue = request.getParameter("pissue");
        if (StringUtils.isEmpty(pissue)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String solutionId = request.getParameter("solutionid");
        if (StringUtils.isEmpty(solutionId)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String userId = (String) request.getAttribute("userId");

        Map<String, Object> result = new HashMap<String, Object>();

        DataResult<Project> dataResult = okGuessService.getBankerProjectDetail(Integer.valueOf(pissue));
        if (dataResult.getResult() != null) {
            if (!userId.equals(dataResult.getResult().getPcreator())) {
                throw new LogicException(Constants.PARA_ERR);
            }

            long boc = dataResult.getResult().getPbocid();
            okGuessService.clearProject(Integer.valueOf(pissue), boc, Integer.valueOf(solutionId));
        }

        return result;
    }

    @RequestMapping(value = "/refund_my_project", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> refundMyProject(HttpServletRequest request) throws Exception {

        LOGGER.info("refundMyProject start");

        String pissue = request.getParameter("pissue");
        if (StringUtils.isEmpty(pissue)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String userId = (String) request.getAttribute("userId");

        Map<String, Object> result = new HashMap<String, Object>();

        DataResult<Project> dataResult = okGuessService.getBankerProjectDetail(Integer.valueOf(pissue));
        if (dataResult.getResult() != null) {
            if (!userId.equals(dataResult.getResult().getPcreator())) {
                throw new LogicException(Constants.PARA_ERR);
            }

            long boc = dataResult.getResult().getPbocid();
            okGuessService.refundProject(Integer.valueOf(pissue), boc);
        }

        return result;
    }


    //---------------------订单相关-------------------------


    @RequestMapping(value = "/project/my_created/orders", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> myCreatedOrders(HttpServletRequest request) throws Exception {

        LOGGER.info("projectDetail start");

        String pissue = request.getParameter("pissue");
        if (StringUtils.isEmpty(pissue)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
        int pageSize = request.getParameter("page_size") == null ? 10 : Integer.valueOf(request.getParameter("page_size"));
        String ostatus = request.getParameter("ostatus") == null ? null : request.getParameter("ostatus");

        String userId = (String) request.getAttribute("userId");

        Map<String, Object> result = new HashMap<String, Object>();

        DataResult<Project> dataResult = okGuessService.getBankerProjectDetail(Integer.valueOf(pissue));
        if (dataResult.getResult() != null) {
            if (!userId.equals(dataResult.getResult().getPcreator())) {
                throw new LogicException(Constants.PARA_ERR);
            }

            PageData<Order> pageData = okGuessService.getBankerProjectOrder(Integer.valueOf(pissue), page, pageSize, ostatus);

            result.put("has_next", pageData.getHas_next());
            result.put("page", pageData.getPage());
            result.put("page_size", pageData.getSize());
            result.put("total", pageData.getTotal_result());
            result.put("lists", filterBankerOrderAttr(pageData.getList()));

            result.put("project", filterPlayerProjectAttr(dataResult.getResult()));
        }

        return result;
    }


    private void checkCreateProjectPara(CreateProjectSolutionDto createProjectSolutionDto) {

        CreateProjectDto createProjectDto = createProjectSolutionDto.getProject();
        List<CreateSolutionDto> createSolutionDtoList = createProjectSolutionDto.getSolutions();
        if (createProjectDto == null || CollectionUtils.isEmpty(createSolutionDtoList) || createSolutionDtoList.size() < 2) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String passets = createProjectDto.getPasset();
        if (StringUtils.isEmpty(passets)) {
            throw new LogicException(Constants.PARA_ERR);
        }
        Map<String, String> assetsMap = BastionPayApi.getAssets(passets);
        if (assetsMap == null || assetsMap.isEmpty()) {
            throw new LogicException(Constants.PARA_ERR);
        }


        try {
            Integer plevel = createProjectDto.getPlevel();
            Assert.isTrue(plevel == 0 || plevel == 1 || plevel == 2);
        } catch (Exception e) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String pdescription = createProjectDto.getPdescription();
        if (StringUtils.isEmpty(pdescription) || pdescription.length() > 1000) {
            throw new LogicException(Constants.PARA_ERR);
        }

        try {
            BigDecimal pmarginBigDecaimal = new BigDecimal(createProjectDto.getPmargin());
            if (pmarginBigDecaimal.compareTo(BigDecimal.ZERO) <= 0) {
                throw new LogicException(Constants.PARA_ERR);
            }
        } catch (Exception e) {
            throw new LogicException(Constants.PARA_ERR);
        }

        try {
            if (new BigDecimal(createProjectDto.getPmaxusercount()).compareTo(BigDecimal.ZERO) < 0) {
                throw new LogicException(Constants.PARA_ERR);
            }

        } catch (Exception e) {
            throw new LogicException(Constants.PARA_ERR);
        }

        String pname = createProjectDto.getPname();
        if (StringUtils.isEmpty(pname) || pname.length() >= 255) {
            throw new LogicException(Constants.PARA_ERR);
        }

        for (CreateSolutionDto createSolutionDto : createSolutionDtoList) {
            if (new BigDecimal(createSolutionDto.getSodds()).compareTo(BigDecimal.ZERO) < 0) {
                throw new LogicException(Constants.PARA_ERR);
            }
            if (StringUtils.isEmpty(createSolutionDto.getSname())) {
                throw new LogicException(Constants.PARA_ERR);
            }
            if (createSolutionDto.getSolutionid() == null) {
                throw new LogicException(Constants.PARA_ERR);
            }
        }
    }


}
