package com.unica.so2.enotesrecorder.Model;

/* This class represents the content of a note, such as the description and the audio file
*
* */
public class Content{
    private String _description;
    private String _audio;

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public String getAudio() {
        return _audio;
    }

    public void setAudio(String _audio) {
        this._audio = _audio;
    }
}
