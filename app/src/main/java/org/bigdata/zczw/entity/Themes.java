package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by darg on 2017/6/12.
 */

public class Themes implements Serializable{

    /**
     * status : 200
     * msg : OK
     * data : [{"id":3,"title":"777234566","outline":"88882","deleteflag":"1","createid":1111,"createtime":1496821441000,"updid":1111,"updtime":1496971380000,"content":"<p>9999<\/p>"},{"id":4,"title":"11","outline":"22233","deleteflag":"1","createid":1111,"createtime":1496821870000,"updid":1111,"updtime":1496971975000,"content":"<p>888<\/p>"},{"id":5,"title":"11","outline":"222","deleteflag":"1","createid":1111,"createtime":1496821897000,"updid":1111,"updtime":1496821897000,"content":"<p>88899<\/p>"},{"id":6,"title":"快要下班了2","outline":"下班去吃饭，吃完饭打会篮球，然后学习,我的减肥的龙卷风的 啊发动机︿(￣︶￣)︿O(∩_∩)O好的O(∩_∩)O好的O(∩_∩)O好的,我的减肥的龙卷风的 啊发动机︿(￣︶￣)︿O(∩_∩)O好的O(∩_∩)O好的O(∩_∩)O好的","deleteflag":"1","createid":1111,"createtime":1496828723000,"updid":1111,"updtime":1496974013000,"content":"<p>我的减肥的龙卷风的 啊发动机︿(￣︶￣)︿O(∩_∩)O好的O(∩_∩)O好的O(∩_∩)O好的<\/p><p><br/><\/p><p>拉法基&lt;(￣︶￣)&gt;O(∩_∩)O嗯!O(∩_∩)O嗯!O(∩_∩)O嗯!╰(￣▽￣)╭<\/p><p><img src=\"http://localhost:8088/ueditor/jsp/upload/image/20170607/1496828696482041957.jpg\" title=\"1496828696482041957.jpg\" alt=\"5552.jpg\"/><\/p><p><br/><\/p><p style=\"margin-left:7px;text-indent:32px;line-height:27px\"><span style=\"font-size:21px;font-family:仿宋\">罪犯<a><\/a>姚海勤，男，<a><\/a>1966年3月28日出生，<a><\/a>汉族，<a><\/a>河北省青龙县人，因<a><\/a>贩卖毒品罪经<a><\/a>河北省宽城满族自治县人民法院于<a><\/a>2013年4月18日以<a><\/a>(2013)宽刑初字第43号刑事判决判处<a><\/a>有期徒刑九年。<a><\/a>刑期自<a><\/a>2012年9月6日起至<a><\/a>2021年9月5日止。于<a><\/a>2013年5月15日由<a><\/a>宽城满族自治县看守所送押我狱服刑改造。<\/span><\/p><p style=\"text-align:justify;text-justify:inter-ideograph;text-indent: 43px;line-height:27px\"><a><\/a><strong><span style=\"font-size: 21px;font-family:仿宋\">该犯财产刑执行情况：<\/span><\/strong><span style=\"font-size:21px;font-family:仿宋\">罚金一万元未缴纳。<\/span><\/p><p style=\"text-align:justify;text-justify:inter-ideograph;text-indent: 43px;line-height:27px\"><strong><span style=\"font-size:21px;font-family: 仿宋\">该犯在服刑期间<a><\/a>确有悔改表现，具体事实如下：<\/span><\/strong><\/p><p style=\"text-align:justify;text-justify:inter-ideograph;text-indent: 43px;line-height:27px\"><span style=\"font-size:21px;font-family:仿宋\">该犯在服刑改造期间，认罪悔罪；严格遵守法律、法规和监规，接受教育改造；积极参加思想、文化、职业技术教育，考试成绩合格；积极参加劳动，努力完成劳动任务。<\/span><\/p><p style=\"text-align:justify;text-justify:inter-ideograph;text-indent: 43px;line-height:27px\"><span style=\"font-size:21px;font-family:仿宋\">该犯累计受<a><\/a>表扬奖励5次。<\/span><\/p><p style=\"text-align:justify;text-justify:inter-ideograph;text-indent: 43px;line-height:27px\"><a><\/a><span style=\"font-size:21px;font-family:仿宋\">为此，根据《中华人民共和国监狱法》第二十九条、《中华人民共和国刑法》第七十八条、七十九条、《中华人民共和国刑事诉讼法》第二百六十二条（二）款的规定，建议对罪犯<a><\/a>姚海勤减去有期徒刑九个月。<\/span><\/p><p style=\"text-align:justify;text-justify:inter-ideograph;text-indent: 43px;line-height:27px\"><span style=\"font-size:21px;font-family:仿宋\">特提请审核裁定。<\/span><\/p><p style=\"text-indent:43px;line-height:27px\"><span style=\"font-size:21px;font-family:仿宋\">此致<\/span><\/p><p style=\"line-height:27px\"><span style=\"font-size:21px;font-family:仿宋\">承德市中级人民法院<\/span><\/p><p><span style=\"font-size:21px;font-family:仿宋\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<\/span><\/p><p><br/><\/p>"}]
     */

    private int status;
    private String msg;
    private List<Theme> data;

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

    public List<Theme> getData() {
        return data;
    }

    public void setData(List<Theme> data) {
        this.data = data;
    }

}
