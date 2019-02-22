package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by darg on 2016/10/28.
 */
public class CollectList {

    /**
     * msg : OK
     * data : [{"collectTime":1477554965000,"collectId":"415","imagePosition":null,"userName":"赵阳","userId":1109}]
     * status : 200
     */
    private String msg;
    private List<CollectUser> data;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<CollectUser> data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public List<CollectUser> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

}
