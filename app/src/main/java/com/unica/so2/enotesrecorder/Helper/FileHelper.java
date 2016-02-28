package com.unica.so2.enotesrecorder.Helper;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;

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

    public static void mergeAudios(File audioFileOne, File audioFileTwo, String outputFilePath){

        try {
            FileInputStream streamOne = new FileInputStream(audioFileOne);
            FileInputStream streamTwo = new FileInputStream(audioFileTwo);
            SequenceInputStream sequenceInputStream = new SequenceInputStream(streamOne, streamTwo);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFilePath));

            int temp;

            while( (temp = sequenceInputStream.read()) != -1) {
                fileOutputStream.write(temp);
            }

            fileOutputStream.close();
            sequenceInputStream.close();
            streamOne.close();
            streamTwo.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

