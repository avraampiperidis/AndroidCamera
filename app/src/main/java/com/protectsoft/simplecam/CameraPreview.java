package com.protectsoft.simplecam;

import android.content.Context;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "SurfaceView";

    private SurfaceHolder mholder;
    private Camera mcamera;
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;
    private float mDist;



    public CameraPreview(Context context,Camera cam) {
        super(context);

        mcamera = cam;

        mSupportedPreviewSizes = mcamera.getParameters().getSupportedPreviewSizes();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mholder = getHolder();
        mholder.addCallback(this);
        //deprecate setting but required for versions prior to 3.0
        mholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
                mcamera.setPreviewDisplay(holder);
                mcamera.startPreview();

        } catch (IOException ex) {
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // preview get changed or rotate
        if(mholder.getSurface() == null) {
            return;
        }



        try {

            mcamera.stopPreview();

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
            Camera.Parameters params = CameraFeatures.params;
            //List<Camera.Size> sizes = params.getSupportedPreviewSizes();
           // Camera.Size size = sizes.get(0);
            params.setPreviewSize(mPreviewSize.width,mPreviewSize.height);

            mcamera.setParameters(params);

            mcamera.setPreviewDisplay(mholder);
            mcamera.startPreview();

        } catch (Exception ex) {
        }


    }


    public Camera.Size getOptiomalpreviewSize(List<Camera.Size> sizes,int w,int h) {

        final double aspect_tolerance = 0.1;
        double targetRatio=(double) h/w;

        if(sizes == null) {
            return null;
        }

        Camera.Size optiomalsize = null;
        double minDiff = Double.MIN_VALUE;

        int targetHeight = h;

        for(Camera.Size size : sizes) {
            double ratio = (double)size.width/ size.height;
            if(Math.abs(ratio = targetRatio) > aspect_tolerance) {
                continue;
            }
            if(Math.abs(size.height - targetHeight) < minDiff) {
                optiomalsize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if(optiomalsize == null) {
            minDiff = Double.MAX_VALUE;
            for(Camera.Size size : sizes) {
                if(Math.abs(size.height - targetHeight) < minDiff) {
                    optiomalsize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optiomalsize;

    }


    @Override
    protected void onMeasure(int widthMeasure,int heightMeasure) {
        final int width = resolveSize(getSuggestedMinimumWidth(),widthMeasure);
        final int height = resolveSize(getSuggestedMinimumHeight(),heightMeasure);
        //setMeasuredDimension(width,height);

        if(mSupportedPreviewSizes != null) {
            mPreviewSize = getOptiomalpreviewSize(mSupportedPreviewSizes,width,height);
        }

        float ratio;
        if(mPreviewSize.height >= mPreviewSize.width)
            ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
        else
            ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;

        // One of these methods should be used, second method squishes preview slightly
        setMeasuredDimension(width, (int) (width * ratio));
        //setMeasuredDimension((int) (width * ratio), height);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(CameraFeatures.isTakingpicture == false) {
            if(MainActivity.isRecording == false) {
                if (CameraFeatures.hasZoom) {
                    Camera.Parameters params = CameraFeatures.params;
                    int action = event.getAction();

                    if (event.getPointerCount() > 1) {
                        // handle multi-touch events
                        if (action == MotionEvent.ACTION_POINTER_DOWN) {
                            mDist = getFingerSpacing(event);
                        } else if (action == MotionEvent.ACTION_MOVE && params.isZoomSupported()) {
                            //// TODO check throwable
                            try {
                                mcamera.cancelAutoFocus();
                                handleZoom(event, params);
                            } catch (Throwable th) {
                            }
                        }
                    } else {
                        // handle single touch events
                        if (action == MotionEvent.ACTION_UP) {
                            handleFocus(event, params);
                        }
                    }
                }
            }
        }
        return true;
    }


    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mcamera.setParameters(params);
    }

    //not used
    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            try {
                mcamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {
                        // currently set to auto-focus on single touch
                    }
                });
            } catch (Throwable th) {

            }
        }
    }

    /**
     *
     * @param event
     * @return determine the space between first two fingers
     */
    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mcamera != null) {
            this.getHolder().removeCallback(this);
            mcamera.stopPreview();
            mcamera.release();
        }
    }




}
