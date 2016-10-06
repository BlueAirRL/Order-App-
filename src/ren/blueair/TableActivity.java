package ren.blueair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class TableActivity extends Activity{
	GridView gv;
	List<Table> list ;
	MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.table_layout);
    	list = new ArrayList<Table>();
    	gv = (GridView)findViewById(R.id.table_gridview);
    	myAdapter = new MyAdapter();
    	gv.setAdapter(myAdapter);
    	
    	//1.��������
    	String url = "http://10.0.2.2:8888/WL_Servlet/TableServlet";
    	new MyTask().execute(url);
    	//init networking
    }
    
    
    String doGetTabMsg(String url){
    	
    	String json = HttpUtil.doPost(url, null);
    	
		return json;
    	
    }
    //�������˷�������
    class MyTask extends AsyncTask<String,Integer,String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			//��������֮�󷵻�һ������
			return doGetTabMsg(arg0[0]);
		}
		//������ĺ����ķ���ֵ������ķ������β�
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			//�������ݳ�һ��list
			Gson gson = new Gson();
			Type type = new TypeToken<List<Table>>(){}.getType();
			list = gson.fromJson(result,type);
			//����Adapter�����Ѿ��ı��ˣ����»�ȡ����
			myAdapter.notifyDataSetChanged();
		}
    	
    }
    
    class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if(convertView==null){
				v = View.inflate(getApplicationContext(), R.layout.table_item,null);
				
			}else{
				v = convertView;
			}
			ImageView iv = (ImageView)v.findViewById(R.id.imageView1);
			TextView tv = (TextView)v.findViewById(R.id.textView1);
			Table t = list.get(position);
			int flag = t.getFlag();
			//��ʾ��λ
			if(flag==0){
				iv.setImageResource(R.drawable.kongwei);
			}else{
				iv.setImageResource(R.drawable.youren);
			}
			tv.setText(t.getTid()+" ");
			return v;
		}
    	
    }
    
}
