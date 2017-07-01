package com.protectsoft.simplecam.editimage;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.protectsoft.simplecam.R;
import com.protectsoft.simplecam.Utils.MediaFileUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by abraham on 8/4/2017.
 */
public class PixelActivity extends AppCompatActivity {

    private ImageView image;
    private Bitmap originalBitmap;
    private Target target;

    private int circle;
    private int square;
    private int diamond;

    private ExecutorService executor = Executors.newFixedThreadPool(3);


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.pixelate_layout);

        image = (ImageView)findViewById(R.id.image);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                originalBitmap = bitmap;
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };



        Picasso.with(getApplicationContext())
                .load((File)getIntent().getExtras().get("file"))
                .resize(450,550)
                .centerInside()
                .into(target);

        SeekBar seekbarCircle = (SeekBar)findViewById(R.id.seekBarRes);
        SeekBar seekbarDiamond = (SeekBar)findViewById(R.id.seekBarSize);
        SeekBar seekbarSquare = (SeekBar)findViewById(R.id.seekBarOffSet);


        seekbarCircle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                circle = i;
                if(i >= 4){
                    List<PixelateLayer> layers = new ArrayList<PixelateLayer>();
                    layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                            .setResolution(i/2)
                            .build());
                    if(diamond > 4){
                        layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                                .setResolution(i/2)
                                .build());
                    }
                    if(square > 4){
                        layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Square)
                                .setResolution(i/2)
                                .build());
                    }

                    final PixelateLayer[] arr = new PixelateLayer[layers.size()];
                    for(int x =0; x < arr.length; x++) {
                        arr[x] = layers.get(x);
                    }

                    execute(arr);
                }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekbarDiamond.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                diamond = i;
                if(i >= 4){
                    List<PixelateLayer> layers = new ArrayList<PixelateLayer>();
                    layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                            .setResolution(i/2)
                            .build());

                    if(circle > 4) {
                        layers.add( new PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                                .setResolution(i/2)
                                .build());
                    }
                    if(square > 4){
                        layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Square)
                                .setResolution(i)
                                .build());
                    }

                    final PixelateLayer[] arr = new PixelateLayer[layers.size()];
                    for(int x =0; x < arr.length; x++) {
                        arr[x] = layers.get(x);
                    }

                    execute(arr);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbarSquare.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                square = i;
                if(i >= 4){
                    List<PixelateLayer> layers = new ArrayList<PixelateLayer>();
                    layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Square)
                            .setResolution(i)
                            .build());
                    if(circle > 4) {
                       layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                               .setResolution(i/2)
                               .build());
                    }
                    if(diamond > 4){
                        layers.add(new PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                                .setResolution(i/2)
                                .build());
                    }

                    final PixelateLayer[] arr = new PixelateLayer[layers.size()];
                    for(int x =0; x < arr.length; x++) {
                        arr[x] = layers.get(x);
                    }

                    execute(arr);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private int getWidth() {
        return MediaFileUtils.dpToPx(image.getMeasuredWidth(),getResources().getDisplayMetrics().density);
    }

    private int getHeigth(){
        return MediaFileUtils.dpToPx(image.getMeasuredHeight(),getResources().getDisplayMetrics().density);
    }


    private void execute(final PixelateLayer[] arr) {
        if(((ThreadPoolExecutor)executor).getActiveCount()  < 5 ) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = Pixelate.fromBitmap(originalBitmap,arr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(bitmap);
                        }
                    });

                }
            });
        }
    }

}
