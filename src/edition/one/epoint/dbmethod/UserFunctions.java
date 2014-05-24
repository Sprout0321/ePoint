package edition.one.epoint.dbmethod;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/
import org.json.JSONObject;

import android.content.Context;

// 這個class有	"登入 loginUser()",
//				"更改密碼 chgPass()",
//				"重設密碼 forPass()",
//				"註冊 registerUser()",
//				"登出 logoutUser()" 五個個方法！！！
public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
    private static String loginURL = "http://54.199.238.145:8080/api/GetCustomer";
    private static String signURL = "http://54.199.238.145:8080/api/CreateCustomer";
    private static String chgpassURL = "http://54.199.238.145:8080/api/chgCustomer";
    private static String resetURL = "http://54.199.238.145:8080/api/resetCustomer";


    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String email, String password){
        // Building JSONObject
    	JSONObject body = new JSONObject();
		try {
			body.put("email", email);
			body.put("password", password);
		} catch (Exception ex) {
		}
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, body);
        return json;
    }
    
    /**
     * Function to Sign
     **/
    public JSONObject SignUser(String email, String password){
        // Building JSONObject
    	JSONObject body = new JSONObject();
		try {
			body.put("email", email);
			body.put("password", password);
		} catch (Exception ex) {
		}
        JSONObject json = jsonParser.getJSONFromUrl(signURL, body);
        return json;
    }

    /**
     * Function to change password
     **/
    public JSONObject chgPass(String newpas, String email){
    	JSONObject body = new JSONObject();
		try {
			body.put("email", email);
			body.put("newpas", newpas);
		} catch (Exception ex) {
		}
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, body);
        return json;
    }

    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String resetpassword){
    	JSONObject body = new JSONObject();
		try {
			body.put("resetpassword", resetpassword);
		} catch (Exception ex) {
		}
        JSONObject json = jsonParser.getJSONFromUrl(resetURL, body);
        return json;
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}
