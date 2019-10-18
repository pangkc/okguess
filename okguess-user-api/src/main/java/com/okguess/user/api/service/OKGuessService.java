package com.okguess.user.api.service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.okguess.user.api.dao.PayOrderDao;
import com.okguess.user.api.dao.UserInfoDao;
import com.okguess.user.api.dto.CreateOrderDto;
import com.okguess.user.api.dto.CreateProjectDto;
import com.okguess.user.api.dto.CreateSolutionDto;
import com.okguess.user.api.entity.PayOrder;
import com.okguess.user.api.entity.UserInfo;
import com.okguess.user.api.exception.LogicException;
import com.okguess.user.api.service.bastionpay.BastionPayApi;
import com.okguess.user.api.service.okservice.OKGuessBankerApi;
import com.okguess.user.api.service.okservice.OKGuessPlayerApi;
import com.okguess.user.api.service.okservice.data.Order;
import com.okguess.user.api.service.okservice.data.Project;
import com.okguess.user.api.service.okservice.data.Solution;
import com.okguess.user.api.service.okservice.result.DataResult;
import com.okguess.user.api.service.okservice.result.ListResult;
import com.okguess.user.api.service.okservice.result.PageData;
import com.okguess.user.api.service.okservice.result.PageResult;
import com.okguess.user.api.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author hunter.pang
 * @Date 2019/8/23 下午4:11
 */
@Service
public class OKGuessService {


    @Autowired
    private PayOrderDao payOrderDao;

    @Autowired
    private UserInfoDao userInfoDao;

    private List<String> solutinoNames = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L");

    public long addUserInfo(UserInfo userInfo) {

        return userInfoDao.insert(userInfo);
    }

    public UserInfo findUerByBpUesrId(long bpUserId) {

        Cache cache = DataCache.getUserCache();

        Object userCacheObj = cache.getIfPresent(bpUserId);
        if (userCacheObj != null) {
            return (UserInfo) userCacheObj;
        }

        UserInfo userDb = userInfoDao.findByBpUesrId(bpUserId);
        if (userDb != null) {
            cache.put(bpUserId, userDb);
        }
        return userDb;
    }

    public UserInfo saveByBpUserIdIfRequired(UserInfo userInfo) {

        UserInfo UserInfoDb = userInfoDao.findByBpUesrId(userInfo.getBpUserId());
        if (UserInfoDb != null) {
            return UserInfoDb;
        }
        long userId = addUserInfo(userInfo);
        userInfo.setId(userId);

        return userInfo;
    }


    public String createProject(CreateProjectDto createProjectDto, List<CreateSolutionDto> createSolutionDtoList) {

        String orderNo = UUID.randomUUID().toString();
        createProjectDto.setPuserproductno(orderNo);

        Project project = OKGuessBankerApi.preCreateProject(createProjectDto, createSolutionDtoList);

        PayOrder payOrder = new PayOrder();
        payOrder.setContent(JSON.toJSONString(createProjectDto));
        payOrder.setCreateTime(new Date());
        payOrder.setStatus(Constants.PAY_ORDER_STATUS_INIT);
        payOrder.setOrderNo(orderNo);
        payOrder.setOrderType(1);

        payOrderDao.insert(payOrder);

        String remark = "第" + project.getPissue() + "期：项目押金";
        return BastionPayApi.createWapOrder(orderNo,
                createProjectDto.getPasset(),
                createProjectDto.getPmargin(),
                remark,
                remark,
                createProjectDto.getReturn_url(),
                remark);
    }

