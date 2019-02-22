package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by darg on 2017/4/25.
 */

public class RecoedBean implements Serializable {

    /**
     * status : 200
     * msg : OK
     * data : {"author":{"id":1430,"name":"王泽","portrait":"http://150od17078.51mypc.cn:9002/images/2017/04/13/1492052915876639.jpg","phone":"18832466885"},"content":"管理处\u201c道德讲堂\u201d进站区\n为强化职工礼仪意识，培养文明礼仪习惯，通过职工学礼仪、懂礼仪、倡礼仪，从而深入推进收费站思想道德建设，4月18日，隆化站邀请处办公室徐广德主任一行于到站内开展文明礼仪专题讲座。\n在讲座中，徐主任围绕文明礼仪的概念、个人礼仪以及工作礼仪三个方面进行了讲解，用鲜活的实例、图片生动阐述了工作礼仪的实际应用，同时，徐主任还深入讲解了介绍礼仪、仪表礼仪、行为礼仪、就餐礼仪、微信礼仪等实用内容。职工们深有感触，纷纷表示要把学到的礼仪知识运用到实际生活和工作中，做一个既懂文化、又讲礼仪的好职工。\n     讲座结束后，处办公室王泽对收费站职工关心的信息宣传工作与大家进行了座谈讨论。(隆化收费站报送)","pictures":[],"video":null,"commentNum":0,"collectNum":0,"praiseNum":0,"sameUnit":2,"longitude":"117.962859","latitude":"41.005679","location":"中国河北省承德市双桥区普乐北路","collect":0,"praise":0,"publishedTime":1492517062000,"messageId":"1492517062384533163"}
     */

    private int status;
    private String msg;
    private Record data;

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

    public Record getData() {
        return data;
    }

    public void setData(Record data) {
        this.data = data;
    }

}
