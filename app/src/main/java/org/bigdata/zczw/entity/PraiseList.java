package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by darg on 2016/10/28.
 */
public class PraiseList {

    /**
     * msg : OK
     * data : [{"praiseTime":1477554911000,"imagePosition":null,"praiseId":"315","userName":"赵阳","userId":1109}]
     * status : 200
     */
    private String msg;
    private List<PraiseUser> data;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<PraiseUser> data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public List<PraiseUser> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }


}