    public boolean activePayOrder(String orderNo, String assets, String amount) {


        PayOrder payOrder = payOrderDao.findByOrderNo(orderNo);
        if (payOrder.getOrderType() == 1) {
            CreateProjectDto project = JSON.parseObject(payOrder.getContent(), CreateProjectDto.class);
            if (project.getPasset().equals(assets) && new BigDecimal(project.getPmargin()).compareTo(new BigDecimal(amount)) == 0) {
                try {
                    OKGuessBankerApi.activeProject(orderNo);
                } catch (LogicException e) {
                    String code = e.getCode();
                    if (code != null && code.startsWith(Constants.OKGUESS_SERVER_ERR)) {
                        String subCode = e.getSubCode();
                        if (Constants.ACTIVE_PROJECT_FAILED.equals(subCode) || Constants.ACTIVE_ORDER_FAILED.equals(subCode)) {
                            String remark = "项目押金返还";
                            BastionPayApi.refund(orderNo, remark);
                        }
                    }
                    throw e;
                }
            } else {
                throw new LogicException(Constants.PARA_ERR);
            }
        } else {

            CreateOrderDto order = JSON.parseObject(payOrder.getContent(), CreateOrderDto.class);
            Long opissue = order.getOpissue();
            Project project = OKGuessBankerApi.getProjectDetail(opissue.intValue()).getResult();
            if (project.getPasset().equals(assets) && new BigDecimal(order.getOusercount()).compareTo(new BigDecimal(amount)) == 0) {
                try {
                    OKGuessPlayerApi.activeOrder(orderNo, project.getPbocid().intValue());
                } catch (LogicException e) {
                    String code = e.getCode();
                    if (code != null && code.startsWith(Constants.OKGUESS_SERVER_ERR)) {
                        String subCode = e.getSubCode();
                        if (Constants.ACTIVE_PROJECT_FAILED.equals(subCode) || Constants.ACTIVE_ORDER_FAILED.equals(subCode)) {
                            String remark = "第" + project.getPissue() + "期：投注失败，资金返还";
                            BastionPayApi.refund(orderNo, remark);
                        }
                    }
                    throw e;
                }
            } else {
                throw new LogicException(Constants.PARA_ERR);
            }
        }
        return true;

    }

    public PageData<Project> getPlayerInProcessingList(String roomId, int page, int pageSize) {

        PageResult<Project> projects = OKGuessPlayerApi.getInProcessingProjectList(roomId, page, pageSize);

        return projects.getResult();
    }


    public DataResult<Project> getPlayerProjectDetail(int pissue) {

        return OKGuessPlayerApi.getProjectDetail(pissue);
    }


    public ListResult<Solution> getPlayerProjectSolution(int pissue) {

        ListResult<Solution> solutionListResult = OKGuessPlayerApi.getProjectSolution(pissue);
        List<Solution> solutions = solutionListResult.getResult().get("list");

        DataResult<Project> projectDataResult = getPlayerProjectDetail(pissue);
        processCurrencyMoney(solutions, projectDataResult.getResult().getPasset());

        return solutionListResult;
    }

    public String createOrder(CreateOrderDto createOrderDto) {

        Long opissue = createOrderDto.getOpissue();
        Project project = OKGuessPlayerApi.getProjectDetail(opissue.intValue()).getResult();

        OKGuessPlayerApi.preCreateOrder(createOrderDto, project.getPbocid().intValue());

        PayOrder payOrder = new PayOrder();
        payOrder.setContent(JSON.toJSONString(createOrderDto));
        payOrder.setCreateTime(new Date());
        payOrder.setStatus(Constants.PAY_ORDER_STATUS_INIT);
        payOrder.setOrderNo(createOrderDto.getOuserorderno());
        payOrder.setOrderType(2);
        payOrderDao.insert(payOrder);

        String orderNo = createOrderDto.getOuserorderno();
        String remark = "第" + opissue + "期：投选项 " + solutinoNames.get(createOrderDto.getOsolutionid());
        return BastionPayApi.createWapOrder(orderNo,
                project.getPasset(),
                createOrderDto.getOusercount(),
                remark,
                remark,
                createOrderDto.getReturn_url(),
                remark);
    }

    public PageData<Project> getPlayerParticipateList(String userId, String roomId, int page, int pageSize, int status) {

        PageResult<Project> projects = OKGuessPlayerApi.getPlayerParticipateList(userId, roomId, page, pageSize, status);

        return projects.getResult();
    }

    public PageData<Order> getPlayerProjectOrder(String userId, String roomId, int pissue, int page, int pageSize) {

        PageResult<Order> projects = OKGuessPlayerApi.getPlayerParticipateProjectOrder(userId, roomId, pissue, page, pageSize);

        processCurrencyMoney(projects.getResult().getList());

        return projects.getResult();
    }


    public Order getPlayerOrderDetail(String ouserorderno) {

        DataResult<Order> orders = OKGuessPlayerApi.getOrderDetail(ouserorderno);

        Order order = orders.getResult();

        processCurrencyMoney(order);

        return order;
    }


    ////////////----------------------------------

    public PageData<Project> getBankerCreatedList(String userId, String roomId, int page, int pageSize, int status) {

        PageResult<Project> projects = OKGuessBankerApi.getCreatorProjectList(userId, roomId, page, pageSize, status);

        processProjectCurrencyMoney(projects.getResult().getList());

        return projects.getResult();
    }

    public DataResult<Project> getBankerProjectDetail(int pissue) {

        DataResult<Project> result = OKGuessBankerApi.getProjectDetail(pissue);

        processProjectCurrencyMoney(result.getResult());

        return result;
    }

