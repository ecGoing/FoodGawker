package com.negative.foodgawker;

import java.io.Serializable;

import org.jsoup.nodes.Element;

import android.graphics.drawable.Drawable;

public class FoodSquare implements Serializable {
	public String url;
	public String displayName;
	public String description;
	public Drawable drawable;
	
	public FoodSquare(){ }
	
	public FoodSquare(Element el){
    	Element card = el.child(0).child(0);
    	Element picture = card.child(0);
    	Element pictureHeader = picture.child(1);
    	Element pictureFooter = picture.child(2);
    	Element image = picture.child(3);
    	String source = image.child(0).attr("src");
    	url = source.contains("png") ? null : source;
    	displayName = image.attr("title");
    	description = card.child(2).text();
	}
}
