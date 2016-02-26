package com.unica.so2.enotesrecorder.Model;

import java.util.Date;

/**
 * Created by s.cusimano on 26-Feb-16.
 */
public class Note {
    private String _id;
    private String _title;
    private String _body;
    private Date _lastEdit;
    private Byte[] _audio;
    private double _rating;

    public Note(String id, String title, String body, Date lastEdit, Byte[] audio, double rating) {
        this._id = id;
        this._title = title;
        this._body = body;
        this._lastEdit = lastEdit;
        this._audio = audio;
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

    public String getBody() {
        return _body;
    }

    public void setBody(String body) {
        this._body = body;
    }

    public Date getLastEdit() {
        return _lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this._lastEdit = lastEdit;
    }

    public Byte[] getAudio() {
        return _audio;
    }

    public void setAudio(Byte[] audio) {
        this._audio = audio;
    }

    public double getRating() {
        return _rating;
    }

    public void setRating(double rating) {
        this._rating = rating;
    }
}
