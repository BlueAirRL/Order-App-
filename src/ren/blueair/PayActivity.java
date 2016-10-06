package ren.blueair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.R.integer;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivity extends Activity{
   
	Spinner tableSp;
	WebView wv;
	List<Table> tableList;
	TableAdapter tableAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay1);
		
		wv = (WebView)findViewById(R.id.webView1);
		
		tableSp = (Spinner)findViewById(R.id.spinner12);
		tableList = new ArrayList<Table>();
		tableAdapter = new TableAdapter();
		tableSp.setAdapter(tableAdapter);
		String url = "http://10.0.2.2:8888/WL_Servlet/TableServlet?flag=1";
		new MyTableTask().execute(url);
		
		
	}
	
	class MyQueryTask extends AsyncTask<String, Integer, String >{

		@Override
		protected String doInBackground(String... params) {
			String json = HttpUtil.doPost(params[0],null);
			
			return json;
		}
   	 @Override
   	protected void onPostExecute(String result) {
   		// TODO Auto-generated method stub
   		super.onPostExecute(result);
   		wv.loadData(result, "text/html", "utf-8");
   	}
   	 
    }
	//1.获得空位桌号  创建异步任务
    class TableAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tableList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			TextView tv;
			if(convertView==null){
				tv = new TextView(getApplicationContext());
			}else{
				tv = (TextView)convertView;
			}
			Table t = tableList.get(position);
			tv.setText(t.getTid()+"");
			return tv;
		}
   	 
    }
    
    class MyTableTask extends AsyncTask<String, Integer, String >{

		@Override
		protected String doInBackground(String... params) {
			String json = HttpUtil.doPost(params[0],null);
			
			return json;
		}
   	 @Override
   	protected void onPostExecute(String result) {
   		// TODO Auto-generated method stub
   		super.onPostExecute(result);
   		//解析Json
   		Gson gson = new Gson();
   		Type type = new TypeToken<List<Table>>(){}.getType();
   	    tableList = gson.fromJson(result, type);
   	    tableAdapter.notifyDataSetChanged();
   	}
   	 
    }
	
	public void query(View v){
		int index = tableSp.getSelectedItemPosition();
		Table t = tableList.get(index);
		String url = "http://10.0.2.2:8888/WL_Servlet/QueryOrderServlet?tid="+t.getTid();
		new MyQueryTask().execute(url);
	}
	
	class MyPayTask extends AsyncTask<String, Integer, String >{

		@Override
		protected String doInBackground(String... params) {
			String json = HttpUtil.doPost(params[0],null);
			
			return json;
		}
   	 @Override
   	protected void onPostExecute(String result) {
   		// TODO Auto-generated method stub
   		super.onPostExecute(result);
        
   		Toast.makeText(getApplicationContext(), result, 1).show();
   	}
   	 
    }
	
    public void pay(View v){
    	int index = tableSp.getSelectedItemPosition();
		Table t = tableList.get(index);
		String url = "http://10.0.2.2:8888/WL_Servlet/PayServlet?tid="+t.getTid();
		new MyPayTask().execute(url);
	}
}
