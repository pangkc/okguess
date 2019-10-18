package test;

import com.alibaba.fastjson.JSON;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class BaseTest {

    public static final Map<String, String> HEADER = new HashMap<String, String>();

    public static final String BASE_URL = "http://localhost:8080";

    //public static final String BASE_URL = "http://3.92.46.183:8080/okguesss-user-api-1.0";

    @Before
    public void init() {
        HEADER.put("Content-Type", "application/json; charset=utf-8");
        HEADER.put("TOKEN", "057d7ce9-2d66-4000-99de-f9007b08a189");
    }

    public String getRequestBody(Map<String, Object> businessPara) throws Exception {

        return JSON.toJSONString(businessPara);
    }

}
