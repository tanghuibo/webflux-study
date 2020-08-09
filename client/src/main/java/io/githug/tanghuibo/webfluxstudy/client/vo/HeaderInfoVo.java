package io.githug.tanghuibo.webfluxstudy.client.vo;

import java.util.List;

/**
 * @author tanghuibo
 * @date 2020/8/9上午5:16
 */
public class HeaderInfoVo {

    /**
     * key
     */
    private String key;

    /**
     * value
     */
    private List<String> values;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
