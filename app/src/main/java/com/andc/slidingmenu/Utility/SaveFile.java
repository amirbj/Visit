package com.andc.slidingmenu.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by y.jafari on 4/22/2015.
 */
public class SaveFile {
    private static SaveFile instance = new SaveFile();
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static SaveFile getInstance() {
        return instance;
    }

    private SaveFile()
    {
    }

    public void SaveFileInternal(String strBody, String path, String fileName) {
        FileOutputStream outputStream;
        try {

            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d(path, "failed to create directory");
                    return;
                }
            }
            File outputFile = new File(mediaStorageDir.getAbsolutePath() + File.separator + fileName);
            outputStream = new FileOutputStream(outputFile);
            outputStream.write(strBody.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String SaveImageInternal(Bitmap bmp, String path, String fileName){
        String rv = "";

        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d(path, "failed to create directory");
                    return "";
                }
            }

            File dest = new File(mediaStorageDir, fileName);

            FileOutputStream out = new FileOutputStream(dest);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.e("Saved at : ", mediaStorageDir.getAbsolutePath());
            rv = "file://" + dest.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rv;
    }

    public void ExportDatabase(String path, String dataBaseFileName, Context context) {
        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d(path, "failed to create directory");
                    return;
                }
            }

            File dest = new File(mediaStorageDir, "backup_" + dataBaseFileName);

            File currentDB = context.getDatabasePath(dataBaseFileName);
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(dest).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ImportDatabase(String path, String dataBaseFileName, Context context) {
        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                throw new Exception("Cann't find folder.");
            }

            File currentDB = new File(mediaStorageDir, "backup_" + dataBaseFileName);

            File dest = context.getDatabasePath(dataBaseFileName);
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(dest).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Create a file Uri for saving an image or video */
    public Uri getOutputMediaFileUri(int type, String path, String fileName){
        return Uri.fromFile(getOutputMediaFile(type, path, fileName));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type, String path, String fileName){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(path, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    fileName + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    fileName + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
