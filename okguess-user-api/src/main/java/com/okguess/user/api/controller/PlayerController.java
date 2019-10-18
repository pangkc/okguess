package com.okguess.user.api.controller;

import com.okguess.user.api.aop.TokenCheck;
import com.okguess.user.api.dto.CreateOrderDto;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.OKGuessService;
import com.okguess.user.api.service.okservice.data.Order;
import com.okguess.user.api.service.okservice.data.Project;
import com.okguess.user.api.service.okservice.data.Solution;
import com.okguess.user.api.service.okservice.result.DataResult;
import com.okguess.user.api.service.okservice.result.ListResult;
import com.okguess.user.api.service.okservice.result.PageData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hunter.pang
 * @Date 2019/8/8 下午2:04
 */
@Controller
@RequestMapping(value = "/api")
public class PlayerController extends AbstractBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private OKGuessService okGuessService;

    @RequestMapping(value = "/project/processing/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> userProcessingList(HttpServletRequest request) throws Exception {

        LOGGER.info("userProcessingList start");

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
        int pageSize = request.getParameter("page_size") == null ? 10 : Integer.valueOf(request.getParameter("page_size"));
        String roomId = request.getParameter("proom");

        PageData<Project> pageData = okGuessService.getPlayerInProcessingList(roomId, page, pageSize);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("has_next", pageData.getHas_next());
        result.put("page", pageData.getPage());
        result.put("page_size", pageData.getSize());
        result.put("total", pageData.getTotal_result());
        result.put("lists", filterPlayerProjectAttr(pageData.getList()));

        return result;
    }

    @RequestMapping(value = "/project/processing/detail", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> projectDetail(HttpServletRequest request) throws Exception {

        LOGGER.info("projectDetail start");

        String pissue = request.getParameter("pissue");
        if (StringUtils.isEmpty(pissue)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        DataResult<Project> dataResult = okGuessService.getPlayerProjectDetail(Integer.valueOf(pissue));

        ListResult<Solution> listResult = okGuessService.getPlayerProjectSolution(Integer.valueOf(pissue));

        Map<String, Object> result = new HashMap<String, Object>();

        Project project = dataResult.getResult();
        result.put("project", filterPlayerProjectAttr(project));

        List<Solution> solutions = listResult.getResult().get("list");
        result.put("solutions", filterPlayerSolutionAttr(solutions));

        return result;
    }


    @RequestMapping(value = "/order/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    @TokenCheck
    @ResponseBody
    public Map<String, Object> createOrder(@RequestBody CreateOrderDto createOrderDto, HttpServletRequest request) throws Exception {

        LOGGER.info("createOrder start");

        checkCreateOrderPara(createOrderDto, request);

        String userId = (String) request.getAttribute(Constants.USER_ID);
        String nick = (String) request.getAttribute(Constants.USER_NICK);

        createOrderDto.setOuserid(userId);
        createOrderDto.setOusername(nick);

        String payForm = okGuessService.createOrder(createOrderDto);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("form", payForm);

        return result;
    }


    @RequestMapping(value = "/project/my_participate/list", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> myParticipateProject(HttpServletRequest request) throws Exception {

        LOGGER.info("myParticipateProject start");

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
        int pageSize = request.getParameter("page_size") == null ? 10 : Integer.valueOf(request.getParameter("page_size"));
        String roomId = request.getParameter("proom");
        Integer status = request.getParameter("status") == null ? 0 : Integer.valueOf(request.getParameter("status"));

        String userId = (String) request.getAttribute(Constants.USER_ID);

        PageData<Project> pageData = okGuessService.getPlayerParticipateList(userId, roomId, page, pageSize, status);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("has_next", pageData.getHas_next());
        result.put("page", pageData.getPage());
        result.put("page_size", pageData.getSize());
        result.put("total", pageData.getTotal_result());
        result.put("lists", filterPlayerProjectAttr(pageData.getList()));

        return result;
    }


    @RequestMapping(value = "/project/my_participate/orders", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> userProjectOrders(HttpServletRequest request) throws Exception {

        LOGGER.info("myParticipateProject start");

        String pissue = request.getParameter("pissue");
        if (StringUtils.isEmpty(pissue)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
        int pageSize = request.getParameter("page_size") == null ? 10 : Integer.valueOf(request.getParameter("page_size"));
        String roomId = request.getParameter("proom");

        String userId = (String) request.getAttribute(Constants.USER_ID);

        PageData<Order> pageData = okGuessService.getPlayerProjectOrder(userId, roomId, Integer.valueOf(pissue), page, pageSize);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("has_next", pageData.getHas_next());
        result.put("page", pageData.getPage());
        result.put("page_size", pageData.getSize());
        result.put("total", pageData.getTotal_result());
        result.put("lists", filterPlayerOrderAttr(pageData.getList()));

        DataResult<Project> dataResult = okGuessService.getBankerProjectDetail(Integer.valueOf(pissue));
        result.put("project", filterPlayerProjectAttr(dataResult.getResult()));

        return result;
    }


    @RequestMapping(value = "/project/my_participate/order/detail", method = RequestMethod.GET)
    @TokenCheck
    @ResponseBody
    public Map<String, Object> myOrderDetail(HttpServletRequest request) throws Exception {

        LOGGER.info("myOrderDetail start");

        String userId = (String) request.getAttribute(Constants.USER_ID);

        String orderno = request.getParameter("orderno");
        if (StringUtils.isEmpty(orderno)) {
            throw new LogicException(Constants.PARA_ERR);
        }

        Map<String, Object> result = new HashMap<String, Object>();

        Order order = okGuessService.getPlayerOrderDetail(orderno);
        if (order != null) {
            result.put("order", filterPlayerOrderAttr(order));

            String opisuue = order.getOpissue();

            Project project = okGuessService.getPlayerProjectDetail(Integer.valueOf(opisuue)).getResult();

            result.put("project", filterPlayerProjectAttr(project));

            List<Solution> solutions = okGuessService.getPlayerProjectSolution(Integer.valueOf(opisuue)).getResult().get("list");

            result.put("solutions", filterPlayerSolutionAttr(solutions));
        }
        return result;
    }


    private void checkCreateOrderPara(CreateOrderDto createOrderDto, HttpServletRequest request) {
        if (createOrderDto.getOpissue() == null) {
            throw new LogicException(Constants.PARA_ERR);
        }
        if (createOrderDto.getOsolutionid() == null) {
            throw new LogicException(Constants.PARA_ERR);
        }
        if (createOrderDto.getOusercount() == null) {
            throw new LogicException(Constants.PARA_ERR);
        }
        if (createOrderDto.getOuserorderno() == null) {
            throw new LogicException(Constants.PARA_ERR);
        }
        if (request.getParameter("boc_id") == null) {
            throw new LogicException(Constants.PARA_ERR);
        }
    }

}
