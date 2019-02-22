package org.bigdata.zczw.entity;

import java.io.Serializable;

public class IntegralListModel implements Serializable{

    private int id;

    /**{"status":200,"msg":"OK","data":{"all":[{"id":1,"publicIntegral":102,"majorIntegral":-1,"type":1,"userId":2077,"createDate":1547534294000,"name":"李子博","portrait":"http://192.168.1.149:12346/images/2018/07/24/1532428479442265.jpg","phone":"15031142492","unitsName":"系统运维","jobsName":"其他"},{"id":2,"publicIntegral":35,"majorIntegral":-1,"type":1,"userId":5,"createDate":1547534294000,"name":"杨立伟","portrait":"http://192.168.1.149:12346/images/2018/03/06/1520343293022905.jpg","phone":"18732493086","unitsName":"处领导","jobsName":"处长"},{"id":3,"publicIntegral":29,"majorIntegral":-1,"type":1,"userId":6,"createDate":1547534294000,"name":"田者野","portrait":null,"phone":"13832488886","unitsName":"办公室","jobsName":"主任"},{"id":4,"publicIntegral":28,"majorIntegral":-1,"type":1,"userId":54,"createDate":1547534294000,"name":"耿彦东","portrait":null,"phone":"18832466626","unitsName":"丰宁养护工区","jobsName":"副主任"},{"id":5,"publicIntegral":28,"majorIntegral":-1,"type":1,"userId":7,"createDate":1547534294000,"name":"郭文辉","portrait":null,"phone":"18832466608","unitsName":"办公室","jobsName":"副主任"},{"id":6,"publicIntegral":28,"majorIntegral":-1,"type":1,"userId":38,"createDate":1547534294000,"name":"田岩","portrait":null,"phone":"18832466799","unitsName":"机电科","jobsName":"科长"},{"id":7,"publicIntegral":27,"majorIntegral":-1,"type":1,"userId":39,"createDate":1547534294000,"name":"齐玉亮","portrait":"http://192.168.1.149:12346/images/2017/04/08/1491612166722057.jpg","phone":"13131196622","unitsName":"机电科","jobsName":"副科长"},{"id":8,"publicIntegral":27,"majorIntegral":-1,"type":1,"userId":8,"createDate":1547534294000,"name":"郭贵玉","portrait":null,"phone":"18832466681","unitsName":"养护科","jobsName":"副科长"},{"id":9,"publicIntegral":26,"majorIntegral":-1,"type":1,"userId":43,"createDate":1547534294000,"name":"李树志","portrait":null,"phone":"13803141766","unitsName":"信息调度中心","jobsName":"主任"},{"id":10,"publicIntegral":26,"majorIntegral":-1,"type":1,"userId":16,"createDate":1547534294000,"name":"吴军华","portrait":null,"phone":"18832466686","unitsName":"财务科","jobsName":"副科长"},{"id":11,"publicIntegral":25,"majorIntegral":-1,"type":1,"userId":44,"createDate":1547534294000,"name":"高晓宁","portrait":null,"phone":"18832466898","unitsName":"红旗监控调度分中心","jobsName":"副主任"},{"id":12,"publicIntegral":25,"majorIntegral":-1,"type":1,"userId":18,"createDate":1547534294000,"name":"刘彬","portrait":"http://192.168.1.149:12346/images/2017/10/18/1508293358076348.jpg","phone":"13403240990","unitsName":"计划科","jobsName":"科长"},{"id":13,"publicIntegral":24,"majorIntegral":-1,"type":1,"userId":20,"createDate":1547534294000,"name":"何江涛","portrait":"http://192.168.1.149:12346/images/2018/06/22/1529628722959038.jpg","phone":"18832466868","unitsName":"人劳科","jobsName":"科长"},{"id":14,"publicIntegral":24,"majorIntegral":-1,"type":1,"userId":46,"createDate":1547534294000,"name":"房亚林","portrait":null,"phone":"15100120302","unitsName":"隆化收费站","jobsName":"副站长"},{"id":15,"publicIntegral":24,"majorIntegral":-1,"type":1,"userId":19,"createDate":1547534294000,"name":"王计冬","portrait":null,"phone":"18832466698","unitsName":"计划科","jobsName":"副科长"},{"id":16,"publicIntegral":23,"majorIntegral":-1,"type":1,"userId":26,"createDate":1547534294000,"name":"窦文利","portrait":null,"phone":"18832466858","unitsName":"养护科","jobsName":"副科长"},{"id":17,"publicIntegral":23,"majorIntegral":-1,"type":1,"userId":47,"createDate":1547534294000,"name":"李建华","portrait":null,"phone":"18832466696","unitsName":"双滦北收费站","jobsName":"副站长"},{"id":18,"publicIntegral":22,"majorIntegral":-1,"type":1,"userId":31,"createDate":1547534294000,"name":"徐宝龙","portrait":null,"phone":"18803149288","unitsName":"安全科","jobsName":"科长"},{"id":19,"publicIntegral":22,"majorIntegral":-1,"type":1,"userId":48,"createDate":1547534294000,"name":"闫俊荣","portrait":"http://192.168.1.149:12346/images/2018/01/03/1514943466090317.jpg","phone":"13503146898","unitsName":"大滩养护工区","jobsName":"主任"},{"id":20,"publicIntegral":21,"majorIntegral":-1,"type":1,"userId":32,"createDate":1547534294000,"name":"张晓蕾","portrait":"http://192.168.1.149:12346/images/2016/10/29/1477699504696344.jpg","phone":"13803146156","unitsName":"党办室","jobsName":"副主任"}],"me":[{"id":1,"publicIntegral":102,"majorIntegral":-1,"type":1,"userId":2077,"createDate":1547534294000,"name":"李子博","portrait":"http://192.168.1.149:12346/images/2018/07/24/1532428479442265.jpg","phone":"15031142492","unitsName":"系统运维","jobsName":"其他"}]}}
     * 积分类型。
     1：公共
     2：专业
     3：公众号
     */
    private int type;
    private int publicIntegral;
    private int majorIntegral;
    private long userId;
    private String createDate;
    private String name;
    private String portrait;
    private String phone;
    private String unitsName;
    private String jobsName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPublicIntegral() {
        return publicIntegral;
    }

    public void setPublicIntegral(int publicIntegral) {
        this.publicIntegral = publicIntegral;
    }

    public int getMajorIntegral() {
        return majorIntegral;
    }

    public void setMajorIntegral(int majorIntegral) {
        this.majorIntegral = majorIntegral;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnitsName() {
        return unitsName;
    }

    public void setUnitsName(String unitsName) {
        this.unitsName = unitsName;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }
}
