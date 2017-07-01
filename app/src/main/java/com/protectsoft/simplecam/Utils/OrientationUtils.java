package com.protectsoft.simplecam.Utils;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.Display;
import android.view.Surface;
import android.widget.LinearLayout;

import com.protectsoft.simplecam.CameraFeatures;

/**
 */
public  class OrientationUtils {

    /**
     * <p>
     *     this method havent tested in many devices
     * </p>
     * @param orientation the current orientation of device
     * @return 0,90,180,270 <br>
     *
     */
    public static int getCurrentOrientation(int orientation) {
        int rotation = 0;

        if ((orientation <= 360 && orientation > 300) || (orientation >= 0 && orientation <= 60)) {
            rotation = 0;
        } else if (orientation <= 120 && orientation > 60) {
            rotation = 270;
        } else if (orientation > 120 && orientation <= 240) {
            rotation = 180;
        } else if (orientation > 240 && orientation <= 300) {
            rotation = 90;
        } else {
            //rotation = -1 = unknown == flat
            rotation = 0;
        }

        return rotation;

    }


    /**
     *
     * @param orientation the current device's orientation
     * @return return only  LinearLayout.HORIZONTAL 0 or LinearLayout.VERTICAL 1;
     */
    public static int getHorizontalOrVertical(int orientation) {

        if ((orientation <= 360 && orientation > 300) || (orientation >= 0 && orientation <= 60)) {
            return LinearLayout.VERTICAL;//1
        } else if (orientation <= 120 && orientation > 60) {
            return LinearLayout.HORIZONTAL;//0
        } else if (orientation > 120 && orientation <= 240) {
            return LinearLayout.VERTICAL;
        } else if (orientation > 240 && orientation <= 300) {
            return LinearLayout.HORIZONTAL;
        } else {
            //rotation = -1 = unknown
            return LinearLayout.HORIZONTAL;
        }

    }

    public static int getRotationForPictureTakeCallBack(int mOrientation) {
        int rotation = 0;
        if(CameraFeatures.cameraId == 0) {
            if ((mOrientation <= 360 && mOrientation >= 300) || (mOrientation >= 0 && mOrientation <= 60)) {
                rotation = 90;
            } else if (mOrientation <= 120 && mOrientation > 60) {
                rotation = 180;
            } else if (mOrientation > 120 && mOrientation <= 240) {
                rotation = 270;
            } else if (mOrientation > 240 && mOrientation <= 300) {
                rotation = 0;
            } else {
                //rotation = -1 = unknown
                rotation = 0;
            }    //cameraid == 1 => front camera in use
        } else if(CameraFeatures.cameraId == 1) {
            if ((mOrientation <= 360 && mOrientation >= 300) || (mOrientation >= 0 && mOrientation <= 60)) {
                rotation = 270;
            } else if (mOrientation <= 120 && mOrientation > 60) {
                rotation = 180;
            } else if (mOrientation > 120 && mOrientation <= 240) {
                rotation = 90;
            } else if (mOrientation > 240 && mOrientation <= 300) {
                rotation = 0;
            } else {
                //rotation = -1 = unknown
                rotation = 0;
            }
        }
        return rotation;
    }


    public static MediaRecorder getMediaRecorder(int mOrientation) {
        MediaRecorder mediaRecorder = new MediaRecorder();
        //set video recording degree orientation
        if(CameraFeatures.cameraId == 0) { //back camera in use
            if ((mOrientation <= 360 && mOrientation >= 300) || (mOrientation >= 0 && mOrientation <= 60)) {
                mediaRecorder.setOrientationHint(90);
            } else if (mOrientation <= 120 && mOrientation > 60) {
                mediaRecorder.setOrientationHint(180);
            } else if (mOrientation > 120 && mOrientation <= 240) {
                mediaRecorder.setOrientationHint(270);
            } else if (mOrientation > 240 && mOrientation <= 300) {
                mediaRecorder.setOrientationHint(0);
            } else {
                //rotation = -1 = unknown
                mediaRecorder.setOrientationHint(0);
            }
        } else if(CameraFeatures.cameraId == 1) { //front camera in use
            if ((mOrientation <= 360 && mOrientation >= 300) || (mOrientation >= 0 && mOrientation <= 60)) {
                mediaRecorder.setOrientationHint(270);
            } else if (mOrientation <= 120 && mOrientation > 60) {
                mediaRecorder.setOrientationHint(180);
            } else if (mOrientation > 120 && mOrientation <= 240) {
                mediaRecorder.setOrientationHint(90);
            } else if (mOrientation > 240 && mOrientation <= 300) {
                mediaRecorder.setOrientationHint(0);
            } else {
                //rotation = -1 = unknown
                mediaRecorder.setOrientationHint(0);
            }
        }

        return mediaRecorder;

    }

    /** <p>
     * sets the camera DisplayOrientation for the preview
     * </p>
     *
     * @param cameraid 0 or 1 usualy front or back camera
     * @param cm the camera instance
     * @param display
     */
    public static  void setCameraDisplayOrientation(int cameraid, Camera cm,Display display) {

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraid,info);
        int rotation = display.getRotation();
        int degrees =0;
        switch(rotation) {
            case Surface.ROTATION_0 : degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        //info.facing
        if (CameraFeatures.cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        if ("samsung".equals(Build.MANUFACTURER) &&
                "sf2wifixx".equals(Build.PRODUCT)) {
            cm.setDisplayOrientation(0);
        } else {
            cm.setDisplayOrientation(result);
        }

    }




}
