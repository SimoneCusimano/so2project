package com.unica.so2.enotesrecorder.Helper;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileHelper {

    public static String encodeFileInString(File file) {
        String encodedString = "";

        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            encodedString = Base64.encodeToString(bytes, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return encodedString;
    }

    public static void decodeStringInFile(String encodedString, String pathFile)
    {
        try {
            byte[] decoded = Base64.decode(encodedString, 0);
            FileUtils.writeByteArrayToFile(new File(pathFile), decoded);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedOutputStream mergeAudio(List<File> filesToMerge)
    {
        BufferedOutputStream bufferedOutputStream = null;

        try {
            for(int i=0; i<filesToMerge.size(); i++) {

                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filesToMerge.get(i).getPath()));
            }
            bufferedOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return bufferedOutputStream;
    }

    public static void writeJsonToFile(String filePath, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

