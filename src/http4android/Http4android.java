package http4android;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;

public class Http4android 
{
	
	public static Drawable loadImgFromUrl(String pUrl) throws IOException
	{
		URL url = new URL(pUrl);;
		InputStream inputstream = (InputStream) url.getContent();
		Drawable drawable = Drawable.createFromStream(inputstream, "src");
		return drawable;
	}

}
