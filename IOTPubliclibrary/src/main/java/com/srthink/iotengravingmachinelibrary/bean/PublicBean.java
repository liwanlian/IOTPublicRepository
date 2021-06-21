package com.srthink.iotengravingmachinelibrary.bean;

/**
 * @author liwanlian
 * @date 2021/5/13 10:49
 */
public class PublicBean {
    private String extend;

    public PublicBean(String extend) {
        this.extend = extend;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "PublicBean{" +
                "extend='" + extend + '\'' +
                '}';
    }
}
