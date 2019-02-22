package org.bigdata.zczw.entity;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by darg on 2017/4/27.
 */

public class Num implements Serializable {

    /**
     * count : 1
     * msgType : MessageBox
     */

    private int count;
    @JsonProperty("msgType")
    private String msgType;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
