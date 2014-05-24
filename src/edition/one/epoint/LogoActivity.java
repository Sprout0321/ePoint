package edition.one.epoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LogoActivity extends Activity {
	public static final String DEFAULT ="N/A";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        new Thread(new Runnable(){
	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	            try {
	                Thread.sleep(1000);//這邊可以做你想預先載入的資料
	
	                SharedPreferences sharedPreferences= getSharedPreferences("MyIdPs", Context.MODE_PRIVATE);
	        		//使用 getString() 從 SharedPreferences 抓取相對的值！
	        		//getString("要抓取值的標頭", 找不到此標頭時給予的值)
	        		String getCusin = sharedPreferences.getString("consindex", DEFAULT);
	        		String getCust = sharedPreferences.getString("customerid", DEFAULT);
	        		
	        		if (getCusin.equals(DEFAULT) || getCust.equals(DEFAULT)) {
	        			startActivity(new Intent().setClass(LogoActivity.this, BeforeLoginActivity.class));
	        			finish();
	        		} else{
	        			startActivity(new Intent().setClass(LogoActivity.this, AfterLoginActivity.class));
	        			finish();
	        		}
	//                    //接下來轉跳到app的主畫面
	//                    startActivity(new Intent().setClass(LogoActivity.this, AfterLoginActivity.class));
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
            
        }).start();
    }
	
}
