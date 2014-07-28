package com.example.blurview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.widget.ScrollView;
public class BlurView extends ScrollView 
{
	private BitmapFactory.Options bgOptions;
	private int STRART_PONT=0;
	private float THRESHOLD=0.4f;
	private Bitmap bgBitmap;
	private Allocation input,output;
	private boolean ischange;
	
	 private RenderScript rs;
	 private ScriptIntrinsicBlur script;
	
	public BlurView(Context context)
	{
		super(context);
		init(context);
		
	}

	public BlurView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context)
	{
		bgOptions=new BitmapFactory.Options();
		bgOptions.inSampleSize=0;
		this.rs = RenderScript.create(getContext());
	    this.script = ScriptIntrinsicBlur.create(this.rs, Element.U8_4(this.rs));
		setStartingPoint(context);
		setBackgroundResource(R.drawable.rainy);
	}
	
	 public void setBackgroundResource(int id)
	 {
	    super.setBackgroundResource(id);
	    
	    if(bgBitmap!=null)
	    {
	    	bgBitmap.recycle();
	    	bgBitmap=null;
	    }
	    bgBitmap=BitmapFactory.decodeResource(getResources(), id);
	    Bitmap newbitmap = Bitmap.createBitmap(bgBitmap, 0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
    	bgBitmap = Bitmap.createBitmap(newbitmap, 0, 0, newbitmap.getWidth(), newbitmap.getHeight());
 
	    this.input = Allocation.createFromBitmap(this.rs, bgBitmap);
	    this.output = Allocation.createTyped(this.rs, this.input.getType());
	    newbitmap.recycle();
	    setBackgroundDrawable(new BitmapDrawable(getResources(), bgBitmap));
	    
	 }
	
	private void setStartingPoint(Context context)
	{
		this.STRART_PONT=context.getResources().getDisplayMetrics().heightPixels / 4;
	}
	
	@SuppressWarnings("deprecation")
	public void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
	    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
	    
	    float value=0.1f;
	    value=(float)(paramInt2-this.STRART_PONT)/getHeight();
	    if(value>0.0f && value<=THRESHOLD)
	    {	
	    		ischange=true;
	    		doChangeImage(10.0f*value);
	    		setBackgroundDrawable(new BitmapDrawable(getResources(), this.bgBitmap));
	    }
	    else if(paramInt2<STRART_PONT && ischange)
    	{
    		ischange=false;
    		setBackgroundResource(R.drawable.rainy);
    	}
	}
	
	private void doChangeImage(float value)
	{
		if((value>0.0f))
		{
			this.script.setRadius(value);
			this.script.setInput(this.input);
		    this.script.forEach(this.output);
		    this.output.copyTo(this.bgBitmap);
		}
	}
}
