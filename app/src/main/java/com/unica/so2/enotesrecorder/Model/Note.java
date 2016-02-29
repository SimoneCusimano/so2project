package com.unica.so2.enotesrecorder.Model;

import java.util.Date;


public class Note {
    private long _id;
    private String _title;
    private Content _content;
    private Date _lastEdit;
    private float _rating;

    public Note(){}

    public Note(long id, String title, Content content, Date lastEdit, float rating) {
        this._id = id;
        this._title = title;
        this._content = content;
        this._lastEdit = lastEdit;
        this._rating = rating;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public Content getContent() {
        return _content;
    }

    public void setContent(Content content) {
        this._content = content;
    }

    public Date getLastEdit() {
        return _lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this._lastEdit = lastEdit;
    }

    public float getRating() {
        return _rating;
    }

    public void setRating(float rating) {
        this._rating = rating;
    }
}
