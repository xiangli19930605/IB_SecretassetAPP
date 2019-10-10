package com.idealbank.module_main.Netty;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImageUtils {


    public  static byte[] getImg(File file) {
        byte[] data = null;

        FileInputStream input = null;
        try {
            input=new FileInputStream(file);
            ByteArrayOutputStream output=new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while((numBytesRead=input.read(buf))!=-1) {
                output.write(buf,0,numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }catch (Exception e) {
            // TODO: handle exception
        }
        return data;
    }

    public  static void setImg(byte[] data) {
        if (data.length < 3) {
            return;
        }
        try {
            char separator = File.separatorChar;
//			String path = System.getProperty("user.dir") + separator + "recieve" + separator
//					+ System.currentTimeMillis() + ".png";
            String path = "/storage/emulated/0/DCIM/camera/IMG_66666666.png";
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);
            output.write(data, 0, data.length);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public  static void setExcl(byte[] data) {
        if (data.length < 3) {
            return;
        }
        try {
            char separator = File.separatorChar;
//			String path = System.getProperty("user.dir") + separator + "recieve" + separator
//					+ System.currentTimeMillis() + ".png";
//            String path = "/storage/emulated/0/DCIM/camera/IMG_66666666.xlsx";
            String path = AppConstants.getSDPath() + AppConstants.ROOT_PATH + AppConstants.HISTORY_FILE_PATH + "/" + "sdas.xlsx";
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);
            output.write(data, 0, data.length);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public  static void setExclList( ArrayList<String> fileName,ArrayList<byte[]> fileData) {

        ArrayList<byte[]> files=fileData;
        ArrayList<String> name=fileName;

        for (int i = 0; i < files.size(); i++) {
                setExcl(files.get(i),name.get(i),AppConstants.HISTORY_FILE_PATH);
        }

    }
    public  static void setExcl(byte[] data,String name,String path) {
        if (data.length < 3) {
            return;
        }
        try {
            char separator = File.separatorChar;
//			String path = System.getProperty("user.dir") + separator + "recieve" + separator
//					+ System.currentTimeMillis() + ".png";
//            String path = "/storage/emulated/0/DCIM/camera/IMG_66666666.xlsx";
//            String path = AppConstants.getSDPath() + AppConstants.ROOT_PATH + AppConstants.HISTORY_FILE_PATH + "/" + "sdas.xlsx";
            File file = new File(path+"/"+name);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);
            output.write(data, 0, data.length);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }



}
