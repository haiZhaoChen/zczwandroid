package org.bigdata.zczw.entity;

public class AttendMessage {
    /**
     * id : 39
     * user : {"id":2068,"name":"冀雷","sex":"男","portrait":"http://zczwstorage.ewonline.org:8094/images/2018/04/12/1523501361240771.jpg","phone":"15830985135","unitsName":"系统运维","jobsName":"其他"}
     * createDate : 1526016834000
     * sourceType : 1
     * leave : {"id":14,"userId":2068,"beginDate":1525968000000,"days":1,"endDate":1525968000000,"attendSubType":3,"status":1,"createDate":1526016834000,"updateDate":1526016851000}
     * tiaoXiu : null
     */

    private int id;
    private Author user;
    private long createDate;
    private int sourceType;
    private Leave leave;
    private TiaoXiu tiaoXiu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author getUser() {
        return user;
    }

    public void setUser(Author user) {
        this.user = user;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public TiaoXiu getTiaoXiu() {
        return tiaoXiu;
    }

    public void setTiaoXiu(TiaoXiu tiaoXiu) {
        this.tiaoXiu = tiaoXiu;
    }
}
