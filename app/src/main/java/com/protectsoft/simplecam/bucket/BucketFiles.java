package com.protectsoft.simplecam.bucket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.protectsoft.simplecam.Utils.MediaFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * arraylist with all the filePath picture taken and saved from camera capture
 *
 */
public class BucketFiles {

    private static ArrayList<File> picturesFileList = new ArrayList<>();


    public static boolean isPictureFileListEmpty() {
        return picturesFileList.isEmpty();
    }


    public static void addPictureFile(File file) {
        picturesFileList.add(file);
    }


    public static ArrayList<File> getPicturesFileList() {
        return picturesFileList;
    }


    public static void removePictureFile(File file) {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).equals(file)) {
                    picturesFileList.remove(i);
                }
            }
        }
    }


    public static void clearPictureFiles() {
        if(!picturesFileList.isEmpty()) {
            picturesFileList.clear();
        }
    }


    public static void initializeAllPicturesToBucket() {
        clearPictureFiles();

        File file = MediaFileUtils.getHomeFile();
        File files[] = file.listFiles();

        if(files != null && files.length > 0) {

            for(int i =0; i < files.length; i++) {

                if(files[i].exists()) {

                    if (files[i].getName().contains(".jpg")) {
                        if(files[i].length() >= 2048) {
                            addPictureFile(files[i]);
                        }
                    }

                }

            }

        }

        if(getPictureFileSize() > 0) {
            shortFileList();
        }

    }


    public static boolean isFileJpgNotEmptyData(File f) {
        if(f.getName().contains(".jpg")) {
            if(f.length() >= 2048) {
                return true;
            }
        }
        return false;
    }


    private static void shortFileList() {

        Collections.sort(picturesFileList, new Comparator<File>() {

            public int compare(File o1, File o2) {

                if (o1.lastModified() > o2.lastModified()) {
                    return -1;
                } else if ( o1.lastModified() < o2.lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });

    }


    public static File getNextPictureFile(File file) {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).equals(file)) {
                    if(++i < picturesFileList.size()) {
                        if (picturesFileList.get(i) != null) {
                            return picturesFileList.get(i);
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isNextPictureFileExists(File file) {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).equals(file)) {
                    if(++i < picturesFileList.size()) {
                        if (picturesFileList.get(i) != null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static File getPreviusPictureFile(File file) {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).equals(file)) {
                    if( --i >= 0) {
                        if (picturesFileList.get(i) != null) {
                            return picturesFileList.get(i);
                        }
                        break;
                    }
                    break;
                }
            }
        }
        return null;
    }

    public static boolean isPreviusPictureFileExists(File file) {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).equals(file)) {
                    if( --i >= 0) {
                        if (picturesFileList.get(i) != null) {
                            return true;
                        }
                        break;
                    }
                    break;
                }
            }
        }
        return false;
    }


    public static void removeDeletedFiles() {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(!picturesFileList.get(i).exists()) {
                    picturesFileList.remove(i);
                }
            }
        }
    }

    public static boolean isFileExists(File file) {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).equals(file)) {
                    return picturesFileList.get(i).exists();
                }
            }
        }
        return false;
    }

    //// TODO:  delete not working properly
    //on android 4.2 and above delete is disabled!
    // actualy remove delete functionality! leave it for photo album apps and etc.
    @Deprecated
    public static void deleteFileFromFileSystem(File file,Context context) {
        try {
            if (!picturesFileList.isEmpty()) {
                for (int i = 0; i < picturesFileList.size(); i++) {
                    if (picturesFileList.get(i).equals(file)) {
                        if (picturesFileList.get(i).exists()) {
                            picturesFileList.get(i).delete();
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(picturesFileList.get(i).getAbsolutePath()))));
                            picturesFileList.remove(i);
                        }
                    }
                }
            }
        } catch (Throwable th) {

        }
    }

    //use in test only
    @Deprecated
    public static void deleteFileFromFileSystem(File file) {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).equals(file)) {
                    if(picturesFileList.get(i).exists()) {
                        //picturesFileList.get(i).delete();
                        picturesFileList.remove(i);
                    }
                }
            }
        }
    }

    public static void deteleAllFiles() {
        if(!picturesFileList.isEmpty()) {
            for(int i =0; i < picturesFileList.size(); i++) {
                if(picturesFileList.get(i).exists()) {
                    picturesFileList.get(i).delete();
                }
            }
        }
    }

    /**
     *
     * @return never returns null
     */
    public static int getPictureFileSize() {
        return picturesFileList.size();
    }




}
