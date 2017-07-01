package com.protectsoft.simplecam;

import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.List;

/**
 */
public final class CameraFeatures {

    public static boolean frontcam;
    public static boolean rearcam;
    public static boolean hasFlash;
    public static boolean hasAutofocus;
    public static boolean hasSmoothZoom;
    public static boolean hasZoom;

    public static boolean shuttersound = true;
    public static boolean canDisableShutterSound;

    public static boolean isTakingpicture;

    public static int cameraId = 0;

    public static Camera.Parameters params;



    public static void setDevicehardwareSupport(Camera.Parameters params, PackageManager pm) {

            //check flash
            if(params.getFlashMode() != null) {
                List<String> supportedFlashmodes = params.getSupportedFlashModes();
                if(supportedFlashmodes == null || supportedFlashmodes.isEmpty() || supportedFlashmodes.size() == 1 && supportedFlashmodes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
                    CameraFeatures.hasFlash = false;
                } else {
                    CameraFeatures.hasFlash = true;
                }
            }

            //check if front/back camera exists
            Camera.CameraInfo ci = new Camera.CameraInfo();
            for(int i =0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i,ci);
                if(ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    CameraFeatures.rearcam = true;
                }
                if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    CameraFeatures.frontcam = true;
                }

            }


        //double check
        //sometimes the above isnt enough and sets false while front camera is present
        if(!frontcam) {
            frontcam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        }

        //check if shutter sound can be disabled
        //??????
        // In fact, in many places (most of Europe, Japan, parts of US, etc.) it's illegal to disable the shutter sound
        /*
        try {
            CameraFeatures.canDisableShutterSound = ci.canDisableShutterSound;
        }catch(NoSuchFieldError ex) {
            CameraFeatures.canDisableShutterSound = false;
        }
        */

            //check if camera supports autofocus

            if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
                CameraFeatures.hasAutofocus = true;
            }

            //check other camera Features
            if(params.isSmoothZoomSupported()) {
                CameraFeatures.hasSmoothZoom = true;
            }
            if(params.isZoomSupported()) {
                CameraFeatures.hasZoom = true;
            }

        }

    /**
     *
     * @return CharSequence[] of all flash modes supported by camera<br>
     *     or null if flash is not supported
     */
    public static CharSequence[] getSupportedFlashoptions() {

        List<String> flashmodes = new ArrayList<>();

        if(params.getSupportedFlashModes() != null) {
            for (String mode : params.getSupportedFlashModes()) {

                switch (mode) {
                    case Camera.Parameters.FLASH_MODE_AUTO:
                        flashmodes.add("flash auto");
                        break;
                    case Camera.Parameters.FLASH_MODE_OFF:
                        flashmodes.add("flash off");
                        break;
                    case Camera.Parameters.FLASH_MODE_ON:
                        flashmodes.add("flash on");
                        break;
                    case Camera.Parameters.FLASH_MODE_TORCH:
                        flashmodes.add("flashlight");
                        break;
                }

            }

            CharSequence finalmodes[] = new CharSequence[flashmodes.size()];

            for (int i = 0; i < finalmodes.length; i++) {
                finalmodes[i] = flashmodes.get(i);
            }

            return finalmodes;
        }
        return null;

    }

    /**
     *
     * @return CharSequence[] of all camera supported effects<br>
     *     or null if camera dosent support effects
     */
    public static CharSequence[] getSupportedCameraEffects() {

        List<String> effects = new ArrayList<>();

        if(params.getSupportedColorEffects() != null) {
            for (String effect : params.getSupportedColorEffects()) {

                switch (effect) {
                    case Camera.Parameters.EFFECT_AQUA:
                        effects.add("aqua");
                        break;
                    case Camera.Parameters.EFFECT_BLACKBOARD:
                        effects.add("blackboard");
                        break;
                    case Camera.Parameters.EFFECT_MONO:
                        effects.add("mono");
                        break;
                    case Camera.Parameters.EFFECT_NEGATIVE:
                        effects.add("negative");
                        break;
                    case Camera.Parameters.EFFECT_NONE:
                        effects.add("none");
                        break;
                    case Camera.Parameters.EFFECT_POSTERIZE:
                        effects.add("posterize");
                        break;
                    case Camera.Parameters.EFFECT_SEPIA:
                        effects.add("sepia");
                        break;
                    case Camera.Parameters.EFFECT_SOLARIZE:
                        effects.add("solarize");
                        break;
                    case Camera.Parameters.EFFECT_WHITEBOARD:
                        effects.add("whiteboard");
                        break;
                }

            }

            CharSequence finaleffects[] = new CharSequence[effects.size()];

            for (int i = 0; i < finaleffects.length; i++) {
                finaleffects[i] = effects.get(i);
            }

            return finaleffects;
        }
        return null;

    }

    /**
     *
     * @return CharSequence[] of all the scene modes the camera supports<br>
     *     or null if camera dont support scene modes
     */
    public static CharSequence[] getSupportedCameraScenes() {

        List<String> scenes = new ArrayList<>();

        if(params.getSupportedSceneModes() != null) {
            for (String scene : params.getSupportedSceneModes()) {

                switch (scene) {
                    case Camera.Parameters.SCENE_MODE_ACTION:
                        scenes.add("action");
                        break;
                    case Camera.Parameters.SCENE_MODE_AUTO:
                        scenes.add("auto");
                        break;
                    case Camera.Parameters.SCENE_MODE_BEACH:
                        scenes.add("beach");
                        break;
                    case Camera.Parameters.SCENE_MODE_CANDLELIGHT:
                        scenes.add("candlelight");
                        break;
                    case Camera.Parameters.SCENE_MODE_FIREWORKS:
                        scenes.add("fireworks");
                        break;
                    case Camera.Parameters.SCENE_MODE_HDR:
                        scenes.add("HDR");
                        break;
                    case Camera.Parameters.SCENE_MODE_LANDSCAPE:
                        scenes.add("landscape");
                        break;
                    case Camera.Parameters.SCENE_MODE_NIGHT:
                        scenes.add("night");
                        break;
                    case Camera.Parameters.SCENE_MODE_NIGHT_PORTRAIT:
                        scenes.add("night portrait");
                        break;
                    case Camera.Parameters.SCENE_MODE_PARTY:
                        scenes.add("party");
                        break;
                    case Camera.Parameters.SCENE_MODE_PORTRAIT:
                        scenes.add("portrait");
                        break;
                    case Camera.Parameters.SCENE_MODE_SNOW:
                        scenes.add("snow");
                        break;
                    case Camera.Parameters.SCENE_MODE_SPORTS:
                        scenes.add("sports");
                        break;
                    case Camera.Parameters.SCENE_MODE_STEADYPHOTO:
                        scenes.add("steady photo");
                        break;
                    case Camera.Parameters.SCENE_MODE_SUNSET:
                        scenes.add("sunset");
                        break;
                    case Camera.Parameters.SCENE_MODE_THEATRE:
                        scenes.add("theatre");
                        break;
                }

            }


            CharSequence finalScnenes[] = new CharSequence[scenes.size()];

            for (int i = 0; i < finalScnenes.length; i++) {
                finalScnenes[i] = scenes.get(i);
            }

            return finalScnenes;
        }
        return null;

    }


    /**
     *
     * @return CharSequence[] of all the focus modes supported by camera<br>
     * or null if no focus modes supported
     */
    public static CharSequence[] getSupportedFocusModes() {


        List<String> focusmodes = new ArrayList<>();

        if(params.getSupportedFocusModes() != null) {

            for (String mode : params.getSupportedFocusModes()) {

                switch (mode) {
                    case Camera.Parameters.FOCUS_MODE_AUTO:
                        focusmodes.add("focus auto");
                        break;
                    case Camera.Parameters.FOCUS_MODE_EDOF:
                        focusmodes.add("Extended depth of field");
                        break;
                    case Camera.Parameters.FOCUS_MODE_FIXED:
                        focusmodes.add("fixed focus");
                        break;
                    case Camera.Parameters.FOCUS_MODE_INFINITY:
                        focusmodes.add("infinity focus");
                        break;
                    case Camera.Parameters.FOCUS_MODE_MACRO:
                        focusmodes.add("macro(close-up) focus");
                        break;
                }

            }

            CharSequence finalmodes[] = new CharSequence[focusmodes.size()];

            for (int i = 0; i < finalmodes.length; i++) {
                finalmodes[i] = focusmodes.get(i);
            }

            return finalmodes;

        }

        return null;

    }

    /**
     *
     * @return the cameras supported resolutions/megapixels <br>
     *     in the format of 1920x1080. or null
     */
    public static CharSequence[] getSupportedPictureSizes() {
        if(params != null) {
            List<Camera.Size> picturesizes = params.getSupportedPictureSizes();
            CharSequence[] finalpicturesizes = new CharSequence[picturesizes.size()];

            for(int i =0; i < finalpicturesizes.length; i++) {
                finalpicturesizes[i] = picturesizes.get(i).width + "x"+picturesizes.get(i).height;
            }
            return finalpicturesizes;
        }
        return null;

    }


    /**
     *
     * @return CharSequence[] of the whitebalances supported by camera<br>
     *     or null if none is supported
     */
    public static CharSequence[] getSupportedWhiteBalances() {
        List<String> whitemodes = new ArrayList<>();

        if(params.getSupportedWhiteBalance() != null) {
            for (String mode : params.getSupportedWhiteBalance()) {

                switch (mode) {
                    case Camera.Parameters.WHITE_BALANCE_AUTO:
                        whitemodes.add("auto");
                        break;
                    case Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT:
                        whitemodes.add("cloudy day");
                        break;
                    case Camera.Parameters.WHITE_BALANCE_DAYLIGHT:
                        whitemodes.add("daylight");
                        break;
                    case Camera.Parameters.WHITE_BALANCE_FLUORESCENT:
                        whitemodes.add("fluorescent");
                        break;
                    case Camera.Parameters.WHITE_BALANCE_SHADE:
                        whitemodes.add("shade");
                        break;
                    case Camera.Parameters.WHITE_BALANCE_TWILIGHT:
                        whitemodes.add("twilight");
                        break;
                    case Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT:
                        whitemodes.add("warm fluorescent");
                        break;
                }

            }

            CharSequence finalmodes[] = new CharSequence[whitemodes.size()];

            for (int i = 0; i < finalmodes.length; i++) {
                finalmodes[i] = whitemodes.get(i);
            }

            return finalmodes;
        }
        return null;

    }


    public static String getMegaPixels(int width,int height) {
        String result = "";

        float megapixel = (width * height) / 1000000f;

        if(megapixel < 0.6) {
            megapixel = 0.5f;
            return result = "0.6M";
        }

        result = String.valueOf(Math.round(megapixel))+"MP";

        return result;
    }


}
