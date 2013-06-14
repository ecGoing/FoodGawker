package com.negative.foodgawker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

public class Scraper extends AsyncTask<Integer, Void, String[]>{
	private GridView gridview;
	private Activity mainActivity;
	public HashMap<Integer, FoodSquare> foodSquareMap;
	public int mapSize;
	private int index = 0;
	
	public Scraper(Activity mainActivity, GridView gridview, HashMap<Integer, FoodSquare> map){
		this.mainActivity = mainActivity;
		this.gridview = gridview;
		foodSquareMap = map;
	}
	
    @SuppressWarnings("null")
	protected String[] doInBackground(Integer... pageNumber) {
    	String[] imageUrls = null;
        Document doc = null;
		try {
        	Log.d("LRI", "PAGE: " + Integer.toString(pageNumber[0]));
			doc = Jsoup.connect("http://foodgawker.com/page/" + pageNumber[0] + "/").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    Elements classElements = doc.select("div[class=flipwrapper]");
	    FoodSquare foodSquare = new FoodSquare();
	    mapSize = foodSquareMap.size();
	    for (Element el : classElements){
	    	foodSquare = new FoodSquare(el);
	    	if (foodSquare != null)
	    		foodSquareMap.put(mapSize + index++, foodSquare);
	    }
	    imageUrls = new String[index];
	    for (int i = 0; i < index; i++){
	    	imageUrls[i] = foodSquareMap.get(mapSize + i).url;
	    }
        return imageUrls;
    } 

    protected void onPostExecute(String[] imageUrls) {
    	if (this.mainActivity == null){
    		ImageAdapter imageAdapter = ((ImageAdapter)this.gridview.getAdapter());
    		String[] mergedUrls = new String[imageAdapter.getCount() + imageUrls.length];
    		System.arraycopy(imageAdapter.getImageUrls(), 0, mergedUrls, 0, imageAdapter.getImageUrls().length);
    		System.arraycopy(imageUrls, 0, mergedUrls, imageAdapter.getImageUrls().length, imageUrls.length);
    		imageAdapter.imageUrls = mergedUrls;
			Log.d("LRI", Integer.toString(imageAdapter.getCount()));
    		imageAdapter.notifyDataSetChanged();
    		return;
    	}
    	this.gridview.setAdapter(new ImageAdapter(this.mainActivity, imageUrls));
    }
 }