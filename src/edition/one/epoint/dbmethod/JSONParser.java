package edition.one.epoint.dbmethod;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


//	這個class 是 透過  httpPost.setEntity() 方法
//	傳送 JSONObject資料 到 伺服器 並取得 回傳 InputStream資料,
//	傳送的 JSONObject資料 內容不同 回傳的 InputStream資料 也會不同,
//	(因此可處理 登出 登入 註冊 等不同狀況～～)
// 	再透過 BufferedReader 分析 InputStream資料 將資料轉成 String,
//	最後將 String 再包裝成 JSONObject資料 回傳值給呼叫 getJSONFromUrl() 的class！！！

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {
    	
    }
    
    public JSONObject getJSONFromUrl(String url, JSONObject body) {
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Authorization", "Basic " + "ZWExMzVkZGIzZGZhNTY0NDMzMGRmYTEwN2FmZjgxNjE=");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(body.toString()));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //while (line = reader.readLine()) == null
            	is.close();
            	json = sb.toString();
            	Log.e("JSON", json);
            	
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result: " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data: " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}
