package org.bigdata.zczw.entity;

import java.util.List;

public class Catalogs {


    /**
     * status : 200
     * msg : OK
     * data : [{"id":100,"parentId":0,"name":"管理手册2-1PM"},{"id":110,"parentId":101,"name":"后勤管理"},{"id":111,"parentId":101,"name":"环境管理"},{"id":112,"parentId":101,"name":"行政管理"},{"id":113,"parentId":102,"name":"计划前期阶段"},{"id":114,"parentId":102,"name":"计划后期评价"},{"id":115,"parentId":102,"name":"计划实施阶段"},{"id":116,"parentId":103,"name":"人力资源篇"},{"id":117,"parentId":103,"name":"财务管理篇"},{"id":118,"parentId":104,"name":"党群篇"},{"id":119,"parentId":104,"name":"纪检篇"},{"id":120,"parentId":105,"name":"安全管理篇"},{"id":121,"parentId":105,"name":"应急管理篇"},{"id":122,"parentId":106,"name":"收费管理篇"},{"id":123,"parentId":107,"name":"信息监控篇"},{"id":124,"parentId":107,"name":"养护管理篇"},{"id":125,"parentId":108,"name":"信息监控篇"},{"id":126,"parentId":108,"name":"机电管理篇"},{"id":127,"parentId":109,"name":"党群管理篇"},{"id":128,"parentId":109,"name":"公共管理篇"},{"id":129,"parentId":109,"name":"养护管理篇"},{"id":130,"parentId":109,"name":"安全与应急篇_安全管理"},{"id":131,"parentId":109,"name":"常用表格"},{"id":132,"parentId":109,"name":"收费管理篇"},{"id":133,"parentId":109,"name":"机电与信息篇_信息监控"},{"id":134,"parentId":109,"name":"机电与信息篇_机电管理"},{"id":135,"parentId":109,"name":"计划管理篇"},{"id":136,"parentId":109,"name":"财务与人劳篇_人劳管理"},{"id":137,"parentId":109,"name":"财务与人劳篇_财务管理"},{"id":138,"parentId":100,"name":"管理手册"},{"id":101,"parentId":0,"name":"公共管理篇2-1PM"},{"id":102,"parentId":0,"name":"计划管理篇2-1PM"},{"id":103,"parentId":0,"name":"财务与人劳篇2-1PM"},{"id":104,"parentId":0,"name":"党群与纪检篇2-1PM"},{"id":105,"parentId":0,"name":"安全与应急篇2-1PM"},{"id":106,"parentId":0,"name":"收费管理篇2-1PM"},{"id":107,"parentId":0,"name":"养护管理篇2-1PM"},{"id":108,"parentId":0,"name":"机电与信息篇2-1PM"},{"id":109,"parentId":0,"name":"流程图索引2-1PM"}]
     */

    private int status;
    private String msg;
    private List<Catalog> data;

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

    public List<Catalog> getData() {
        return data;
    }

    public void setData(List<Catalog> data) {
        this.data = data;
    }

}
