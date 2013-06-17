package com.negative.foodgawker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ExpandActivity extends Activity {
	private FoodSquare foodSquare;
	public Drawable drawable;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
    	Intent intent = getIntent();
    	foodSquare = (FoodSquare)intent.getSerializableExtra("FoodSquare");
    	Bitmap bitmap = ActivityBridge.instance().cache.get("Bitmap");
    	Drawable drawable = new BitmapDrawable(getResources(), bitmap);
    	
    	DisplayMetrics metrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	ImageView imageView = (ImageView)findViewById(R.id.ExpandView);
    	TextView titleText = (TextView)findViewById(R.id.TitleText);
        
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
    	
        titleText.setText(foodSquare.displayName);
    	imageView.setImageDrawable(drawable);
    	ActivityBridge.instance().cache.remove("Bitmap");
    	Log.d("TEST", foodSquare.url);
    }
}
