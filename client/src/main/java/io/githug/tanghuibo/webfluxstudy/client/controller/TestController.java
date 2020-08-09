package io.githug.tanghuibo.webfluxstudy.client.controller;


import io.githug.tanghuibo.webfluxstudy.client.vo.CheckSignResultVo;
import io.githug.tanghuibo.webfluxstudy.client.vo.HeaderInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


/**
 * @author tanghuibo
 * @date 2020/8/9上午12:46
 */
@RestController
public class TestController {

    Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("getTest")
    public CheckSignResultVo getTest(ServerHttpRequest serverHttpRequest, @RequestParam("key") String key) {
        return checkSign(serverHttpRequest, key);
    }

    @PostMapping("postTest")
    public CheckSignResultVo postTest(ServerHttpRequest serverHttpRequest, @RequestBody String data) {
        return checkSign(serverHttpRequest, data);
    }

    private CheckSignResultVo checkSign(ServerHttpRequest serverHttpRequest, @RequestBody String data) {
        HttpHeaders headers = serverHttpRequest.getHeaders();
        String sign = headers.getFirst("sign");
        sign = sign == null ? "" : sign;
        String mySign = DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
        boolean checkResult = sign.equalsIgnoreCase(mySign);
        CheckSignResultVo resultVo = new CheckSignResultVo();
        resultVo.setSrcData(data);
        resultVo.setSign(sign);
        resultVo.setCheckResult(checkResult);
        resultVo.setHeaders(headers.entrySet().stream().map(entry -> {
            HeaderInfoVo headerInfoVo = new HeaderInfoVo();
            headerInfoVo.setKey(entry.getKey());
            headerInfoVo.setValues(entry.getValue());
            return headerInfoVo;
        }).collect(Collectors.toList()));
        return resultVo;

    }
}
