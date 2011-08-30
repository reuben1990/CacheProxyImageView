package maccha.cacheproxyimageview.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import macca.cacheproxyimageview.sample.R;

import cacheproxyimageview.CacheProxyImageView;
import cacheproxyimageview.StateDrawable;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class Main extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Map<String, StateDrawable> map = new HashMap<String, StateDrawable>();
        List<String> list = new ArrayList<String>();
        
        for(int i = 0; i < 1000; ++ i)
        {
        	list.add("http://ww1.sinaimg.cn/large/6636f83ajw1dkmdtzv69sj.jpg");
        }
        
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new UrlImageAdapter(list, this, map));
    }
    
    class UrlImageAdapter extends BaseAdapter
    {
    	private List<String> list;
    	private Context context;
    	private Map<String, StateDrawable> map;
    	
    	UrlImageAdapter(List<String> pList, Context pContext, Map<String, StateDrawable> pMap)
    	{
    		list = pList;
    		context = pContext;
    		map = pMap;
    	}
    	
		@Override
		public int getCount()
		{
			return list.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			convertView = inflateView(context, R.layout.item);
			CacheProxyImageView img = (CacheProxyImageView) convertView.findViewById(R.id.img);
			img.setDrawableFromUrl(list.get(position), map, R.drawable.successful, R.drawable.failed);
			return convertView;
		}

		private View inflateView(Context pContext, int resource)
		{
			LayoutInflater inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(resource, null);
		}
    }
}





