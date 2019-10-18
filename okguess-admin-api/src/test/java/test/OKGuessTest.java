package test;

import com.alibaba.fastjson.JSON;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.*;


public class OKGuessTest extends BaseTest {


    @Test
    public void test_assets() throws Exception {
        String api = "/api/assets";
        String url = BASE_URL + api;


        String result = HttpUtil.get(url);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_open_assets() throws Exception {
        String api = "/api/open_asset?asset=BTC";
        String url = BASE_URL + api;


        String result = HttpUtil.get(url);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_close_assets() throws Exception {
        String api = "/api/close_asset?asset=OKG";
        String url = BASE_URL + api;


        String result = HttpUtil.get(url);
        assert result != null;
        System.out.println("result============" + result);
    }


    @Test
    public void refund() throws Exception {
        String api = "/api/refund?orderno=b2d7368f-1853-48f9-bc79-699380539010";
        String url = BASE_URL + api;


        String result = HttpUtil.get(url);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void transfer() throws Exception {
        String api = "/api/transfer";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();
        businessPara.put("orderno", UUID.randomUUID().toString());
        businessPara.put("asset", "OKG");
        businessPara.put("amount", "1.1");
        businessPara.put("payee", "90");
        businessPara.put("remark", "测试转账");

        String body = JSON.toJSONString(businessPara);
        System.out.println(body);

        String result = HttpUtil.postWithStringEntityParaAndHeader(url, new StringEntity(body, Charset.forName("utf-8")), HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }
}
