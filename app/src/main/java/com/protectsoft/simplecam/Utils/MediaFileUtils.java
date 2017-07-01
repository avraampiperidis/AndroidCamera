package com.protectsoft.simplecam.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.CamcorderProfile;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import com.protectsoft.simplecam.Constants;
import com.protectsoft.simplecam.bucket.BucketFiles;
import com.protectsoft.simplecam.jni.bitmapoperations.JniBitmapHolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class MediaFileUtils {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static int retries = 0;


    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * initialize create folders/file
     */
    public static void initializeFolder() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"camshare");
        //location for shared picture folder between apps

        //create storage dir if not exists
        if(!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                //LogToDatabaseService.appendLog(new Exception("camshare " + " failed to create directory check storage free space"));
                return ;
            }
        }

    }

    /**
     * <p>
     *     returns the path + filename ! isn't creating it!
     * </p>
     * @param type file will be image or video/
     * @return     the File name with the path.
     *
     */
    public static File getOutputMediaFile(int type) {
        //check sdcard is mounted
        // using  Environment.getExternalStorageState() before doing this

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"camshare");
        //location for shared picture folder between apps

        //create storage dir if not exists
        if(!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                //LogToDatabaseService.appendLog(new Exception("camshare " + " failed to create directory check storage free space"));
                return null;
            }
        }

        //create media filename
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediafile;

        if(type == MEDIA_TYPE_IMAGE) {
             mediafile = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timestamp+".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediafile = new File(mediaStorageDir.getPath()+File.separator+"VID_"+timestamp+".mp4");
        } else {
            return null;
        }

        return mediafile;

    }



    /**
     *
     * @return home file path
     */
    public static final File getHomeFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"camshare");
        return mediaStorageDir;
    }



    /**
     *  two possible Throwables!  OutOfMemoryError Exception(when dalvik runs out of memory because of big bitmaps data) ->subclass of Error<br>
     *         and Unsatisfiedlinkerror ->subclass of Error if failed to load cpp library <-this comes from JniBitmapHolder
     * @param rotation the current rotation of the camera ,for the picture taken from camera to be rotated properly
     * @param data the byte[] array image captured from camera
     * @param picturefile the path+imagename to be saved
     * @param con application context
     * @throws Throwable  to inform calling method by catching it and taking different action(edit image with java instead cpp)<br>
     *
     */
    public  static void jnirotateImageAndWriteToFile(int rotation,byte[] data,File picturefile,Context con) throws Throwable {

        try {
        JniBitmapHolder jnibitmapholder = new JniBitmapHolder();
            try {

            if(rotation == 90) {

                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);

                jnibitmapholder.storeBimap(btm);
                jnibitmapholder.rotateBitmapCw90();

                Bitmap btm2 = jnibitmapholder.getBitmapAndFree();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                btm2.compress(Bitmap.CompressFormat.JPEG, Constants.jpegquality, stream);
                byte[] bitmapdata = stream.toByteArray();

                FileOutputStream fos = null;
                fos = new FileOutputStream(picturefile);
                fos.write(bitmapdata);
                fos.close();

                btm2.recycle();
                btm.recycle();

                btm2 = null;
                btm = null;

            } else if(rotation == 0) {

                FileOutputStream fos = null;

                fos = new FileOutputStream(picturefile);
                fos.write(data);
                fos.close();

            } else if(rotation == 180) {

                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);

                jnibitmapholder.storeBimap(btm);
                jnibitmapholder.rotateBitmap180();

                Bitmap btm2 = jnibitmapholder.getBitmapAndFree();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                btm2.compress(Bitmap.CompressFormat.JPEG, Constants.jpegquality, stream);
                byte[] bitmapdata = stream.toByteArray();

                FileOutputStream fos = null;

                fos = new FileOutputStream(picturefile);
                fos.write(bitmapdata);
                fos.close();

                btm2.recycle();
                btm.recycle();

                btm2 = null;
                btm = null;

            } else if(rotation == 270) {

                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);

                jnibitmapholder.storeBimap(btm);
                jnibitmapholder.rotateBitmapCcw90();

                Bitmap btm2 = jnibitmapholder.getBitmapAndFree();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                btm2.compress(Bitmap.CompressFormat.JPEG, Constants.jpegquality, stream);
                byte[] bitmapdata = stream.toByteArray();

                FileOutputStream fos = null;
                fos = new FileOutputStream(picturefile);
                fos.write(bitmapdata);
                fos.close();

                btm2.recycle();
                btm.recycle();

                btm2 = null;
                btm = null;

            }

            //android bug
            //refresh saved picture to internall storage otherwise it will not be accessible from usb
            MediaScannerConnection.scanFile(con, new String[]{picturefile.getAbsolutePath()}, null, null);
            Constants.lastpicturefiletaken = picturefile;
                BucketFiles.addPictureFile(picturefile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
          //throwable catched becase cpp/so library failed to load!
        } catch (Throwable ex) {
            //in that case throw new throwable to inform calling method ny catching it and taking different action(edit image with java instead cpp)
            // or OutOfMemoryError
            throw new Throwable();
        }

    }

    /**<p>
     *  usealy this method gets called if the above throw Exception
     * </p>
     *
     * @param rotation the current rotation of the camera ,for the picture taken from camera to be rotated properly
     * @param data the byte[] array image captured from camera
     * @param picturefile the path+imagename to be saved
     * @param con application context
     *  @see #jnirotateImageAndWriteToFile
     */
    public  static void rotateImageAndWriteToFile(int rotation,byte[] data,File picturefile,Context con) {
        try {

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
            btm = Bitmap.createBitmap(btm, 0, 0, btm.getWidth(), btm.getHeight(), matrix, true);

            btm = btm.copy(Bitmap.Config.RGB_565, true);
            FileOutputStream fos = null;
            fos = new FileOutputStream(picturefile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            btm.compress(Bitmap.CompressFormat.JPEG, Constants.jpegquality, stream);
            byte[] bytearray = stream.toByteArray();

            fos.write(bytearray);
            fos.close();
            btm.recycle();
            btm = null;

            //android bug
            //refresh saved picture to internall storage otherwise it will not be accessible from usb
            MediaScannerConnection.scanFile(con,new String[]{picturefile.getAbsolutePath()},null,null);
            Constants.lastpicturefiletaken = picturefile;
            BucketFiles.addPictureFile(picturefile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError oom) {
            System.gc();
        }
    }

    /**
     * <p>
     *     this method havent tested!
     * </p>
     * @param dp the imageview dp
     * @param density phones screen density
     * @return the right pixel value for the imageview<br>
     *
     */
    public static int dpToPx(int dp,float density) {
        return Math.round((float)dp * density);
    }



    /**
     *
     * @param data the data as a byte[] array to be tested if there is enough free space in the folder to be saved<br>
     *             compares the given data plus 1mb for safety
     * @return true false
     */
    public static boolean isFreeSpaceAvailable(byte[] data) {
        //count bytes
        StatFs stat = new StatFs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();

        System.out.println("|---->data: "+data.length);
        System.out.println("|---->bytesavailable:"+bytesAvailable);

        //if free space is greater the data plus 1mb for safety
        if(bytesAvailable > (data.length+10000000)) {
            return true;
        }
        return false;
    }



    public static long getBytesSdAvailable() {
        StatFs stat = new StatFs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        return bytesAvailable;
    }



    /**
     *
     * @param profileQuality the camera quality
     * @return the birate of camera record video
     * used in recording video for calculating free space
     */
    public static int getBitrate(int profileQuality) {

        CamcorderProfile profile = CamcorderProfile.get(profileQuality);
        int bitrate = profile.videoBitRate + profile.audioBitRate;

        return bitrate;
    }






}
