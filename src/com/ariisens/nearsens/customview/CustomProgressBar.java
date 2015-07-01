package com.ariisens.nearsens.customview;

import com.ariisens.nearsens.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

public class CustomProgressBar extends View {

	float dim;
	int shape;
	Paint paint1,paint2,paint3,paint4;
	float toDo = 3;
	float dim2;
	private float center_x;
	private float center_y;
	private float width;
	private float height;
	private float radius;
	final RectF oval = new RectF();
	 Path path = new Path();
	 int restart = 6;
	int disegna = 6;
	public static final int CIRCLE = 0;
	public static final int SQUARE = 1;
	Paint pText;
	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomBar,
                0, 0
        );
		
		try {
            dim = a.getDimension(R.styleable.CustomBar_dim, 20f);
            shape = a.getInteger(R.styleable.CustomBar_shape, 0);
        } finally {
            a.recycle();
        }
		pText = new Paint(); 
		pText.setColor(Color.WHITE); 
		pText.setStyle(Style.FILL); 
		pText.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
		pText.setTextAlign(Align.CENTER);
		pText.setColor(Color.BLACK); 
		pText.setTextSize(dim*2); 
		
		dim2 = dim;
		paint1 = new Paint();
		paint1.setColor(Color.BLACK);
		paint1.setAntiAlias(true);
		paint1.setStrokeWidth(dim*1.5f);	
		paint1.setStyle(Paint.Style.STROKE);
		
		paint2 = new Paint();
		paint2.setColor(Color.BLACK);
		paint2.setAntiAlias(true);
		paint2.setStrokeWidth(dim*1.8f);	
		paint2.setStyle(Paint.Style.STROKE);
		
		paint3 = new Paint();
		paint3.setColor(Color.BLACK);
		paint3.setAntiAlias(true);
		paint3.setStrokeWidth(dim*2.1f);	
		paint3.setStyle(Paint.Style.STROKE);
		
		paint4 = new Paint();
		paint4.setColor(Color.BLACK);
		paint4.setAntiAlias(true);
		paint4.setStrokeWidth(dim*2.5f);	
		paint4.setStyle(Paint.Style.STROKE);
		
	/*	paint1.setShadowLayer(3f, 1.0f, 1.0f,Color.BLACK);
		paint2.setShadowLayer(3, 2.0f, 2.0f,Color.BLACK);
		paint3.setShadowLayer(3f, 2.0f, 2.0f,Color.BLACK);
		paint4.setShadowLayer(4f, 2.0f, 2.0f,Color.BLACK);*/
		

		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		

		// draw circle
		switch(shape){
			case CIRCLE:
				

		

				
				
				 width = (float)getWidth();

				 height = (float)getHeight();
			

					path.addCircle(width/2, 

							height/2, radius, 

							Path.Direction.CW);

				if (width > height){

					radius = height/6;

				}else{

					radius = width/12;

				}

				center_x = width/2;

				center_y = height/2;

			

				oval.set(center_x - radius, 

						center_y - radius, 

						center_x + radius, 

						center_y + radius);
				
				for(int i=6; i<=370;i=i+6){

					
						canvas.drawArc(oval, i, 3, false, paint1);
						
				}
				if(disegna>=370)
					disegna=6;
				
				if(disegna%restart == 0){
					canvas.drawArc(oval, disegna-12, 3, false, paint2);
					canvas.drawArc(oval, disegna-6, 3, false, paint3);
					canvas.drawArc(oval, disegna, 3, false, paint4);
					canvas.drawArc(oval, disegna+6, 3, false, paint3);
					canvas.drawArc(oval, disegna+12, 3, false, paint2);
				}
				disegna+=6;
				restart+=6;
				if(restart%370<=0)
					restart=6;
				/*int xPos = (canvas.getWidth() / 2);
				 int yPos = (int) ((canvas.getHeight() / 2) - ((pText.descent() + pText.ascent()) / 2)) ; 
				 
				canvas.drawText("Loading", xPos, yPos, pText); */
				
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				break;
			case SQUARE:
				canvas.drawRect(0, 0, dim, dim, paint2);
				break;

		}
		invalidate();

	}
	

}
