package com.negative.foodgawker;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private boolean loading = true;
    public int pageNumber = 1;
    private HashMap<Integer, FoodSquare> foodSquareMap = new HashMap<Integer, FoodSquare>();
    private LruCache<String, Bitmap> memoryCache;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Add splash screen
        //http://blog.iangclifton.com/2011/01/01/android-splash-screens-done-right/
        final GridView gridview = (GridView)findViewById(R.id.gridview);
        //gridview.addFooterView(yourFooterView, null, true);
        new Scraper(this, gridview, foodSquareMap).execute(pageNumber++);
        
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/6th of the available memory for this memory cache.
        // Only storing the expanded image in here.
        final int cacheSize = maxMemory / 6;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        
        gridview.setPadding(10, 10, 0, 10);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent myIntent = new Intent(MainActivity.this, ExpandActivity.class);
            	FoodSquare foodSquare = foodSquareMap.get(position);
            	Drawable drawable = ((ImageAdapter)gridview.getAdapter()).drawableMap.get(foodSquare.url);
            	addBitmapToMemoryCache("Bitmap", drawableToBitmap(drawable));
            	ActivityBridge.instance().cache = memoryCache;
            	myIntent.putExtra("FoodSquare", foodSquare);
            	MainActivity.this.startActivity(myIntent);
            }
        });
        
        gridview.setOnScrollListener(
        	new OnScrollListener() {//Make infinite scrolling smoother with a load footer * seems difficult (non existant?) with gridview
		        @Override
		        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		        	if (totalItemCount < 1)
		        		return;
		        	int renderedCount = ((ImageAdapter)gridview.getAdapter()).getDrawableCount();
	                if (loading && totalItemCount > renderedCount){
	                    loading = false;
	                }
		            if (!loading && totalItemCount == renderedCount &&
		            		gridview.getChildAt(gridview.getChildCount() - 6).getBottom() <= gridview.getHeight())
		            {
		            	Log.d("LRI", "NEXT PAGE");
		                new Scraper(null, gridview, foodSquareMap).execute(pageNumber++);
		                loading = true;
		            }
		        }
		        
		        @Override
		        public void onScrollStateChanged(AbsListView view, int scrollState) {
		            // TODO Auto-generated method stub
		        }
		    }
    	);
    }
    
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap); 
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        memoryCache.put(key, bitmap);
	    }
	}
	
	public Bitmap getBitmapFromMemCache(String key) {
	    return memoryCache.get(key);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
