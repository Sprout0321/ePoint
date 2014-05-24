package edition.one.epoint.method;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class InternetConnect {
	static String iplocation = "http://54.199.238.145:8080/api/";
	String data,
			id,
			result,
			connectUrl;
	HttpResponse response;
	HttpEntity resEntity;
	JSONObject	object,
				jsonObject,
				returnObject;
	
	public JSONObject async(final String body, final String api, final Boolean proct) {
		
		//測試是否確實從MainActivity收到body,api,proct
		System.out.println(body + api + "==>" + Boolean.toString(proct) + " 從InternetConnect.java印出");
		connectUrl = iplocation + api;
		
//		Thread thread = new Thread() {
//			public void run() {
				if (proct == true) {
					data = new String(body);
					returnObject = get(data);
				} else if (proct == false) {
					try {
						object = new JSONObject(body);
						returnObject = post(object);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
//			}
//		};
//		thread.start();
		return returnObject;
	}
	
	public JSONObject post(JSONObject body) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(connectUrl);
		post.setHeader("Authorization", "Basic " + "ZWExMzVkZGIzZGZhNTY0NDMzMGRmYTEwN2FmZjgxNjE=");
		post.setHeader("Content-Type", "application/json");
		try {
			post.setEntity(new StringEntity(body.toString()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			response = client.execute(post);
			resEntity = response.getEntity();
			result = EntityUtils.toString(resEntity);
			Log.d("resEntity", result);
			try {
				jsonObject = new JSONObject(result);
				data = jsonObject.getString("customerid");
				id = jsonObject.getString("cusindex");
				Log.d("post", data + " " + id);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;

	}

	public JSONObject get(String body) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(connectUrl + "?" + body);
		get.setHeader("Authorization", "Basic " + "ZWExMzVkZGIzZGZhNTY0NDMzMGRmYTEwN2FmZjgxNjE=");
		get.setHeader("Content-Type", "application/json");
		try {
			response = client.execute(get);
			resEntity = response.getEntity();
			result = EntityUtils.toString(resEntity);
			Log.d("ResEntity", result);
				try {
					JSONObject jsonObject2 = new JSONObject(result);
					data = jsonObject2.getString("state");
					Log.d("state", data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
}
