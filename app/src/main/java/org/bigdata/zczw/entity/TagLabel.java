package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by darg on 2016/11/9.
 */
public class TagLabel implements Serializable {

    /**
     * msg : OK
     * data : [{"labelId":0,"isMy":true,"userIds":[{"userId":1108},{"userId":1109},{"userId":1110},{"userId":1111}],"labelName":"默认标签"}]
     * status : 200
     */
    private String msg;
    private List<TagDataEntity> data;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<TagDataEntity> data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public List<TagDataEntity> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

}