    public ListResult<Solution> getBankerProjectSolution(int pissue) {

        ListResult<Solution> solutionListResult = OKGuessBankerApi.getProjectSolution(pissue);
        List<Solution> solutions = solutionListResult.getResult().get("list");

        DataResult<Project> projectDataResult = this.getBankerProjectDetail(pissue);
        processCurrencyMoney(solutions, projectDataResult.getResult().getPasset());

        return solutionListResult;
    }

    public PageData<Order> getBankerProjectOrder(int pissue, int page, int pageSize, String ostatus) {

        PageData<Order> pageData = OKGuessBankerApi.getProjectOrder(pissue, page, pageSize, ostatus).getResult();
        processCurrencyMoney(pageData.getList());
        return pageData;
    }


    public void clearProject(int pissue, long bocId, int solutionId) {

        OKGuessBankerApi.clearProject(pissue, bocId, solutionId);
    }

    public void refundProject(int pissue, long bocId) {

        OKGuessBankerApi.refund(pissue, bocId);
    }

    private void processProjectCurrencyMoney(List<Project> projects) {
        if (projects != null) {
            for (Project project : projects) {
                processProjectCurrencyMoney(project);
            }
        }
    }

    private void processProjectCurrencyMoney(Project project) {
        String pprofit = project.getPprofit();
        String ouserprofit = project.getOuserprofit();
        if (!StringUtils.isEmpty(pprofit) || !StringUtils.isEmpty(ouserprofit)) {
            String fromCoin = project.getPasset();
            String toCoin = "USD";

            List<Map<String, Object>> quotes = CoinApi.getCoinQuote(fromCoin, toCoin);
            if (CollectionUtils.isEmpty(quotes) || quotes.size() < 1) {
                return;
            }
            Map<String, Object> quote = quotes.get(0);
            List<Map<String, Object>> details = (List) quote.get("detail");
            if (CollectionUtils.isEmpty(details) || details.size() < 1) {
                return;
            }
            BigDecimal price = new BigDecimal(details.get(0).get("price").toString());

            if (!StringUtils.isEmpty(pprofit)) {
                BigDecimal currencyAmount = new BigDecimal(pprofit).multiply(price);
                project.setCurrencypprofit(currencyAmount.toPlainString());
            }

            if (!StringUtils.isEmpty(ouserprofit)) {
                BigDecimal currencyAmount = new BigDecimal(ouserprofit).multiply(price);
                project.setCurrencyouserprofit(currencyAmount.toPlainString());
            }
        }
    }

    private void processCurrencyMoney(List<Order> orders) {
        if (orders != null) {
            for (Order order : orders) {
                processCurrencyMoney(order);
            }
        }
    }


    private void processCurrencyMoney(List<Solution> solutions, String assets) {
        if (solutions != null) {
            for (Solution solution : solutions) {
                processCurrencyMoney(solution, assets);
            }
        }
    }

    private void processCurrencyMoney(Solution solution, String assets) {

        String fromCoin = assets;
        String toCoin = "USD";

        List<Map<String, Object>> quotes = CoinApi.getCoinQuote(fromCoin, toCoin);
        if (CollectionUtils.isEmpty(quotes) || quotes.size() < 1) {
            return;
        }
        Map<String, Object> quote = quotes.get(0);
        List<Map<String, Object>> details = (List) quote.get("detail");
        if (CollectionUtils.isEmpty(details) || details.size() < 1) {
            return;
        }
        BigDecimal price = new BigDecimal(details.get(0).get("price").toString());

        String sprofit = solution.getSprofit();
        if (!StringUtils.isEmpty(sprofit)) {
            BigDecimal currencyAmount = new BigDecimal(sprofit).multiply(price);
            solution.setCurrencysprofit(currencyAmount.toPlainString());
        }
    }

    private void processCurrencyMoney(Order order) {
        String oprize = order.getOprize();
        if (!StringUtils.isEmpty(oprize)) {
            String fromCoin = order.getOpasset();
            String toCoin = "USD";

            List<Map<String, Object>> quotes = CoinApi.getCoinQuote(fromCoin, toCoin);
            if (CollectionUtils.isEmpty(quotes) || quotes.size() < 1) {
                return;
            }
            Map<String, Object> quote = quotes.get(0);
            List<Map<String, Object>> details = (List) quote.get("detail");
            if (CollectionUtils.isEmpty(details) || details.size() < 1) {
                return;
            }
            BigDecimal price = new BigDecimal(details.get(0).get("price").toString());
            BigDecimal currencyAmount = new BigDecimal(oprize).multiply(price);
            order.setCurrencyoprize(currencyAmount.toPlainString());
        }
    }

}
