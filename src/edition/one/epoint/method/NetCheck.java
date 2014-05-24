package edition.one.epoint.method;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import edition.one.epoint.AsyncTaskCompleteListener;

public class NetCheck extends AsyncTask<String, String, JSONObject> {
	private Activity activity;
	private AsyncTaskCompleteListener callback;
	private ProgressDialog pDlgNet;

	public NetCheck(Activity act) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener)act;
	}
	@Override
	protected void onPreExecute() {
		pDlgNet = new ProgressDialog(activity);
		pDlgNet.setTitle("Checking Network");
		pDlgNet.setMessage("Loading..");
		pDlgNet.setIndeterminate(false);
		pDlgNet.setCancelable(true);
		pDlgNet.show();
		super.onPreExecute();
	}
	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("args[0] 是 " + args[0] + " 從NetCheck.java ==> doInBackground() 印出");
		JSONObject check = new JSONObject();
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL url = new URL("http://www.google.com");
				HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
				urlc.setConnectTimeout(3000);
				urlc.connect();
				if (urlc.getResponseCode() == 200) {
					String netreturn = "true";
					System.out.println("netreturn 是 " + netreturn + " 從NetCheck.java ==> doInBackground() 印出");
					try {
						check.put("method", args[0]);
						check.put("return", netreturn);
					} catch (Exception ex) {
					}
					return check;
				}
				
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String netreturn = "false";
		System.out.println("netreturn 是 " + netreturn + " 從NetCheck.java ==> doInBackground() 印出");
		try {
			check.put("method", args[0]);
			check.put("return", netreturn);
		} catch (Exception ex) {
		}
		return check;
	}
	@Override
	protected void onPostExecute(JSONObject check) {
		super.onPostExecute(check); 
		pDlgNet.dismiss();
		callback.onNeChTaskComplete(check);
	}
}

