package com.hankcs.example.aitp.domain;


/**
 * @author jianfei.yin
 * @create 2018-08-27 9:27 PM
 **/
public class Image {

    private Long id;
    private String objId;
    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
