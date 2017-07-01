package com.protectsoft.simplecam.PreviewImage;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.isseiaoki.simplecropview.CropImageView;
import com.protectsoft.simplecam.Constants;
import com.protectsoft.simplecam.R;
import com.protectsoft.simplecam.Utils.BitmapUtils;
import com.protectsoft.simplecam.Utils.MediaFileUtils;
import com.protectsoft.simplecam.bucket.BucketFiles;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @see //https://android-arsenal.com/details/1/2366
 */
public class CropActivity extends Activity {

    private File currentFile;
    private CropImageView cropImageView;
    private Bitmap currentBitmap;

    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.previewimage_croplayout);

        Intent intent = getIntent();
        currentFile = (File)intent.getExtras().get("file");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        currentBitmap = BitmapUtils.decodeSampledBitmapFromFile(currentFile,1000,850);

        cropImageView = (CropImageView)findViewById(R.id.cropImageView);
        cropImageView.setHandleColor(getResources().getColor(R.color.material_deep_teal_200));

        cropImageView.setImageBitmap(currentBitmap);

    }


    @Override
    public void onBackPressed() {

        if(isChanged) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CropActivity.this);
            builder.setInverseBackgroundForced(true);
            builder.setTitle("Save croped picture ?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            crop(null);

                        }
                    }
            );

            builder.setNegativeButton("No", new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            finish();
                        }
                    }

            );

            builder.show();

        } else {

            finish();

        }

    }

    public void crop(View view) {

        final ImageButton imageButton = (ImageButton)findViewById(R.id.crop_button);

        imageButton.setEnabled(false);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setEnabled(true);
                    }
                });
            }
        },1500);

        Bitmap bitmap = cropImageView.getCroppedBitmap();

        File file = MediaFileUtils.getOutputMediaFile(1);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.jpegquality, stream);
        byte[] bitmapdata = stream.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getAbsolutePath()}, null, null);

        if (!BucketFiles.isFileExists(file)) {
            BucketFiles.addPictureFile(file);
        }

        finish();

    }


    public void rotateLeft(View view) {

        isChanged = true;

        final ImageButton imageButton = (ImageButton)findViewById(R.id.rotateleftbutton);
        imageButton.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setEnabled(true);
                    }
                });
            }
        },1500);

        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_270D);
    }


    public void rotateRight(View view) {

        isChanged = true;

        final ImageButton imageButton = (ImageButton)findViewById(R.id.rotaterightbutton);
        imageButton.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setEnabled(true);
                    }
                });
            }
        },1500);

        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
    }




}
