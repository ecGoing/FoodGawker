package com.negative.foodgawker;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<String> {
    private Context mContext;
    public String[] imageUrls;
    private final DrawableManager drawableManager;
    protected final HashMap<String, Drawable> drawableMap;

    public ImageAdapter(Context context, String[] imageUrls){
    	super(context, 0);
    	if (context != null)
    		this.mContext = context;
        this.imageUrls = imageUrls;
        drawableMap = new HashMap<String, Drawable>();
        drawableManager = new DrawableManager(drawableMap);
    }
    
    public String[] getImageUrls(){
    	return this.imageUrls;
    }

    public int getDrawableCount() {
    	return drawableManager.getCount();
    }
    
    public int getCount() {
        return this.imageUrls.length;
    }

    public String getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } 
        else {
            imageView = (ImageView) convertView;
            imageView.setImageDrawable(null);
        }
        //new RetrieveImage(imageView).execute(this.imageUrls[position]);
        drawableManager.fetchDrawableOnThread(this.imageUrls[position], imageView);
        return imageView;
    }
}