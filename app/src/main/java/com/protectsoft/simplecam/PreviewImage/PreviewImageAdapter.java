package com.protectsoft.simplecam.PreviewImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.protectsoft.simplecam.R;
import com.protectsoft.simplecam.editimage.ColorFilters;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 */
public class PreviewImageAdapter extends ArrayAdapter<PreviewImageModel> {

    private String layout;
    private int cols = 1;

    public PreviewImageAdapter(Context context,ArrayList<PreviewImageModel> models) {
        super(context, 0, models);
        this.layout = "previewimage_gridviewlist";
        this.cols = 4;
    }

    public PreviewImageAdapter(Context context,ArrayList<PreviewImageModel> models,String layout,int cols) {
        super(context, 0, models);
        this.layout = layout;
        this.cols = cols;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final PreviewImageModel model = getItem(position);

        if(convertView == null) {
            int layoutId = getContext().getResources().getIdentifier(layout,"layout",getContext().getPackageName());
            convertView = LayoutInflater.from(getContext()).inflate(layoutId,parent,false);
        }

        final ImageView img = (ImageView)convertView.findViewById(R.id.imageviewitem);

        int size = getCalculatedPicSize(cols);

        Picasso.with(getContext())
                .load(model.getFile())
                .resize(size,size)
                .centerCrop()
                .into(img);

        try {
            if(PreviewImageGridview.choosenFiles != null && !PreviewImageGridview.choosenFiles.isEmpty()) {
                if (PreviewImageGridview.choosenFiles.contains(model.getFile())) {
                    Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                    ColorFilters.doColorFilterRedDark(img, bitmap);
                }
            }
        } catch (NullPointerException npe) {

        }


        return convertView;
    }


    public static int getCalculatedPicSize(int col){
        switch (col){
            case 1:
                return 470;
            case 2:
                return 270;
            case 3:
                return 170;
            case 4:
                return 120;
            default:
                return 100;
        }
    }



}
