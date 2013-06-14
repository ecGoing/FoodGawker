package com.negative.foodgawker;

import android.graphics.Bitmap;
import android.util.LruCache;

class ActivityBridge {
    private ActivityBridge() {}

    static ActivityBridge obj = null;
    static public ActivityBridge instance() {
         if (obj == null) obj = new ActivityBridge();
         return obj;
    }

    public LruCache<String, Bitmap> cache;
}
