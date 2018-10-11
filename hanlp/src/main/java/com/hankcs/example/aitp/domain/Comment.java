package com.hankcs.example.aitp.domain;

/**
 * @author jianfei.yin
 * @create 2018-08-27 9:24 PM
 **/
public class Comment {

    private Long id;
    private String objId;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
