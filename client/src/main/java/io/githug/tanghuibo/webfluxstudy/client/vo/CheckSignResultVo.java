package io.githug.tanghuibo.webfluxstudy.client.vo;

import java.util.List;

/**
 * @author tanghuibo
 * @date 2020/8/9上午5:12
 */
public class CheckSignResultVo {

    /**
     * 原始数据
     */
    private String srcData;

    /**
     * 签名
     */
    private String sign;

    /**
     * 校验结果
     */
    private Boolean checkResult;

    /**
     * 头信息
     */
    private List<HeaderInfoVo> headers;


    public String getSrcData() {
        return srcData;
    }

    public void setSrcData(String srcData) {
        this.srcData = srcData;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Boolean getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Boolean checkResult) {
        this.checkResult = checkResult;
    }

    public List<HeaderInfoVo> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HeaderInfoVo> headers) {
        this.headers = headers;
    }
}
