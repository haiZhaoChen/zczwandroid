package org.bigdata.zczw.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class Pictures implements Serializable {
    private long picId ;
    private String url ;

    public Pictures() {
    }

    public Pictures(long picId, String url) {
        this.picId = picId;
        this.url = url;
    }

    public long getPicId() {
        return picId;
    }

    public void setPicId(long picId) {
        this.picId = picId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Pictures{" +
                "picId=" + picId +
                ", url='" + url + '\'' +
                '}';
    }
}
