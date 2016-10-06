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
  //��������
  String doLogin(String url){
//	  String url = "http://10.0.2.2:8888/WL_Servlet/LoginServlet";
	  //��ȡ�ͻ��˵��û���������
	  String username = usernameEt.getText().toString();
	  String password = passwordEt.getText().toString();
	  //1.apache client
	  /*
	   * android��ʹ��http������������������󣬲���Ҫ���ݲ���ʱ��
	   * ����Ҫ�õ�NameValuePair��ֵ��
	   * */
	  List<NameValuePair> list = new ArrayList<NameValuePair>();
	  NameValuePair p1 = new BasicNameValuePair("username",username);
	  NameValuePair p2 = new BasicNameValuePair("password",password);
      
	  list.add(p1);
	  list.add(p2);
	  //��ȡ�������˵�����
	  String msg = HttpUtil.doPost(url, list);
	  System.out.println(msg);
	  return msg;
  
  }
  //handler
  //�첽��Ϣ����
  //Asynctask
  /*
   * һ���첽�����ִ��һ��������¼������裺
     1.execute(Params... params)��ִ��һ���첽������Ҫ�����ڴ����е��ô˷����������첽�����ִ�С�
     2.onPreExecute()����execute(Params... params)�����ú�����ִ�У�һ��������ִ�к�̨����ǰ��UI��һЩ��ǡ�
     3.doInBackground(Params... params)����onPreExecute()��ɺ�����ִ�У�����ִ�н�Ϊ��ʱ�Ĳ������˷�����������������ͷ��ؼ���������ִ�й����п��Ե���publishProgress(Progress... values)�����½�����Ϣ��
     4.onProgressUpdate(Progress... values)���ڵ���publishProgress(Progress... values)ʱ���˷�����ִ�У�ֱ�ӽ�������Ϣ���µ�UI����ϡ�
     5.onPostExecute(Result result)������̨��������ʱ���˷������ᱻ���ã�����������Ϊ�������ݵ��˷����У�ֱ�ӽ������ʾ��UI����ϡ�
   * */
  class MyTask extends AsyncTask<String ,Integer, String>{
    @Override
	protected String doInBackground(String... params) {
		String url = params[0];
		String result = doLogin(url);
		return result;
	}
	//���Ĳ������ط��������ص���Ϣ
	@Override
	protected void onPostExecute(String result){
		super.onPostExecute(result);
		//3.��������
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		//�жϵ�¼�Ƿ�ɹ�
		if(result!=null&&result.equals("-1")){
			Toast.makeText(getApplicationContext(), "��¼ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			return;
		}else{
			//json���浽�ͻ��ˣ����ʱ��Ҫ���û�����
			//�������ú���ת��ϵͳ��������
			
			configUtil.setUserJson(result);
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}
	
  }
  public void login(View v){
	  //2.MultiThread
      String url = "http://10.0.2.2:8888/WL_Servlet/LoginServlet";
      //�����첽ִ��
	  new MyTask().execute(url);
  }
  
}
