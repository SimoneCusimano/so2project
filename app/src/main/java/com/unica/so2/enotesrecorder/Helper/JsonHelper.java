package com.unica.so2.enotesrecorder.Helper;


import com.unica.so2.enotesrecorder.Model.Content;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class JsonHelper {

        /*serialization: from Content to Json string*/
        public static String serializeContent(Content content) {

            String jsonString = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("description", content.getDescription());
                jsonObject.put("audio", content.getAudio());
                jsonString = jsonObject.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return jsonString;
        }

        // deserialization: from Json string to Content
        public static Content deserializeContent(String jsonString) {
            Content content = new Content();

            try {
                JSONObject json = new JSONObject(jsonString);
                content.setDescription(json.getString("description"));
                content.setAudio(json.getString("audio"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return content;
        }
    
        /*serialization: from Note to Json string*/
        public static String serializeNote(Note note) {
    
            String jsonString = "";
    
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", note.getId());
                jsonObject.put("title", note.getTitle());
                jsonObject.put("content", serializeContent(note.getContent()));
                jsonObject.put("lastEdit", note.getLastEdit());
                jsonObject.put("rating", note.getRating());
                jsonString = jsonObject.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    
            return jsonString;
        }
    
        // deserialization: from Json string to Note
        public static Note deserializeNote(String jsonString) {
            Note note = new Note();
    
            try {
                JSONObject json = new JSONObject(jsonString);
                note.setId(json.getString("id"));
                note.setTitle(json.getString("title"));
                note.setContent(deserializeContent(json.getString("content")));
                note.setLastEdit(GenericHelper.stringToDate(json.getString("lastEdit")));
                note.setRating(json.getDouble("rating"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    
            return note;
        }

}
