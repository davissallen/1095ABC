/*  Created by Davis Allen
 * 
 *  Class: ${CLASS_NAME}.java
 *  Project: JacobApp
 */


package com.davis.jacobapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    RelativeLayout[] videoPreviews;

    private final String LOG_TAG = getClass().getSimpleName() + "findme";
    List<ParseObject> parseObjects;
    Context context;
    Activity activity;

    public ImageAdapter(List<ParseObject> parseObjects, Context context, Activity activity) {
        if (parseObjects != null) {
            videoPreviews = new RelativeLayout[parseObjects.size()];
            this.parseObjects = parseObjects;
            this.context = context;
            this.activity = activity;
        }
    }

    @Override
    public int getCount() {
        return parseObjects.size();
    }

    @Override
    public ParseObject getItem(int position) {
        return parseObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create new imageview for each image
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        RelativeLayout relativeLayout;

        if (convertView == null) {

            /* if the image is new, start fresh */

            // new RelativeLayout
            relativeLayout = new RelativeLayout(activity.getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(lp);

            // new ImageView
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0, 25, 0, 25);
            if (parseObjects.get(position).getParseFile("thumbnailImage") != null) {
                ParseFile thumbnailImage = parseObjects.get(position).getParseFile("thumbnailImage");
                Picasso.with(context).load(thumbnailImage.getUrl()).into(imageView);
            } else {
                imageView.setBackgroundColor(Color.LTGRAY);
            }

            // new title
            TextView textView = new TextView(activity.getApplicationContext());
            textView.setText(parseObjects.get(position).getString("title"));
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundColor(Color.argb(230, 255, 255, 255));
            textView.setTextSize(16);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setPadding(0,50,0,50);
            textView.setClickable(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);

            RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            // add image and title to relativeLayout
            relativeLayout.addView(imageView);
            relativeLayout.addView(textView, tvlp);

            videoPreviews[position] = relativeLayout;
        } else {
            // recycled, it's already been saved in array as a RelativeLayout
            // get relativeLayout from array
            relativeLayout = videoPreviews[position];
        }

        // show the list after all are loaded
        if (position == (parseObjects.size() - 1)) {
            RelativeLayout layoutSplashScreenMaster = (RelativeLayout) activity.findViewById(R.id.layout_splash_screen_master);
            layoutSplashScreenMaster.setVisibility(RelativeLayout.INVISIBLE);

            RelativeLayout layoutVideoMaster = (RelativeLayout) activity.findViewById(R.id.layout_videos_master);
            layoutVideoMaster.setVisibility(RelativeLayout.VISIBLE);
        }

        return relativeLayout;
    }

}
