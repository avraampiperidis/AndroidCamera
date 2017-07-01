package com.protectsoft.simplecam.PreviewImage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.protectsoft.simplecam.R;
import com.protectsoft.simplecam.bucket.BucketFiles;
import com.protectsoft.simplecam.editimage.EditImage;
import com.protectsoft.simplecam.editimage.PixelActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 */
public class PreviewImage extends Activity {

    private TouchImageView imageView;
    private File currentFile;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previewimage_layout);

        Intent intent = getIntent();
        currentFile = (File)intent.getExtras().get("file");

        imageView = (TouchImageView) findViewById(R.id.imagepreview);

        try {

            Picasso.with(getApplicationContext())
                    .load(currentFile)
                    .resize(850, 1200)
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageView);



        } catch (OutOfMemoryError oom) {
            finish();
        }

        final GestureDetector gdt = new GestureDetector(new GestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });


        bringlayoutsToFront();
    }



    @Override
    public void onResume() {
        super.onResume();

        if(currentFile.exists()) {

            Picasso.with(getApplicationContext())
                    .load(currentFile)
                    .resize(850, 1200)
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageView);

            bringlayoutsToFront();

        }


    }



    public void action_crop(View view) {

        final ImageView imageViewcrop = (ImageView)findViewById(R.id.crop_button);
        imageViewcrop.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageViewcrop.setEnabled(true);
                    }
                });
            }
        },1000);

        Intent intent = new Intent(this,CropActivity.class).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra("file",currentFile);
        startActivity(intent);

    }

    public void editpixel(View v) {
        Intent intent = new Intent(this, PixelActivity.class).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra("file",currentFile);
        startActivity(intent);

    }



    public void bringlayoutsToFront() {

        LinearLayout l2 = (LinearLayout)findViewById(R.id.optionsbuttonlayout);
        l2.bringToFront();

    }



    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                File nextPicture = BucketFiles.getNextPictureFile(currentFile);

                if(nextPicture != null && BucketFiles.isFileExists(nextPicture)) {

                    currentFile = nextPicture;

                    try {

                        imageView = (TouchImageView)findViewById(R.id.imagepreview);

                        Picasso.with(getApplicationContext())
                                .load(nextPicture)
                                .resize(850,1200)
                                .centerInside()
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(imageView);


                    } catch (OutOfMemoryError ooe) {
                        finish();
                    }
                } else {
                    //file deleted from user or something
                    BucketFiles.removeDeletedFiles();
                }

                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                File previusPicture = BucketFiles.getPreviusPictureFile(currentFile);

                if(previusPicture != null && BucketFiles.isFileExists(previusPicture)) {

                    currentFile = previusPicture;

                    try {

                        imageView = (TouchImageView)findViewById(R.id.imagepreview);

                        Picasso.with(getApplicationContext())
                                .load(previusPicture)
                                .resize(850,1200)
                                .centerInside()
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(imageView);


                    } catch (OutOfMemoryError ooe) {

                    }

                } else {
                    BucketFiles.removeDeletedFiles();
                }
                return false; // Left to right
            }

            //not used yet up and down swipe
            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }
    }



    public void share(View v) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, currentFile);
        intent.setType("image/jpeg");
        startActivity(Intent.createChooser(intent,"share image"));

    }



    public void editimage(View view) {

        final ImageView imageViewedit = (ImageView)findViewById(R.id.edit_image);
        imageViewedit.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageViewedit.setEnabled(true);
                    }
                });
            }
        },1000);

        if(currentFile != null && currentFile.exists()) {
            Intent intent = new Intent(this, EditImage.class);
            intent.putExtra("file", currentFile);
            startActivity(intent);
            finish();
        }

    }



}
