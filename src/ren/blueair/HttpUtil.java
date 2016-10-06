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
	
    // ����URL����post���ɿͻ��˴������ݸ�������
	
	public static String doPost(String url,List<NameValuePair> list){
		HttpPost post = new HttpPost(url);
		//http��ʵ��
		HttpEntity entity = null;
		if(list != null){
			try{
				//��ȡʵ���е��û��� ����
				entity = new UrlEncodedFormEntity(list,"utf-8");
				
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
			//��ʵ�����Post�����з��͸�������
			post.setEntity(entity);
		}
		
		HttpClient client = new DefaultHttpClient();
		try{
			//HTTP��Ӧ���ɷ������ڽ��պͽ���������֮�󷵻ط��͸��ͻ��˵ı��ġ�
			HttpResponse response = client.execute(post);
			//��200����ɹ� ��303�ض��� ��400������� 
			//��401δ��Ȩ ��403��ֹ���� ��404�ļ�δ�ҵ� ��500����������
			if(response.getStatusLine().getStatusCode()==200){
				String result = EntityUtils.toString(response.getEntity());
				//save SharedPre...
				//��ֹ�������ת��
				result = new String(result.getBytes("utf-8"),"utf-8");
				return result;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
}
