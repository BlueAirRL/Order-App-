package ren.blueair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
    // 利用URL经过post是由客户端传递数据给服务器
	
	public static String doPost(String url,List<NameValuePair> list){
		HttpPost post = new HttpPost(url);
		//http的实体
		HttpEntity entity = null;
		if(list != null){
			try{
				//获取实体中的用户名 密码
				entity = new UrlEncodedFormEntity(list,"utf-8");
				
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
			//将实体放入Post请求中发送给服务器
			post.setEntity(entity);
		}
		
		HttpClient client = new DefaultHttpClient();
		try{
			//HTTP响应是由服务器在接收和解释请求报文之后返回发送给客户端的报文。
			HttpResponse response = client.execute(post);
			//口200请求成功 口303重定向 口400请求错误 
			//口401未授权 口403禁止访问 口404文件未找到 口500服务器错误
			if(response.getStatusLine().getStatusCode()==200){
				String result = EntityUtils.toString(response.getEntity());
				//save SharedPre...
				//防止乱码进行转码
				result = new String(result.getBytes("utf-8"),"utf-8");
				return result;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
}
