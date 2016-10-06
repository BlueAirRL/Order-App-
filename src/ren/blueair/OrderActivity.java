package ren.blueair;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity{
	Spinner tableSp;
	List<Table> tableList;
	TableAdapter tableAdapter;
	String ctime;
	int uid;
	int persons;
	EditText numEditText;
	ConfigUtil configUtil;
	DbAdapter dbAdapter;
	List<Menu> menuList;
	List<MenuTemp> menuTempList;
	ListView lv;
	MenuListAdapter menuListAdapter;
	Gson gson;
     @Override
     protected void onCreate(Bundle savedInstanceState){
    	 super.onCreate(savedInstanceState);
    	 setContentView(R.layout.order);
    	 configUtil = new ConfigUtil(this);
    	 gson = new Gson();
    	 dbAdapter = new DbAdapter(this);
    	 tableSp = (Spinner)findViewById(R.id.spinner1);
    	 
    	 numEditText = (EditText)findViewById(R.id.num_editText1);
    	 menuList = new ArrayList<Menu>();
    	 menuTempList = new ArrayList<MenuTemp>();
    	 
//    	 String strNum = numEditText.getText().toString();
//    	 persons = Integer.valueOf(strNum);
//    	 System.out.println("persons="+persons);
    	 
    	 menuListAdapter = new MenuListAdapter();
    	 Date date = new Date();
    	 SimpleDateFormat sdf = new SimpleDateFormat("yy/mm/dd HH:MM");
    	 ctime =  sdf.format(date);
    	 System.out.println("timeStr="+ctime);
    	 
    	 String userJson = configUtil.getUserJson();
    	 Type type = new TypeToken<User>(){}.getType();
    	 User u = gson.fromJson(userJson, type);
    	 uid = u.getId();
    	 System.out.println("uid="+uid);
    	 
    	 tableList = new ArrayList<Table>();
    	 tableAdapter = new TableAdapter();
    	 
    	 tableSp.setAdapter(tableAdapter);
    	 String tableUrl = "http://10.0.2.2:8888/WL_Servlet/TableServlet?flag=0";
    	 new MyTableTask().execute(tableUrl);
    	 
    	 lv = (ListView)findViewById(R.id.listView1);
    	 lv.setAdapter(menuListAdapter);
    }
     
     //下单
     public void order(View v){
    	 Order o = new Order();
    	 //获取下订单时间
    	 o.setCtime(ctime);
    	 //获取客人的id
    	 o.setUid(uid);
         //取到桌号
    	 int index = tableSp.getSelectedItemPosition();
    	 Table t = tableList.get(index);
    	 o.setTid(t.getTid());
    	 //获取就餐人数
    	 persons = Integer.parseInt(numEditText.getText().toString());
    	 o.setPersonNum(persons);
    	 //获取饭菜描述
    	 o.setDesc("desc");
    	 //将所有的信息放到Order中
    	 o.setList(menuTempList);
    	 
    	 //将数据进行解析，先获取Gson对象
    	 Gson gson = new Gson();
    	 //解析成什么type类型的
    	 Type type = new TypeToken<Order>(){}.getType();
    	 //通过toJson()方法将数据发送至服务器
    	 String json = gson.toJson(o,type);
    	 String url = "http://10.0.2.2:8888/WL_Servlet/OrderServlet";
    	 //执行异步任务
    	 new MyOrderTask().execute(url,json);
     }
     /*
      * AsyncTask定义了三种泛型类型 Params，Progress和Result。

		Params 启动任务执行的输入参数，比如HTTP请求的URL。
		Progress 后台任务执行的百分比。
		Result 后台执行任务最终返回的结果，比如String。
		使用过AsyncTask 的同学都知道一个异步加载数据最少要重写以下这两个方法：
		
		doInBackground(Params…) 后台执行，比较耗时的操作都可以放在这里。
		注意这里不能直接操作UI。此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。
		在执行过程中可以调用publicProgress(Progress…)来更新任务的进度。
		onPostExecute(Result)  相当于Handler 处理UI的方式，
		在这里面可以使用在doInBackground 得到的结果处理操作UI。
		 此方法在主线程执行，任务执行的结果作为此方法的参数返回
      * */
     //下面要将json发送到服务器端
     class MyOrderTask extends AsyncTask<String , integer, String>{
    	 List<NameValuePair> list = new ArrayList<NameValuePair>();
		@Override
		protected String doInBackground(String... params) {
			
	    	 NameValuePair p1 = new BasicNameValuePair("order_json",params[1]);
	    	 list.add(p1);
		     return HttpUtil.doPost(params[0],list);
        }
    	 
    	 @Override
    	protected void onPostExecute(String result) {
    		super.onPostExecute(result);
    		Toast.makeText(getApplicationContext(), result, 1).show();
    	}
     
     }
     
     
     //用于点完菜后的详细登记
     class MenuListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuTempList.size();
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
        //获取信息放置在listView中
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if(convertView==null){
				v = View.inflate(getApplicationContext(), R.layout.menu_item, null);
			}else {
				v = convertView;
			}
			
			MenuTemp mt = menuTempList.get(position);
			TextView idTv = (TextView)v.findViewById(R.id.id_textView1);
			TextView numTv = (TextView)v.findViewById(R.id.num_textView1);
			TextView nameTv = (TextView)v.findViewById(R.id.name_textView1);
			
			idTv.setText(position+1+"");
			numTv.setText(mt.getNum()+"");
			nameTv.setText(mt.getName());
			return v;
		}
    	 
     }
     
     //点击点菜按钮后，将点菜的信息呈现在spinner菜谱中
     
     class MenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuList.size();
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
			Menu m = menuList.get(position);
				tv.setText(m.getName());
		        return tv;
		}
    	 
     }
   
     
     //点菜
     public void diancai(View v) throws UnsupportedEncodingException{
    	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	 builder.setTitle("点菜");
    	 builder.setNegativeButton("取消", null);
    	 final View v1 = View.inflate(this, R.layout.diancai, null);
    	 builder.setView(v1);
    	 final Spinner sp = (Spinner)v1.findViewById(R.id.spinner11);
    	 final EditText numEt = (EditText)v1.findViewById(R.id.num_editText1);
    	 final EditText descEt = (EditText)v1.findViewById(R.id.desc_editText2);
    	 builder.setPositiveButton("确定", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				MenuTemp mt = new MenuTemp();
//				String name = sp.getSelectedItem().toString();
				int index = sp.getSelectedItemPosition();
				Menu m = menuList.get(index);
				int num = Integer.parseInt(numEt.getText().toString());
				String desc = descEt.getText().toString();
				
				mt.setName(m.getName());
				mt.setDesc(desc);
				mt.setNum(num);
				mt.setMid(m.getId());
				menuTempList.add(mt);
				menuListAdapter.notifyDataSetChanged();
			}
    	});
    	 
    	//将菜谱中的菜名显示在spinner中
    	 MenuAdapter ma = new MenuAdapter();
    	 sp.setAdapter(ma);
    	 
    	 menuList = dbAdapter.query();
    	 ma.notifyDataSetChanged();
    	 
    	 builder.create().show();
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
    		Type type = new TypeToken<List<Table>>(){}.getType();
    	    tableList = gson.fromJson(result, type);
    	    tableAdapter.notifyDataSetChanged();
    	}
    	 
     }
}
