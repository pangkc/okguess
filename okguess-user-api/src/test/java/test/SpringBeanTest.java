package test;

import com.alibaba.fastjson.JSON;
import com.okguess.user.api.OKGuessConfig;
import com.okguess.user.api.aop.TokenCheck;
import com.okguess.user.api.dto.CreateSolutionDto;
import com.okguess.user.api.service.OKGuessService;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.util.*;


public class SpringBeanTest extends AbstractTestCase {


    @Autowired
    private OKGuessService okGuessService;

    @Test
    public void testActive() {
        okGuessService.activePayOrder("4712da6a-a1f2-474e-bb7c-c929c0f52a0e", "OKG", "100.11");
    }

}
