package ren.blueair;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class UpdateActivity extends Activity{
	DbAdapter adAdapter;//数据库的一个适配器
	List<Menu> list;
    @Override
    protected void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.update);
    	adAdapter = new DbAdapter(this);
    }
    
    String getMenuList(String url){
    	return HttpUtil.doPost(url, null);
    }
    
    class MyTask extends AsyncTask<String,Integer,String>{

		@Override
		protected String doInBackground(String... arg0) {
			// arg0[0]返回的是url，然后给getMenuList最后给onPostExecute
			return getMenuList(arg0[0]);
		}
    	@Override
    	protected void onPostExecute(String result){
    		super.onPostExecute(result);
    		Toast.makeText(getApplicationContext(),result,1).show();
    	    Gson gson = new Gson();
    	    Type type = new TypeToken<List<Menu>>(){}.getType();
    	    list = gson.fromJson(result, type);
    	    //循环的调用数据库
    	    if(list!=null){
    	    	adAdapter.deleteMenu();
    	    	for(Menu m:list){
    	    	  int id = m.getId();
    	    	  String name = m.getName();
    	    	  int tid = m.getTid();
    	    	  int price = m.getPrice();
    	    	  String description = m.getDesc();
    	    	  adAdapter.addMenu(id, name, tid, price, description);
    	    	  
    	    
    	    	}
    	    }
    	}
    }
    
    public void update(View v){
    	//networking
    	//AsyncTask
    	//SQLite
    	String url="http://10.0.2.2:8888/WL_Servlet/MenuServlet";
    	new MyTask().execute(url);
    	
    }
}
