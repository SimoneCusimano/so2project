package com.unica.so2.enotesrecorder.Model;

import java.util.Date;


public class Note {
    private String _id;
    private String _title;
    private String _content;
    private Date _lastEdit;
    private double _rating;

    public Note(){}

    public Note(String id, String title, String content, Date lastEdit, double rating) {
        this._id = id;
        this._title = title;
        this._content = content;
        this._lastEdit = lastEdit;
        this._rating = rating;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        this._content = content;
    }

    public Date getLastEdit() {
        return _lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this._lastEdit = lastEdit;
    }

    public double getRating() {
        return _rating;
    }

    public void setRating(double rating) {
        this._rating = rating;
    }
}
