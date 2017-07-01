package com.protectsoft.simplecam.jni.bitmapoperations;


import android.graphics.Bitmap;
import android.util.Log;

import java.nio.ByteBuffer;


/*
*
* native class and method calling c++ methods from jni/JniBitmapOperationsLibrary.cpp for heavy image processing
*
 */

public class JniBitmapHolder {

    private ByteBuffer handler = null;

    static   {
        try {
            System.loadLibrary("stlport_shared");
            System.loadLibrary("JniBitmapOperationsLibrary");
        } catch (Throwable th) {
            //LogToDatabaseService.appendLog(th,null);
            //throw this to inform calling method that loading library failed and to take different action
            try {
                throw new Throwable();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
    }

    private native ByteBuffer jniStoreBitmapData(Bitmap bitmap) ;

    private native Bitmap jniGetBitmapFromStoredBitmapData(ByteBuffer handler);

    private native void jniFreeBitmapData(ByteBuffer handler);

    //rotates inner bitmap data by 90 degrees counter clock wise*
    private native void jniRotateBitmapCcw90(ByteBuffer handler);

    /**rotates the inner bitmap data by 90 degrees clock wise*/ //
    private native void jniRotateBitmapCw90(ByteBuffer handler);

    //rotate 180 degree
    private native void jniRotateBitmap180(ByteBuffer handler);

    private native void jniCropBitmap(ByteBuffer handler,final int left,final int top,final int right,final int bottom);

    private native void jniFlipBitmapHorizontal(ByteBuffer handler);

    private native void jniFlipBitmapVertical(ByteBuffer handler);

    private native void jniScaleNNBitmap(ByteBuffer handler,final int newWidth,final int newHeight);


    public JniBitmapHolder() {

    }

    public JniBitmapHolder(final Bitmap bitmap) {
        storeBimap(bitmap);
    }

    public void storeBimap(final Bitmap bitmap) {
        if(handler != null) {
            freeBitmap();
        }
        handler = jniStoreBitmapData(bitmap);
    }

    public void freeBitmap() {
        if(handler == null) {
            return;
        }
        jniFreeBitmapData(handler);
        handler = null;
    }

    /**
     * Counter Clock Wise 90
     */
    public void rotateBitmapCcw90() {
        if(handler == null) {
            return;
        }
        jniRotateBitmapCcw90(handler);
    }

    /**
     * Counter wise 90
     */
    public void rotateBitmapCw90() {
        if(handler == null) {
            return;
        }
        jniRotateBitmapCw90(handler);
    }

    public void rotateBitmap180() {
        if(handler == null) {
            return;
        }
        jniRotateBitmap180(handler);
    }

    public void cropBitmap(final int left,final int top,final int right,final int bottom)
    {
        if(handler ==null)
            return;
        jniCropBitmap(handler, left, top, right, bottom);
    }

    public Bitmap getBitmap() {
        if(handler == null) {
            return null;
        }
        return jniGetBitmapFromStoredBitmapData(handler);
    }

    public Bitmap getBitmapAndFree() {
        final Bitmap bitmap = getBitmap();
        freeBitmap();
        return bitmap;
    }

    public void scaleBitmap(final int newWidth,final int newHeight) {
        if(handler == null) {
            return;
        }
        jniScaleNNBitmap(handler,newWidth,newHeight);
    }

    /**
     * flips a bitmap horizontally, as such: <br/>
     *
     * <pre>
     * 123    321
     * 456 => 654
     * 789    987
     * </pre>
     */
    public void flipBitmapHorizontal()
    {
        if(handler ==null)
            return;
        jniFlipBitmapHorizontal(handler);
    }

    /**
     * Flips the bitmap on the vertically, as such:<br/>
     *
     * <pre>
     * 123    789
     * 456 => 456
     * 789    123
     * </pre>
     */
    public void flipBitmapVertical()
    {
        if(handler ==null)
            return;
        jniFlipBitmapVertical(handler);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(handler == null) {
            return;
        }
        Log.w("DEBUG", "JNI bitmap wasnt freed nicely!!");
        freeBitmap();
    }


}
