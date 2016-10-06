package ren.blueair;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	EditText usernameEt,passwordEt;
	ConfigUtil configUtil;
	
  @Override
  protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.login);
	  
	  usernameEt = (EditText)findViewById(R.id.editText1);
	  passwordEt = (EditText)findViewById(R.id.editText2);
	  configUtil = new ConfigUtil(this);
	  
  }
  //第三步：
  String doLogin(String url){
//	  String url = "http://10.0.2.2:8888/WL_Servlet/LoginServlet";
	  //获取客户端的用户名和密码
	  String username = usernameEt.getText().toString();
	  String password = passwordEt.getText().toString();
	  //1.apache client
	  /*
	   * android中使用http请求向服务器发起请求，并需要传递参数时，
	   * 经常要用到NameValuePair键值对
	   * */
	  List<NameValuePair> list = new ArrayList<NameValuePair>();
	  NameValuePair p1 = new BasicNameValuePair("username",username);
	  NameValuePair p2 = new BasicNameValuePair("password",password);
      
	  list.add(p1);
	  list.add(p2);
	  //获取服务器端的数据
	  String msg = HttpUtil.doPost(url, list);
	  System.out.println(msg);
	  return msg;
  
  }
  //handler
  //异步信息处理
  //Asynctask
  /*
   * 一个异步任务的执行一般包括以下几个步骤：
     1.execute(Params... params)，执行一个异步任务，需要我们在代码中调用此方法，触发异步任务的执行。
     2.onPreExecute()，在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记。
     3.doInBackground(Params... params)，在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。在执行过程中可以调用publishProgress(Progress... values)来更新进度信息。
     4.onProgressUpdate(Progress... values)，在调用publishProgress(Progress... values)时，此方法被执行，直接将进度信息更新到UI组件上。
     5.onPostExecute(Result result)，当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
   * */
  class MyTask extends AsyncTask<String ,Integer, String>{
    @Override
	protected String doInBackground(String... params) {
		String url = params[0];
		String result = doLogin(url);
		return result;
	}
	//第四步：返回服务器返回的信息
	@Override
	protected void onPostExecute(String result){
		super.onPostExecute(result);
		//3.保存数据
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		//判断登录是否成功
		if(result!=null&&result.equals("-1")){
			Toast.makeText(getApplicationContext(), "登录失败！", Toast.LENGTH_SHORT).show();
			return;
		}else{
			//json保存到客户端，点餐时还要用用户数据
			//保存配置后，跳转到系统的主界面
			
			configUtil.setUserJson(result);
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}
	
  }
  public void login(View v){
	  //2.MultiThread
      String url = "http://10.0.2.2:8888/WL_Servlet/LoginServlet";
      //开启异步执行
	  new MyTask().execute(url);
  }
  
}
