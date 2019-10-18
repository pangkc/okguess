package com.okguess.user.api.controller;

import com.okguess.user.api.entity.UserInfo;
import com.okguess.user.api.service.OKGuessService;
import com.okguess.user.api.service.bastionpay.BastionPayApi;
import com.okguess.user.api.service.okservice.data.Order;
import com.okguess.user.api.service.okservice.data.Project;
import com.okguess.user.api.service.okservice.data.Solution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hunter.pang
 * @Date 2019/9/28 上午11:16
 */
public class AbstractBaseController {

    @Autowired
    private OKGuessService okGuessService;


    protected List<Map<String, Object>> filterPlayerProjectAttr(List<Project> projects) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        if (projects != null) {
            for (Project pro : projects) {
                result.add(filterPlayerProjectAttr(pro));
            }
        }

        return result;
    }

    protected Map<String, Object> filterPlayerProjectAttr(Project project) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pallcount", project.getPallcount());
        result.put("passet", project.getPasset());
        result.put("asset_logo", BastionPayApi.getAssets(project.getPasset()) == null ? "" : BastionPayApi.getAssets(project.getPasset()).get("logo"));
        result.put("pbegintime", project.getPbegintime());
        result.put("pbetcount", project.getPbetcount());
        result.put("pbocid", project.getPbocid());
        result.put("pcreatetime", project.getPcreatetime());
        result.put("pcreator", project.getPcreator());

        UserInfo creator = okGuessService.findUerByBpUesrId(Long.valueOf(project.getPcreator()));
        if (creator != null) {
            result.put("pcreatornick", creator.getBpNick());
            result.put("pcreatorphoto", creator.getBpPhoto());
        }

        result.put("pdecimal", project.getPdecimal());
        result.put("pendtime", project.getPendtime());
        result.put("pissue", project.getPissue());
        result.put("plevel", project.getPlevel());
        result.put("pmaxusercount", project.getPmaxusercount());
        result.put("pname", project.getPname());
        result.put("proom", project.getProom());
        result.put("pstatus", project.getPstatus());
        result.put("pdescription", project.getPdescription());
        result.put("pwincount", project.getPwincount());
        result.put("pwinsid", project.getPwinsid());
        result.put("pwinsname", project.getPwinsname());
        result.put("ouserallcount", project.getOuserallcount());
        result.put("ouserprofit", project.getOuserprofit());
        result.put("currencyouserprofit", project.getCurrencyouserprofit());
        result.put("pliquidatetime", project.getPliquidatetime());
        result.put("psettlestatus", project.getPsettlestatus());

        return result;
    }

    protected List<Map<String, Object>> filterBankerProjectAttr(List<Project> projects) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        if (projects != null) {
            for (Project pro : projects) {
                result.add(filterBankerProjectAttr(pro));
            }
        }

        return result;
    }

    protected Map<String, Object> filterBankerProjectAttr(Project project) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pallcount", project.getPallcount());
        result.put("passet", project.getPasset());
        result.put("asset_logo", BastionPayApi.getAssets(project.getPasset()) == null ? "" : BastionPayApi.getAssets(project.getPasset()).get("logo"));
        result.put("pbegintime", project.getPbegintime());
        result.put("pbetcount", project.getPbetcount());
        result.put("pbocid", project.getPbocid());
        result.put("pcreatetime", project.getPcreatetime());
        result.put("pcreator", project.getPcreator());
        result.put("pdecimal", project.getPdecimal());
        result.put("pendtime", project.getPendtime());
        result.put("pissue", project.getPissue());
        result.put("plevel", project.getPlevel());
        result.put("pmaxusercount", project.getPmaxusercount());
        result.put("pname", project.getPname());
        result.put("proom", project.getProom());
        result.put("pstatus", project.getPstatus());
        result.put("pdescription", project.getPdescription());
        result.put("pmargin", project.getPmargin());
        result.put("pwincount", project.getPwincount());
        result.put("pwinsid", project.getPwinsid());
        result.put("pwinsname", project.getPwinsname());
        result.put("pprofit", project.getPprofit());
        result.put("currencypprofie", project.getCurrencypprofit());
        result.put("ouserallcount", project.getOuserallcount());
        result.put("ouserprofit", project.getOuserprofit());
        result.put("currencyouserprofit", project.getCurrencyouserprofit());
        result.put("pliquidatetime", project.getPliquidatetime());
        result.put("psettlestatus", project.getPsettlestatus());
        result.put("pmarginleft", project.getPmarginleft());
        result.put("pplatformprofit", project.getPplatformprofit());

        UserInfo creator = okGuessService.findUerByBpUesrId(Long.valueOf(project.getPcreator()));
        if (creator != null) {
            result.put("pcreatornick", creator.getBpNick());
            result.put("pcreatorphoto", creator.getBpPhoto());
        }

        return result;
    }


    protected List<Map<String, Object>> filterPlayerSolutionAttr(List<Solution> solutions) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        if (solutions != null) {
            for (Solution solution : solutions) {

                Map<String, Object> solMap = new HashMap<String, Object>();
                solMap.put("sbetcount", solution.getSbetcount());
                solMap.put("scurrentodds", solution.getScurrentodds());
                solMap.put("sname", solution.getSname());
                solMap.put("solutionid", solution.getSolutionid());
                solMap.put("spdecimal", solution.getSpdecimal());
                solMap.put("spissue", solution.getSpissue());
                solMap.put("sstatus", solution.getSstatus());

                result.add(solMap);
            }
        }

        return result;
    }

    protected List<Map<String, Object>> filterBankerOrderAttr(List<Order> orders) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();


        if (orders != null) {
            for (Order order : orders) {

                Map<String, Object> orderMap = new HashMap<String, Object>();

                orderMap.put("ocreatetime", order.getOcreatetime());
                orderMap.put("oodds", order.getOodds());
                orderMap.put("opasset", order.getOpasset());
                orderMap.put("asset_logo", BastionPayApi.getAssets(order.getOpasset()) == null ? "" : BastionPayApi.getAssets(order.getOpasset()).get("logo"));
                orderMap.put("opdecimal", order.getOpdecimal());
                orderMap.put("opissue", order.getOpissue());
                orderMap.put("oproom", order.getOproom());
                orderMap.put("oprize", order.getOprize());
                orderMap.put("osolutionid", order.getOsolutionid());
                orderMap.put("ostatus", order.getOstatus());
                orderMap.put("ousercount", order.getOusercount());
                orderMap.put("ouserid", order.getOuserid());
                orderMap.put("ouserorderno", order.getOuserorderno());
                orderMap.put("currencyprize", order.getCurrencyoprize());
                orderMap.put("osettlestatus", order.getOsettlestatus());

                UserInfo creator = okGuessService.findUerByBpUesrId(Long.valueOf(order.getOuserid()));
                if (creator != null) {
                    orderMap.put("ousernick", creator.getBpNick());
                    orderMap.put("ouserphoto", creator.getBpPhoto());
                }

                result.add(orderMap);
            }
        }

        return result;
    }

    protected List<Map<String, Object>> filterPlayerOrderAttr(List<Order> orders) {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        if (orders != null) {
            for (Order order : orders) {
                result.add(filterPlayerOrderAttr(order));
            }
        }

        return result;
    }


    protected Map<String, Object> filterPlayerOrderAttr(Order order) {

        Map<String, Object> orderMap = new HashMap<String, Object>();

        orderMap.put("ocreatetime", order.getOcreatetime());
        orderMap.put("oodds", order.getOodds());
        orderMap.put("opasset", order.getOpasset());
        orderMap.put("asset_logo", BastionPayApi.getAssets(order.getOpasset()) == null ? "" : BastionPayApi.getAssets(order.getOpasset()).get("logo"));
        orderMap.put("opdecimal", order.getOpdecimal());
        orderMap.put("opissue", order.getOpissue());
        orderMap.put("oproom", order.getOproom());
        orderMap.put("oprize", order.getOprize());
        orderMap.put("osolutionid", order.getOsolutionid());
        orderMap.put("ostatus", order.getOstatus());
        orderMap.put("ousercount", order.getOusercount());
        orderMap.put("ouserid", order.getOuserid());
        orderMap.put("ouserorderno", order.getOuserorderno());
        orderMap.put("opname", order.getOpname());
        orderMap.put("osname", order.getOsname());
        orderMap.put("currencyprize", order.getCurrencyoprize());
        orderMap.put("osettlestatus", order.getOsettlestatus());

        UserInfo creator = okGuessService.findUerByBpUesrId(Long.valueOf(order.getOuserid()));
        if (creator != null) {
            orderMap.put("ousernick", creator.getBpNick());
            orderMap.put("ouserphoto", creator.getBpPhoto());
        }

        return orderMap;
    }


}
