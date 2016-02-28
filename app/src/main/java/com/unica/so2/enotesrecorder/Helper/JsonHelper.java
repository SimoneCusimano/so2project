package com.unica.so2.enotesrecorder.Helper;


import com.unica.so2.enotesrecorder.Model.Content;
import com.unica.so2.enotesrecorder.Model.Note;

import org.json.JSONObject;

public class JsonHelper {

       /** This method implements the serialization from the Content, passed as parameter,
        *  to a Json String
        *
        * @return String, throws an exception otherwise
        * */
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

    /** This method implements the deserialization from a String, passed as parameter,
     *  to a Content
     *
     * @return Content, throws an exception otherwise
     * */
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


    /** This method implements the serialization from the Note, passed as parameter,
     *  to a Json String
     *
     * @return String of the jsonObject created, throws an exception otherwise
     * */
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


    /** This method implements the deserialization from the json String, passed as parameter,
     *  to a Note
     *
     * @return Note , throws an exception otherwise
     * */
    public static Note deserializeNote(String jsonString) {
            Note note = new Note();
    
            try {
                JSONObject json = new JSONObject(jsonString);
                note.setId(json.getString("id"));
                note.setTitle(json.getString("title"));
                note.setContent(deserializeContent(json.getString("content")));
                note.setLastEdit(GenericHelper.stringToDate(json.getString("lastEdit")));
                note.setRating((float)json.getDouble("rating"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    
            return note;
        }

}
