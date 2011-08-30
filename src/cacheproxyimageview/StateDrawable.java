package cacheproxyimageview;

import android.graphics.drawable.Drawable;

public class StateDrawable 
{	
	private Drawable drawable;
	
	public StateDrawable(Drawable pDrawable)
	{
		drawable = pDrawable;
	}
	
	public StateDrawable()
	{
		//Do nothing.
	}
	
	public void setDrawable(Drawable pDrawable)
	{
		drawable = pDrawable;
	}
	
	public Drawable getDrawable()
	{
		return drawable;
	}

}
















































