package com.protectsoft.simplecam.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.protectsoft.simplecam.R;

import java.util.ArrayList;

/**
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_drawer_list_item, null);
        }

        ImageView imageViewIcon = (ImageView)convertView.findViewById(R.id.icon);
        imageViewIcon.setImageResource(navDrawerItems.get(position).getIcon());

        int icon = navDrawerItems.get(position).getChooseicon();
        if(icon != 0) {
            ImageView chooseIcon = (ImageView) convertView.findViewById(R.id.checkicon);
            chooseIcon.setImageResource(icon);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        TextView txtsubtutle = (TextView)convertView.findViewById(R.id.subtitle);
        txtsubtutle.setText(navDrawerItems.get(position).getSubtitle());

        convertView.setAlpha(0f);
        convertView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null)
                .start();

        return convertView;
    }


}
