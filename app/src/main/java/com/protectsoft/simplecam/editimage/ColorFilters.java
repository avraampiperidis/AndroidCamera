package com.protectsoft.simplecam.editimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ImageView;

/**
 */
public class ColorFilters {


    public static void colorDefault(ImageView imageView) {
        imageView.setColorFilter(null);
    }

    public static void doColorFilterRedLight(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0.9, 0, 0));
    }

    public static void doColorFilterRedDark(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0.5, 0, 0));
    }

    public static void doColorFilterMagentaLight(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0.9, 0, 0.9));
    }

    public static void doColorFilterMagentaDark(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0.5, 0, 0.5));
    }

    public static void doColorFilterBlueLight(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0, 0, 0.9));
    }

    public static void doColorFilterBlueDark(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0, 0, 0.5));
    }

    public static void doColorFilterCyanLight(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0, 0.5, 0.5));
    }

    public static void doColorFilterCyanDark(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0, 0.5, 0.5));
    }

    public static void doColorFilterGreenLight(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0, 0.9, 0));
    }

    public static void doColorFilterGreenDark(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0, 0.5, 0));
    }

    public static void doColorFilterWhiteLight(ImageView imageView) {
        imageView.setColorFilter(Color.WHITE,PorterDuff.Mode.LIGHTEN);
    }

    public static void doColorFilterWhiteDark(ImageView imageView) {
        imageView.setColorFilter(Color.WHITE,PorterDuff.Mode.DARKEN);
    }

    public static void doColorFilterYellowLight(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0.9, 0.9, 0));
    }

    public static void doColorFilterYellowDark(ImageView imageView,Bitmap bitmap) {
        imageView.setImageBitmap(doColorFilter(bitmap, 0.5, 0.5, 0));
    }




    private static Bitmap doColorFilter(Bitmap src,double red,double green,double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }




}
