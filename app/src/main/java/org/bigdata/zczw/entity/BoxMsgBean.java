package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2018/3/5.
 */

public class BoxMsgBean {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":1,"creator":{"id":1110,"name":"陈海召","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/07/05/1499220134519199.jpg","phone":"18032098867","unitsName":"系统运维","jobsName":"其他"},"createDate":1519869021000,"commentId":null,"commentContent":null,"commentAtRange":null,"commentAtUserId":null,"praiseId":50951,"message":{"author":{"id":2067,"name":"张涛","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/11/13/1510558987873374.jpg","phone":"18032098869","unitsName":"系统运维","jobsName":"其他"},"content":"测试","pictures":[],"video":null,"commentNum":1,"collectNum":0,"praiseNum":2,"sameUnit":1,"longitude":"114.521335","latitude":"38.038359","location":"中国河北省石家庄市裕华区槐北路39号在河北科技大学附近","collect":0,"praise":1,"publishedTime":1519868767000,"messageId":"1519868767409092757","viewNum":2,"forwardMessage":null,"forwardNum":0,"forwardMessageId":null,"publicScope":1,"priority":0},"parentCommentCreator":null}]
     */

    private int status;
    private String msg;
    private List<BoxMsg> data;

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

    public List<BoxMsg> getData() {
        return data;
    }

    public void setData(List<BoxMsg> data) {
        this.data = data;
    }

}
