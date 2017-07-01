package com.protectsoft.simplecam.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 */
public final class BitmapUtils {

    /**
     *
     * @param data the byte[] array that contains the image
     * @param reqWidth the imageview width
     * @param reqHeight the imageview height
     * @return fixed size bitmap for the given imageview
     */
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data,int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data,0,data.length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data,0,data.length,options);
    }

    /**
     *
     * @param res the filepath to the image
     * @param reqWidth the imageview width
     * @param reqHeight the imageview height
     * @return fixed size bitmap for the given imageview
     */
    public static  Bitmap decodeSampledBitmapFromFile(File res,int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(res.getAbsolutePath(), options);
    }

    /**
     *
     * @param options the BitmapFactoryOptions
     * @param reqWidth the imageview width
     * @param reqHeight the imageview height
     * @return int how many times the origila bitmap's size will be divided to be fit to the imageview
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



}
