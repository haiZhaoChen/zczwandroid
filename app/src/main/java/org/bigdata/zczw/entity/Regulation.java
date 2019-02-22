package org.bigdata.zczw.entity;

import java.io.Serializable;

public class Regulation implements Serializable{
    /**
     * id : 493
     * name : 管理手册
     * code :
     * catalogId : 138
     * url : http://zczwstorage.ewonline.org:8094/regulations/0管理手册2-1PM/0管理手册2-1PM_管理手册_管理手册.PDF
     */

    private int id;
    private String name;
    private String code;
    private int catalogId;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
