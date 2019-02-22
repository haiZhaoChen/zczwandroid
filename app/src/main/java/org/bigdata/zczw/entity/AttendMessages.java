package org.bigdata.zczw.entity;

import java.util.List;

public class AttendMessages {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":39,"user":{"id":2068,"name":"冀雷","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/04/12/1523501361240771.jpg","phone":"15830985135","unitsName":"系统运维","jobsName":"其他"},"createDate":1526016834000,"sourceType":1,"leave":{"id":14,"userId":2068,"beginDate":1525968000000,"days":1,"endDate":1525968000000,"attendSubType":3,"status":1,"createDate":1526016834000,"updateDate":1526016851000},"tiaoXiu":null},{"id":38,"user":{"id":2067,"name":"张涛","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/05/02/1525232462303262.jpg","phone":"18032098869","unitsName":"系统运维","jobsName":"办事员"},"createDate":1526007648000,"sourceType":1,"leave":{"id":13,"userId":2067,"beginDate":1525968000000,"days":1,"endDate":1525968000000,"attendSubType":1,"status":0,"createDate":1526007648000,"updateDate":1526007648000},"tiaoXiu":null},{"id":28,"user":{"id":2069,"name":"宁培培","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/10/26/1508990118920137.jpg","phone":"15097328990","unitsName":"系统运维","jobsName":"其他"},"createDate":1525937824000,"sourceType":1,"leave":{"id":12,"userId":2069,"beginDate":1525881600000,"days":1,"endDate":1525881600000,"attendSubType":1,"status":0,"createDate":1525937824000,"updateDate":1525937824000},"tiaoXiu":null},{"id":27,"user":{"id":2069,"name":"宁培培","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/10/26/1508990118920137.jpg","phone":"15097328990","unitsName":"系统运维","jobsName":"其他"},"createDate":1525937796000,"sourceType":1,"leave":{"id":11,"userId":2069,"beginDate":1526227200000,"days":3,"endDate":1526400000000,"attendSubType":1,"status":0,"createDate":1525937796000,"updateDate":1525937796000},"tiaoXiu":null},{"id":25,"user":{"id":2068,"name":"冀雷","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/04/12/1523501361240771.jpg","phone":"15830985135","unitsName":"系统运维","jobsName":"其他"},"createDate":1525937299000,"sourceType":1,"leave":{"id":10,"userId":2068,"beginDate":1525881600000,"days":1,"endDate":1525881600000,"attendSubType":3,"status":1,"createDate":1525937299000,"updateDate":1525937311000},"tiaoXiu":null},{"id":23,"user":{"id":2066,"name":"运维中心","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/03/21/1521639769246990.jpg","phone":"13931174097","unitsName":"系统运维","jobsName":"科员"},"createDate":1525933126000,"sourceType":1,"leave":{"id":9,"userId":2066,"beginDate":1525881600000,"days":1,"endDate":1525881600000,"attendSubType":1,"status":1,"createDate":1525933126000,"updateDate":1526003917000},"tiaoXiu":null},{"id":20,"user":{"id":2066,"name":"运维中心","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/03/21/1521639769246990.jpg","phone":"13931174097","unitsName":"系统运维","jobsName":"科员"},"createDate":1525933102000,"sourceType":1,"leave":{"id":8,"userId":2066,"beginDate":1525881600000,"days":1,"endDate":1525881600000,"attendSubType":2,"status":1,"createDate":1525933102000,"updateDate":1525933108000},"tiaoXiu":null},{"id":14,"user":{"id":2066,"name":"运维中心","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/03/21/1521639769246990.jpg","phone":"13931174097","unitsName":"系统运维","jobsName":"科员"},"createDate":1525932743000,"sourceType":1,"leave":{"id":7,"userId":2066,"beginDate":1525881600000,"days":1,"endDate":1525881600000,"attendSubType":2,"status":1,"createDate":1525932743000,"updateDate":1525932872000},"tiaoXiu":null},{"id":13,"user":{"id":2068,"name":"冀雷","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/04/12/1523501361240771.jpg","phone":"15830985135","unitsName":"系统运维","jobsName":"其他"},"createDate":1525925030000,"sourceType":1,"leave":{"id":6,"userId":2068,"beginDate":1528560000000,"days":1,"endDate":1528646400000,"attendSubType":1,"status":0,"createDate":1525925030000,"updateDate":1525925030000},"tiaoXiu":null}]
     */

    private int status;
    private String msg;
    private List<AttendMessage> data;

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

    public List<AttendMessage> getData() {
        return data;
    }

    public void setData(List<AttendMessage> data) {
        this.data = data;
    }

}
