package com.unica.so2.enotesrecorder.Helper;


import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class JsonHelper {

        /*serialization: from Content to Json string*/
        public String serializeContent (Content content) {
            String jsonString = "";
            try {
                FileOutputStream fout = new FileOutputStream("thenote.note");
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(note);
                oos.close();
            }
            catch (Exception e) { e.printStackTrace(); }
            return jsonString;
        }
        // deserialization: from Json string to Content
        public Content deserializeContent (String jsonString) {

        }
}
