package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by darg on 2017/4/19.
 */

public class ZanBean {


    /**
     * status : 200
     * msg : OK
     * data : [{"id":"391","ifnotsee":"1","createtime":1490867967000,"author":{"id":1109,"name":"赵阳","portrait":"http://150od17078.51mypc.cn:9002/images/2017/04/01/1491030833837262.jpg","phone":null},"messageId":"1490860742694614066","messageContent":"测试","pictures":[{"picId":"1490860742789284427","url":"http://150od17078.51mypc.cn:9002/images/2017/03/30/1490860742745952.jpg"},{"picId":"1490860742886820901","url":"http://150od17078.51mypc.cn:9002/images/2017/03/30/1490860742852019.jpg"},{"picId":"1490860742741944800","url":"http://150od17078.51mypc.cn:9002/images/2017/03/30/1490860742697786.jpg"}],"video":null}]
     */

    private int status;
    private String msg;
    private List<Zan> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Zan> getData() {
        return data;
    }

    public void setData(List<Zan> data) {
        this.data = data;
    }

}
