package org.bigdata.zczw.entity;

import java.io.Serializable;

/**
 * Created by darg on 2016/6/29.
 */
public class Video implements Serializable {

    private long id;
    private String path;
    private String thumbnail;
    private long length;
    private long size;

    public Video() {
    }

    public Video(long id, String path) {
        this.id = id;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", length=" + length +
                ", size=" + size +
                '}';
    }
}
