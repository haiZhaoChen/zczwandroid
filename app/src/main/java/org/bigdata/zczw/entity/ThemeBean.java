package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by darg on 2017/6/13.
 */

public class ThemeBean {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":1,"title":"哈哈","outline":"啦啦'\u2018\u2019","content":"<p><img src=\"http://localhost:8088/ueditor/jsp/upload/image/20170612/1497256096792080806.jpg\" title=\"1497256096792080806.jpg\" alt=\"5552.jpg\"/><\/p><p><br/><\/p><p style=\"text-indent:43px;text-align:justify;text-justify:inter-ideograph;line-height:27px\"><strong><span style=\"font-family: 仿宋;font-size: 21px\"><span style=\"font-family:仿宋\">该犯在服刑期间执行刑期变动情况：<\/span><\/span><\/strong><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">承德市中级人民法院于<\/span><\/span><span style=\";font-family:仿宋;font-size:21px\">2015年7月21日作出(2015)承刑执字第1179号刑事裁定，对其减去有期徒刑八个月。现刑期止于2018年8月14日。<\/span><\/p><p style=\"text-indent:43px;text-align:justify;text-justify:inter-ideograph;line-height:27px\"><strong><span style=\"font-family: 仿宋;font-size: 21px\"><span style=\"font-family:仿宋\">该犯在服刑期间<\/span><span style=\"font-family:仿宋\">悔改表现突出，具体事实如下：<\/span><\/span><\/strong><\/p><p style=\"text-indent:43px;text-align:justify;text-justify:inter-ideograph;line-height:27px\"><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">该犯在服刑改造期间，认罪悔罪；严格遵守法律、法规和监规，接受教育改造；积极参加思想、文化、职业技术教育，考试成绩合格；积极参加劳动，努力完成劳动任务。<\/span><\/span><\/p><p style=\"text-indent:43px;text-align:justify;text-justify:inter-ideograph;line-height:27px\"><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">该犯累计受<\/span><span style=\"font-family:仿宋\">记功奖励<\/span><\/span><span style=\";font-family:仿宋;font-size:21px\">1次、表扬奖励3次、年度狱级改造积极分子1次<\/span><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">。<\/span><\/span><\/p><p style=\"text-indent:43px;text-align:justify;text-justify:inter-ideograph;line-height:27px\"><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">为此，根据《中华人民共和国监狱法》第三十二条、《中华人民共和国刑法》第八十一条、《中华人民共和国刑事诉讼法》第二百六十二条（二）款的规定，建议对罪犯<\/span><span style=\"font-family:仿宋\">计海予以假释。<\/span><\/span><\/p><p style=\"text-indent:43px;text-align:justify;text-justify:inter-ideograph;line-height:27px\"><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">特提请审核裁定。<\/span><\/span><\/p><p style=\"text-indent:43px;line-height:27px\"><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">此致<\/span><\/span><\/p><p style=\"line-height:27px\"><span style=\";font-family:仿宋;font-size:21px\"><span style=\"font-family:仿宋\">承德市中级人民法院<\/span><\/span><\/p><p><br/><\/p><p><br/><\/p>","deleteflag":"1","createtime":1497256161000,"updtime":null,"author":{"id":1108,"name":"任忠","portrait":"http://150od17078.51mypc.cn:9002/images/2017/05/24/1495636364245664.jpg","phone":"18032098865"},"comments":[{"c_id":2,"c_content":"1234567","c_createtime":1497261369000,"c_updtime":1497261369000,"c_userId":1108,"c_userName":"任忠","c_userPhone":"18032098865","c_portrait":"http://150od17078.51mypc.cn:9002/images/2017/05/24/1495636364245664.jpg"},{"c_id":1,"c_content":"123","c_createtime":1497261077000,"c_updtime":1497261077000,"c_userId":1108,"c_userName":"任忠","c_userPhone":"18032098865","c_portrait":"http://150od17078.51mypc.cn:9002/images/2017/05/24/1495636364245664.jpg"}]}]
     */

    private int status;
    private String msg;
    private Theme data;

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

    public Theme getData() {
        return data;
    }

    public void setData(Theme data) {
        this.data = data;
    }
}
