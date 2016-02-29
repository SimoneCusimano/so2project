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
    private static final String TAG = "FileHelper";

    /*Takes a file as parameters and codifies it in a base64 string*/
    public static String encodeFileInString(File file) {
        String encodedString = "";

        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            encodedString = Base64.encodeToString(bytes, 0);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return encodedString;
    }

    /* Takes a base64 string as parameters and decodes it in a file
     * with the path specified by the parameter */
    public static void decodeStringInFile(String encodedString, String pathFile)
    {
        try {
            byte[] decoded = Base64.decode(encodedString, 0);
            FileUtils.writeByteArrayToFile(new File(pathFile), decoded);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /*Implements the merging of a list of files */
    public static BufferedOutputStream mergeAudio(List<File> filesToMerge)
    {
        BufferedOutputStream bufferedOutputStream = null;

        try {
            for(int i=0; i<filesToMerge.size(); i++) {

                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filesToMerge.get(i).getPath()));
            }
            bufferedOutputStream.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return bufferedOutputStream;
    }

    /*Write a json string into a file, passed as parameters*/
    public static void writeJsonToFile(String filePath, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}

