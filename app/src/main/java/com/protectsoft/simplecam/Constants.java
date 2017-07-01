package com.protectsoft.simplecam;

import android.graphics.Bitmap;

import java.io.File;

/**
 */
public final class Constants {

    private Constants() {}

    //the last picture taken to be set as preview at surface view
    public static Bitmap bitmap = null;

    //and the file to the last picture taken
    public static File lastpicturefiletaken = null;

    public static final int jpegquality = 70;


}
