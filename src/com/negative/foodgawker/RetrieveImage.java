package com.negative.foodgawker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class RetrieveImage extends AsyncTask<String, Void, Bitmap>{
    private Exception exception;
	private ImageView imageView = null;
	
    public RetrieveImage(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap bm = null;
        String url = urls[0];
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
       } catch (IOException e) {
           Log.e("", "Error getting bitmap", e);
       }
       return bm;
    } 

    protected void onPostExecute(Bitmap bitmap) {
    	this.imageView.setImageBitmap(bitmap);
    }
 }
