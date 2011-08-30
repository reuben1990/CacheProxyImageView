package cacheproxyimageview;

import http4android.Http4android;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CacheProxyImageView extends ImageView
{
	private final int LOADIMGSUCCESSFUL = 0;
	private final int LOADIMGFAILED = 1;
	private Handler handler;
	
	public CacheProxyImageView(Context context) 
	{
		super(context);
	}
	
	
	public CacheProxyImageView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}
	
	
	public CacheProxyImageView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}
	
	private void initHandler()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message m)
			{
				switch(m.what)
				{
				case LOADIMGSUCCESSFUL:
					Drawable drawable = (Drawable) m.obj;
					setImageDrawable(drawable);
					break;
				case LOADIMGFAILED:
					int res = (Integer) m.obj;
					setImageResource(res);
					break;
				}
			}
		};
	}
	
	public void setDrawableFromUrl(String pUrl, Map<String, StateDrawable>  pMap, int pLoadingImgRes, int pLoadFailedImgRes)
	{
		initHandler();
		
		synchronized(pMap)
		{
			StateDrawable stateDrawable = (StateDrawable) pMap.get(pUrl);
			
			if(stateDrawable == null)//Picture has not saved yet, and has no any other threads are downloading it too.
			{
				setImageResource(pLoadingImgRes);
				stateDrawable = new StateDrawable();
				pMap.put(pUrl, stateDrawable);
				new Thread(new loadImgRunnable(pUrl, stateDrawable, pLoadFailedImgRes, pMap)).start();
			}
			else if(stateDrawable.getDrawable() == null)//Picture has not saved yet, but there is a thread is downloading it.
			{
				setImageResource(pLoadingImgRes);
				new Thread(new waitImgRunnable(stateDrawable, pLoadFailedImgRes)).start();
			}
			else//Picture has saved.
			{
				setImageDrawable(stateDrawable.getDrawable());
			}
		}
	}
	
	private void sendSuccessfulMsg(Drawable pDrawable)
	{	
		Message message = handler.obtainMessage(LOADIMGSUCCESSFUL, pDrawable);
		handler.sendMessage(message);
	}
	
	private void sendFailedMsg(int pLoadFailedImgRes)
	{
		Message message = handler.obtainMessage(LOADIMGFAILED, (Integer) pLoadFailedImgRes);
		handler.sendMessage(message);
	}
	
	class loadImgRunnable implements Runnable
	{

		private String url;
		private int loadFailedImgRes;
		private StateDrawable stateDrawable;
		private Map<String, StateDrawable> map;
		
		loadImgRunnable(String pUrl, StateDrawable pStateDrawable, int pLoadFailedImgRes, Map<String, StateDrawable>  pMap)
		{
			url = pUrl;
			loadFailedImgRes = pLoadFailedImgRes;
			stateDrawable = pStateDrawable;
			map = pMap;
		}
		
		@Override
		public void run()
		{
			try 
			{
				Drawable tmpDrawable = Http4android.loadImgFromUrl(url);
				synchronized(map)
				{
					stateDrawable.setDrawable(tmpDrawable);
				}
				sendSuccessfulMsg(tmpDrawable);
			} 
			catch (MalformedURLException e) 
			{
				e.printStackTrace();
				synchronized(map)
				{
					map.remove(url);
				}
				sendFailedMsg(loadFailedImgRes);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				synchronized(map)
				{
					map.remove(url);
				}
				sendFailedMsg(loadFailedImgRes);
			}
		}
	}
	
	class waitImgRunnable implements Runnable
	{
		private StateDrawable stateDrawable;
		private int loadFailedImgRes;
		
		public waitImgRunnable(StateDrawable pStateDrawable, int pLoadFailedImgRes)
		{
			stateDrawable = pStateDrawable;
			loadFailedImgRes = pLoadFailedImgRes;
		}
		
		@Override
		public void run() 
		{
			Drawable tmpDrawable;
			for(int i = 0; i < 10; ++ i)
			{
				try 
				{
					Thread.sleep(2000);
					tmpDrawable = stateDrawable.getDrawable();
					if(tmpDrawable != null)
					{
						sendSuccessfulMsg(tmpDrawable);
						return;
					}
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			sendFailedMsg(loadFailedImgRes);
		}
	}
	
}
