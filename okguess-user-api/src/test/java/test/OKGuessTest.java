package test;

import com.alibaba.fastjson.JSON;
import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.dto.CreateSolutionDto;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.*;


public class OKGuessTest extends BaseTest {


    @Test
    public void test_oauth() throws Exception {
        String api = "/api/oauth?auth_code=hwlJR5rzARhYpPzWgbQqYJwP7KZJ5JrKePf2vmmbbFk%3D";
        String url = BASE_URL + api;


        String result = HttpUtil.get(url);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_calculate_odds() throws Exception {
        String api = "/api/project/calculate_odds";


        String url = BASE_URL + api;
        String body = "{\"product\":{\"plevel\":0},\"solutions\":[{\"solutionid\":1,\"spossible\":\"0.4\"},{\"solutionid\":2,\"spossible\":\"0.6\"}]}";

        String result = HttpUtil.postWithStringEntityParaAndHeader(url, new StringEntity(body, Charset.forName("utf-8")), HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_create_project() throws Exception {
        String api = "/api/project/create";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();

        Map<String, Object> project = new TreeMap<String, Object>();
        project.put("plevel", 1);
        project.put("passet", "OKG");
        project.put("pbegintime", System.currentTimeMillis() / 1000 + 10);
        project.put("pendtime", (System.currentTimeMillis() / 1000 + 3000));
        project.put("pdescription", "测试项目111描述");
        project.put("pmargin", "100.11");
        project.put("pq", "0.11");
        project.put("pmaxusercount", 10);
        project.put("pname", "项目名称");
        project.put("proom", 11);
        project.put("return_url", "dfdf");

        businessPara.put("project", project);

        List<CreateSolutionDto> createSolutionDtoList = new ArrayList<CreateSolutionDto>();

        CreateSolutionDto createSolutionDto = new CreateSolutionDto();
        createSolutionDto.setSolutionid(1);
        createSolutionDto.setSname("方案一");
        createSolutionDto.setSodds("1.2");
        createSolutionDtoList.add(createSolutionDto);

        CreateSolutionDto createSolutionDto2 = new CreateSolutionDto();
        createSolutionDto2.setSolutionid(2);
        createSolutionDto2.setSname("方案二");
        createSolutionDto2.setSodds("0.9");
        createSolutionDtoList.add(createSolutionDto2);

        CreateSolutionDto createSolutionDto3 = new CreateSolutionDto();
        createSolutionDto3.setSolutionid(3);
        createSolutionDto3.setSname("方案三");
        createSolutionDto3.setSodds("0.9");
        createSolutionDtoList.add(createSolutionDto3);

        businessPara.put("solutions", createSolutionDtoList);

        String body = JSON.toJSONString(businessPara);
        System.out.println(body);
//        body = "{\"project\":{\"passet\":\"BTC\",\"pbegintime\":1568256941,\"pdescription\":\"测试下活动\",\"pendtime\":1568882520,\"plevel\":2,\"pmargin\":\"1\"," +
//                "\"pmaxusercount\":\"0.1\",\"pname\":\"9月30号活动\",\"proom\":11,\"pq\":\"0.2\"},\"solutions\":[{\"sname\":\"1\",\"sodds\":\"2.5863\"," +
//                "\"solutionid\":1},{\"sname\":\"2\",\"sodds\":\"2.5863\",\"solutionid\":2},{\"sname\":\"3\",\"sodds\":\"2.5088\",\"solutionid\":3}]}";

//        body = "{\"project\":{\"passet\":\"BTC\",\"pbegintime\":1568256941,\"pdescription\":\"测试下活动\",\"pendtime\":1568882520,\"plevel\":2,\"pmargin\":\"1\"," +
//                "\"pmaxusercount\":\"0.1\",\"pname\":\"9月30号活动\",\"proom\":11,\"pq\":\"0.2\",\"solutions\":[{\"sname\":\"1\",\"sodds\":\"2.5863\"," +
//                "\"solutionid\":1},{\"sname\":\"2\",\"sodds\":\"2.5863\",\"solutionid\":2},{\"sname\":\"3\",\"sodds\":\"2.5088\",\"solutionid\":3}]}}"
        String result = HttpUtil.postWithStringEntityParaAndHeader(url, new StringEntity(body, Charset.forName("utf-8")), HEADER);

        //{"project":{"passet":"BTC","pbegintime":1568256941,"pdescription":"测试下活动","pendtime":1568882520,"plevel":2,"pmargin":"1","pmaxusercount":"0.1","pname":"9月30号活动","proom":11,"pq":"0.2","solutions":[{"sname":"1","sodds":"2.5863","solutionid":1},{"sname":"2","sodds":"2.5863","solutionid":2},{"sname":"3","sodds":"2.5088","solutionid":3}]}}
        //{"project":{"passet":"OKG","pbegintime":1568266214,"pdescription":"测试项目11","pendtime":1568269204,"plevel":1,"pmargin":"1","pmaxusercount":10,   "pname":"项目名称","pq":"0.11","proom":11},"solutions":[{"sname":"方案一","sodds":"1.2","solutionid":1},{"sname":"方案二","sodds":"0.9","solutionid":2},{"sname":"方案三","sodds":"0.9","solutionid":3}]}
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_processing_room() throws Exception {
        String api = "/api/project/processing/list?proom=11&page_size=1";
        String url = BASE_URL + api;


        String result = HttpUtil.get(url);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_prodec_detail_solution() throws Exception {
        String api = "/api/project/processing/detail?pissue=17";
        String url = BASE_URL + api;


        String result = HttpUtil.getWithHeader(url, HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_0rders() throws Exception {
        String api = "/api/project/my_participate/orders?pissue=29";
        String url = BASE_URL + api;


        String result = HttpUtil.getWithHeader(url, HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_0rders_deatil() throws Exception {
        String api = "/api/project/my_participate/order/detail?orderno=100001";
        String url = BASE_URL + api;


        String result = HttpUtil.getWithHeader(url, HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_avail_assets() throws Exception {
        String api = "/api/avail_assests?auth_code=m3F3vD6T7nF7zvWGjwjlqeitqt8V%2B4B8EjCdaGxW5yg%3D";
        String url = BASE_URL + api;


        String result = HttpUtil.get(url);
        assert result != null;
        System.out.println("result============" + result);
    }



    @Test
    public void test_create_eorder() throws Exception {
        String api = "/api/order/create?boc_id=16777216";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();
        businessPara.put("opissue", 67);
        businessPara.put("osolutionid", 1);
        businessPara.put("ousercount", "0.01");
        businessPara.put("ouserorderno", UUID.randomUUID().toString());

        String body = JSON.toJSONString(businessPara);
        System.out.println(body);

        String result = HttpUtil.postWithStringEntityParaAndHeader(url, new StringEntity(body, Charset.forName("utf-8")), HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_participate_room() throws Exception {
        String api = "/api/project/my_participate/list";
        String url = BASE_URL + api;

        String result = HttpUtil.getWithHeader(url, HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_created() throws Exception {
        String api = "/api/project/my_created/list";
        String url = BASE_URL + api;

        String result = HttpUtil.getWithHeader(url, HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_created_detail() throws Exception {
        String api = "/api/project/my_created/detail?pissue=29";
        String url = BASE_URL + api;

        String result = HttpUtil.getWithHeader(url, HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }

    @Test
    public void test_user_created_projedt_orders() throws Exception {
        String api = "/api/project/my_created/orders?pissue=67";
        String url = BASE_URL + api;

        String result = HttpUtil.getWithHeader(url, HEADER);
        assert result != null;
        System.out.println("result============" + result);
    }


    @Test
    public void test_notify() throws Exception {
        String api = "/api/notify/bastion_pay";
        String url = BASE_URL + api;

        Map<String, Object> businessPara = new TreeMap<String, Object>();
        businessPara.put("merchantOrderNo", "a5239509-66b2-415c-a413-306512c2947e");
        businessPara.put("tradeOrderNo", "e0252f3d-b701-4db2-92d9-ff2007198ab6df");
        businessPara.put("status", "1");
        businessPara.put("assets", "OKG");
        businessPara.put("amount", "0.01");


        StringBuilder tempString = new StringBuilder();
        for (String key : businessPara.keySet()) {
            tempString.append(key);
            tempString.append("=");
            tempString.append(businessPara.get(key));
            tempString.append("&");
        }
        String str = tempString.substring(0, tempString.lastIndexOf("&"));

        RSASign rsaSign = new RSASign(OKGuessConfig.BASTION_OPEN_API_PUB_KEY, OKGuessConfig.OKGUESS_TO_BASTION_OPEN_API_PRI_KEY);
        String signature = rsaSign.sign(str);
        str = str + "&signature=" + signature;
//        str = "amount=1&assets=OKG&merchantOrderNo=6c1f" +
//                "b992-19b4-4038-abbe-700346f1eb26&status=3&tradeOrderNo=b37c853d-346a-4b31-8929-0d470b509ad7&signature=HVWkcI2uIOaLCYO63" +
//                "%2FqBwC8UBQB37ngi8469l%2FnnrazNCl6xNKdbz1Xl84A2Xr8WTkCvURrQWW0voj4ppo0NimtomAKmpEHF3EHwOUXgXZj6oO%2B0Z6tMd0WG7ZhuCL5Arxuo63yjwTP" +
//                "%2FwlPoUf2hZiGoaqYrsyeEUGSCboezpF7OPhMarkwtF9NAlNXvTRYV8sKkhBEhDxbpuMTqb%2BTIszqQV1gDR3cOgdsFaP3cPUO69Z5BqaQmFXRAI%2FEzRjvjDhTndMPyzBdP" +
//                "%2Fxl8UnvvLg8c8Sh2f8iyDpN5GLhixZvq4d4JQ5gZNTnxYrR83nLWPh2OR%2BhDLT7EetwySjb5EQ%3D%3D";


        String result = HttpUtil.postWithStringEntityParaAndHeader(url, new StringEntity(str, Charset.forName("utf-8")), HEADER);
        System.out.println("result============" + result);
    }
}
