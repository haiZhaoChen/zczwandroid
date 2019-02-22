package org.bigdata.zczw.entity;

import java.io.Serializable;

public class FileBean implements Serializable{


    /**
     * url : http://zczwstorage.ewonline.org:8094/files/2018/04/23/1524475079544033.txt
     * fileType : txt
     * name : ht.txt
     * size : 1650
     */

    private String url;
    private String fileType;
    private String name;
    private long size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
