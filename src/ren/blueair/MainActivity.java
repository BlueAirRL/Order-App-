package ren.blueair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity{
    GridView gv;
    Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gv = (GridView)findViewById(R.id.gridview);
		gv.setAdapter(new MyAdapter());
		
		gv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch(position){
				case 0:
					intent = new Intent(MainActivity.this,TableActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(MainActivity.this,OrderActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(MainActivity.this,UnionActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(MainActivity.this,ChangeActivity.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(MainActivity.this,PayActivity.class);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(MainActivity.this,UpdateActivity.class);
					startActivity(intent);
					break;
					default:
						Toast.makeText(getApplicationContext(),position+"",1).show();
				}
				
			}
			
			
		});
	}
	
	
	class MyAdapter extends BaseAdapter{
        //有多少张图片需要绑定
		int imageRes[] = {
			R.drawable.chatai,
			R.drawable.diancai,
			R.drawable.bingtai,
			R.drawable.zhuantai,
			R.drawable.jietai,
			R.drawable.shezhi,
			R.drawable.zhuxiao
		};
		@Override
		public int getCount() {
			
			return imageRes.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		//此方法循环执行根据getCount中图片的数目
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv;
			if(convertView==null){
				iv = new ImageView(getApplicationContext());
			}else
			{
				iv = (ImageView) convertView;
			}
			
			iv.setLayoutParams(new GridView.LayoutParams(90,90));
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iv.setPadding(8,8,8,8);
			
			
			iv.setImageResource(imageRes[position]);
			return iv;
		}
		
	}
	
	
	
}
