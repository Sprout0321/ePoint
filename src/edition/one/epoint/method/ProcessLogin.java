package edition.one.epoint.method;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import edition.one.epoint.AsyncTaskCompleteListener;
import edition.one.epoint.dbmethod.UserFunctions;

public class ProcessLogin extends AsyncTask<String, String, JSONObject> {
	private static final String TAG = "ProcessLogin.java";
	private Activity activity;
	private AsyncTaskCompleteListener callback;
	private ProgressDialog pDlgPro;

	public ProcessLogin(Activity act) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener)act;
	}
	
    //onPreExecute方法用于在执行后台任务前做一些UI操作  
    @Override  
    protected void onPreExecute() {  
        Log.i(TAG, "onPreExecute() called");
        pDlgPro = new ProgressDialog(activity);
		pDlgPro.setTitle("Contacting Servers");
		pDlgPro.setMessage("Logging in ...");
		pDlgPro.setIndeterminate(false);
		pDlgPro.setCancelable(true);
		pDlgPro.show();
        super.onPreExecute();
    }  

    //doInBackground方法内部执行后台任务,不可在此方法内修改UI
    @Override  
    protected JSONObject doInBackground(String... args) {		Log.i(TAG, "doInBackground(String... args) called");
    	String sLoginId = args[0];    							System.out.println("sLoginId = " + sLoginId + " 從ProcessLogin.java ==> doInBackground() 印出");
    	String sLoginPs = args[1];    							System.out.println("sLoginPs = " + sLoginPs + " 從ProcessLogin.java ==> doInBackground() 印出");
    	
    	UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.loginUser(sLoginId, sLoginPs);
		
		try {
			json.put("method", args[2]);
		} catch (Exception ex) {
		}
		return json;
    }  

    //onProgressUpdate方法用于更新进度信息  
    @Override  
    protected void onProgressUpdate(String... progresses) {  
        Log.i(TAG, "onProgressUpdate(Progress... progresses) called");  
    }  

    //onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
    @Override
    protected void onPostExecute(JSONObject json) {  
        Log.i(TAG, "onPostExecute(Result result) called");
        pDlgPro.dismiss();
        callback.onPrLoTaskComplete(json);
	}

    //onCancelled方法用于在取消执行中的任务时更改UI  
    @Override  
    protected void onCancelled() {  
        Log.i(TAG, "onCancelled() called");
    }  
}  