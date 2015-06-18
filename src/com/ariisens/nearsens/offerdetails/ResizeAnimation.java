package com.ariisens.nearsens.offerdetails;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
	
	/***		
		ResizeAnimation resizeAnimation = new ResizeAnimation(img, getDisplayHeight());
		resizeAnimation.setDuration(600);
		img.startAnimation(resizeAnimation);
	 */
	final int startWidth;
	final int targetWidth;
	View view;
 
	public ResizeAnimation(View view, int targetWidth) {
		this.view = view;
		this.targetWidth = targetWidth;
		startWidth = view.getWidth();
	}
 
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int newWidth = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
		view.getLayoutParams().width = newWidth;
		view.requestLayout();
	}
 
	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}
 
	@Override
	public boolean willChangeBounds() {
		return true;
	}
}