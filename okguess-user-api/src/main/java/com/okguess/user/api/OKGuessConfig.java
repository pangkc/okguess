package com.okguess.user.api;

import com.okguess.user.api.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author hunter.pang
 * @Date 2018/10/7 上午10:30
 */

public class OKGuessConfig {

    public static String OKGUESS_SERVER_URL;

    public static String OKGUESS_API_URL;

    public static String BASTION_PAY_OPEN_API;

    public static String BASTION_PAY_MERCHANT_ID;

    public static String BASTION_PAY_PAYEE_ID;


    private OKGuessConfig() {

    }

    public static String BASTION_OPEN_API_PUB_KEY;

    public static String OKGUESS_TO_BASTION_OPEN_API_PUB_KEY;

    public static String OKGUESS_TO_BASTION_OPEN_API_PRI_KEY;

    public static String COIN_URL;

    public static String BASTION_PAY_OAUTH_URL;

    static {


        try {

            Properties properties = null;
            try {
                properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("okguess.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            OKGUESS_SERVER_URL = (String) properties.get("okguesss.server.url");

            BASTION_PAY_OPEN_API = (String) properties.get("bastionpay.openapi.url");

            BASTION_PAY_MERCHANT_ID = (String) properties.get("bastionpay.openapi.merchantid");

            BASTION_PAY_PAYEE_ID = (String) properties.get("bastionpay.openapi.payeeid");

            OKGUESS_API_URL = (String) properties.get("okguesss.api.url");

            BASTION_PAY_OAUTH_URL  = (String) properties.get("bastion.pay.oauth.url");

            COIN_URL = (String) properties.get("bastion.coin.base.url");
            COIN_URL += "/api/v1/coin/quote";

            BASTION_OPEN_API_PUB_KEY = FileUtil.getFileContent(new ClassPathResource("bastion-open-api-public-key.properties").getInputStream(), "UTF-8");
            BASTION_OPEN_API_PUB_KEY = BASTION_OPEN_API_PUB_KEY.replaceAll("\\n", "");

            OKGUESS_TO_BASTION_OPEN_API_PRI_KEY = FileUtil.getFileContent(new ClassPathResource("okguess-to-bastion-open-api-private-key.properties")
                    .getInputStream(), "UTF-8");
            OKGUESS_TO_BASTION_OPEN_API_PRI_KEY = OKGUESS_TO_BASTION_OPEN_API_PRI_KEY.replaceAll("\\n", "");

            OKGUESS_TO_BASTION_OPEN_API_PUB_KEY = FileUtil.getFileContent(new ClassPathResource("okguess-to-bastion-open-api-public-key.properties")
                    .getInputStream(), "UTF-8");
            OKGUESS_TO_BASTION_OPEN_API_PUB_KEY = OKGUESS_TO_BASTION_OPEN_API_PUB_KEY.replaceAll("\\n", "");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
