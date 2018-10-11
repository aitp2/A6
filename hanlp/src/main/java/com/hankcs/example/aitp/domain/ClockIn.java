package com.hankcs.example.aitp.domain;


/**
 * @author jianfei.yin
 * @create 2018-08-27 9:23 PM
 **/
public class ClockIn {

    private Long id;
    private String objId;
    private String title;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
