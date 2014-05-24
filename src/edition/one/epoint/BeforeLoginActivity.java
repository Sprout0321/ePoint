package edition.one.epoint;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;

import edition.one.epoint.method.NetCheck;
import edition.one.epoint.method.ProcessLogin;
import edition.one.epoint.method.ProcessSign;


public class BeforeLoginActivity extends Activity implements AsyncTaskCompleteListener {
	private TextView mTxtResult;
	private Dialog	mDlgLogin,
					mDlgSign;
	private Button	mDlgLoginBtnOk,
					mDlgLoginBtnCancel,
					mDlgSignBtnOk,
					mDlgSignBtnCancel;
	private ImageButton mBtnLogin,
						mBtnSign,
						mBtnFbLogin;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private List<String> permissions = new ArrayList<String>();
	String	sLoginId,
			sLoginPs,
			sSignId,
			sSignPs,
			sFbEmail,
			sFbId;
	EditText	edtUserName,
				edtPassword;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beforelogin);
        setupViewComponent();
        
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        
//        permissions.add("email");
//        permissions.add("public_profile");
//        
//        Session.OpenRequest openRequest = new Session.OpenRequest(this).setPermissions(permissions).setCallback(statusCallback);
        
        Session session = Session.getActiveSession();
        
        if (session == null) {														System.out.println("if (session == null)");
            if (savedInstanceState != null) {										System.out.println("if (session == null) ====> if (savedInstanceState != null)");
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {													System.out.println("if (session == null) ====> if (session == null)");
                session = new Session(this);
            }
            
            Session.setActiveSession(session);										System.out.println("Session.setActiveSession(session);");
            
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {		System.out.println("if (session == null) ====>  if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED))");
//                session.openForRead(openRequest);
            }
        }
    }
    
    @Override
    public void onStart() {															System.out.println("onStart()");
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {															System.out.println("onStop()");
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {	System.out.println("onActivityResult(int requestCode, int resultCode, Intent data)");
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {							System.out.println("onSaveInstanceState(Bundle outState)");
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);
    }
    
	private void setupViewComponent() {
		mBtnLogin = (ImageButton) findViewById(R.id.btnLogin);
		mBtnSign = (ImageButton) findViewById(R.id.btnSignup);
		mBtnFbLogin = (ImageButton) findViewById(R.id.btnFbLogin);

		mTxtResult = (TextView) findViewById(R.id.txtResult);
		mTxtResult.setText("版本: 1.0.Develope");
		
		mBtnLogin.setOnClickListener(btnLoginOnClkLis);
		mBtnSign.setOnClickListener(btnSignOnClkLis);
		mBtnFbLogin.setOnClickListener(btnFbLoginOnClkLis);
		
		mTxtResult.setOnClickListener(txtResultOnClkLis);
	}
	
	private Button.OnClickListener btnLoginOnClkLis = new Button.OnClickListener() {
		public void onClick(View v) {
			mDlgLogin = new Dialog(BeforeLoginActivity.this, R.style.cust_dialog);
			mDlgLogin.setTitle("登入");
			mDlgLogin.setCancelable(false);
			mDlgLogin.setContentView(R.layout.dlg_login);

			mDlgLoginBtnOk = (Button) mDlgLogin.findViewById(R.id.btnOK);
			mDlgLoginBtnCancel = (Button) mDlgLogin.findViewById(R.id.btnCancel);
			mDlgLoginBtnOk.setOnClickListener(mDlgLoginBtnOkOnClkLis);
			mDlgLoginBtnCancel.setOnClickListener(mDlgLoginBtnCancelOnClkLis);
			mDlgLogin.show();
		}
	};
		private Button.OnClickListener mDlgLoginBtnOkOnClkLis = new Button.OnClickListener() {
			public void onClick(View v) {
				edtUserName = (EditText) mDlgLogin.findViewById(R.id.edtUserName);
				edtPassword = (EditText) mDlgLogin.findViewById(R.id.edtPassword);
				sLoginId = edtUserName.getText().toString();
				sLoginPs = edtPassword.getText().toString();
				Log.d("EditText login email", sLoginId);
				Log.d("EditText login password", sLoginPs);
				
				if ((sLoginId.matches("")) && (sLoginPs.matches(""))) {
					AlertDialogInfo("警告", "請輸入帳號與密碼");
				} else if (sLoginId.matches("")) {
					AlertDialogInfo("警告", "請輸入帳號");
				} else if (sLoginPs.matches("")) {
					AlertDialogInfo("警告", "請輸入密碼");
				} else {
//					NetAsync("Login");
					new NetCheck(BeforeLoginActivity.this).execute("Login");
					CcDialog(mDlgLogin);
				}
			}
		};
		private Button.OnClickListener mDlgLoginBtnCancelOnClkLis = new Button.OnClickListener() {
			public void onClick(View v) {
				mDlgLogin.cancel();
			}
		};
	private Button.OnClickListener btnSignOnClkLis = new Button.OnClickListener() {
		public void onClick(View v) {

			mDlgSign = new Dialog(BeforeLoginActivity.this, R.style.cust_dialog);
			mDlgSign.setTitle("註冊帳號");

			mDlgSign.setCancelable(false);
			mDlgSign.setContentView(R.layout.dlg_sign);

			mDlgSignBtnOk = (Button) mDlgSign.findViewById(R.id.btnOK);
			mDlgSignBtnCancel = (Button) mDlgSign.findViewById(R.id.btnCancel);
			mDlgSignBtnOk.setOnClickListener(mDlgSignBtnOkOnClkLis);
			mDlgSignBtnCancel.setOnClickListener(mDlgSignBtnCancelOnClkLis);
			mDlgSign.show();
		}
	};
		private Button.OnClickListener mDlgSignBtnOkOnClkLis = new Button.OnClickListener() {
			public void onClick(View v) {
				edtUserName = (EditText) mDlgSign.findViewById(R.id.edtUserName);
				edtPassword = (EditText) mDlgSign.findViewById(R.id.edtPassword);
				sSignId = edtUserName.getText().toString();
				sSignPs = edtPassword.getText().toString();
				Log.d("EditText sign email", sSignId);
				Log.d("EditText sign password", sSignPs);
				
				if ((sSignId.matches("")) && (sSignPs.matches(""))) {
					AlertDialogInfo("警告", "請輸入帳號與密碼");
				} else if (sSignId.matches("")) {
					AlertDialogInfo("警告", "請輸入帳號");
				} else if (sSignPs.matches("")) {
					AlertDialogInfo("警告", "請輸入密碼");
				} else {
					if ( sSignId.length() > 3 && sSignPs.length() > 3){
//	                    NetAsync("Sign");
	                    new NetCheck(BeforeLoginActivity.this).execute("Sign");
	    				CcDialog(mDlgSign);
	                    
					}
					else
					{
						AlertDialogInfo("警告", "帳號 和 密碼 至少要大於4個字母");
					}				
				}
			}
		};
		private Button.OnClickListener mDlgSignBtnCancelOnClkLis = new Button.OnClickListener() {
			public void onClick(View v) {
				mDlgSign.cancel();
			}
		};
    private Button.OnClickListener btnFbLoginOnClkLis = new Button.OnClickListener() {
		public void onClick(View v) {
//			NetAsync("Fb");
			new NetCheck(BeforeLoginActivity.this).execute("Fb");
		}
	};
		private Button.OnClickListener txtResultOnClkLis = new Button.OnClickListener() {
			public void onClick(View v) {
	            ChangeView(AfterLoginActivity.class);
	            finish();
			}
		};
    
    private void UseToast(String message) {
		Toast toast = Toast.makeText(BeforeLoginActivity.this, message,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
		toast.show();
	}
    
    private void AlertDialogInfo(String title, String Message) {
		AlertDialog.Builder aDlgWarn = new AlertDialog.Builder(BeforeLoginActivity.this);
		aDlgWarn.setTitle(title);
		aDlgWarn.setMessage(Message);
		aDlgWarn.setPositiveButton("確認", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
			}
		});
		aDlgWarn.show();
	}
    
    private void CcDialog(final Dialog dlg) {
    	dlg.cancel();
    }
    
    private void ChangeView(Class<?> cls) {
		Intent it = new Intent();
		it.setClass(BeforeLoginActivity.this, cls);
		startActivity(it);
	}
    
    public void SaveQrInfo(String cusindex, String customerid) {
		SharedPreferences sharedPreferences = getSharedPreferences("MyIdPs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("consindex", cusindex);
		editor.putString("customerid", customerid);
		editor.commit();
		System.out.println(" \n" + "\ncusindex 是 " + cusindex + ",\ncustomerid 是 " + customerid + ",\n在 BeforeLoginActivity.class SaveQrInfo() 之中");
		UseToast("considex & customerid save success !!!");
	}    

//	public void NetAsync(String str) {
//		
//		new NetCheck(this).execute(str);
//	}
	
	private void onFacebookLogin() {
		// TODO Auto-generated method stub
		Session session = Session.getActiveSession();
        
        permissions.add("email");
        permissions.add("public_profile");

        Session.OpenRequest openRequest = new Session.OpenRequest(BeforeLoginActivity.this).setPermissions(permissions).setCallback(statusCallback);
        
        if (!session.isOpened() && !session.isClosed()) {							System.out.println("if (!session.isOpened() && !session.isClosed())");
			session.openForRead(openRequest);
        } else {																	System.out.println("else if (session.isClosed())");
    		Session.openActiveSession(BeforeLoginActivity.this, true, permissions, statusCallback);
        }
	}
	
	
	@Override
	public void onNeChTaskComplete(JSONObject check) {
		// TODO Auto-generated method stub
		System.out.println("從BeforeLoginActivity.java ==> onNeChTaskComplete() 印出");
		try {
			if (check.getString("method").matches("Login")) {				System.out.println("NetCheck程序回傳JSON物件 物件中 method = Login 表示 由Login呼叫NetCheck");
				if (check.getString("return").matches("true")) {			System.out.println("網路已連線 執行ProcessLoging.java");
					new ProcessLogin(this).execute(sLoginId, sLoginPs, "Login");
				} else {
					UseToast("網路未連線 登入失敗");							System.out.println("網路未連線 登入失敗");
				}
			} else if (check.getString("method").matches("Sign")) {			System.out.println("NetCheck程序回傳JSON物件 物件中 method = Sign 表示 由Sign呼叫NetCheck");
				if (check.getString("return").matches("true")) {			System.out.println("網路已連線 執行ProcessSign.java");
					new ProcessSign(this).execute(sSignId, sSignPs);
				} else {
					UseToast("網路未連線 註冊失敗");							System.out.println("網路未連線 註冊失敗");
				}
			} else if (check.getString("method").matches("Fb")) {			System.out.println("NetCheck程序回傳JSON物件 物件中 method = Fb 表示 由Sign呼叫NetCheck");
				if (check.getString("return").matches("true")) {			System.out.println("網路已連線 執行 onFacebookLogin();");
					onFacebookLogin();
				} else {
					UseToast("網路未連線 Facebook登入失敗");					System.out.println("網路未連線 Facebook登入失敗");
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPrLoTaskComplete(JSONObject json) {
		// TODO Auto-generated method stub
		System.out.println("從BeforeLoginActivity.java ==> onPrLoTaskComplete() 印出");
		try {
			if (json.getString("state").matches("YES")) {						System.out.println("登入程序回傳JSON物件 物件中 state = YES 登入成功");
				String cusindex = json.getString("cusindex");
				String customerid = json.getString("customerid");
				String qrString = "{\"OS\":\"Android\",\"cusindex\":\"" + cusindex + "\",\"customerid\":\"" + customerid + "\"}";
				System.out.println("qrString = " + qrString + " 從BeforeLoginActivity.java ==> onPrLoComplete() 印出");
				UseToast("登入成功");
				SaveQrInfo(cusindex, customerid);
				ChangeView(AfterLoginActivity.class);
				finish();
			} else {															System.out.println("登入程序回傳JSON物件 物件中 state = NO 登入失敗");
				if (json.getString("method").matches("Login")) {				System.out.println("登入程序回傳JSON物件 物件中 method = Login");
					UseToast("登入失敗");
				} else {														System.out.println("登入程序回傳JSON物件 物件中 method = FbLogin");
					new ProcessSign(this).execute(sFbEmail, sFbId);				System.out.println("繼續執行 new ProcessSign(this).execute(sFbEmail, sFbId);");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onPrSiTaskComplete(JSONObject json) {
		// TODO Auto-generated method stub
		System.out.println("從BeforeLoginActivity.java ==> onPrSiTaskComplete() 印出");
		try {	
			if (json.getString("customerid").matches("same email")) {			System.out.println("註冊程序回傳JSON物件 物件中 customerid = \"same email\" 註冊失敗");
				UseToast("此email已註冊過 註冊失敗");
			} else {															System.out.println("註冊程序回傳JSON物件 物件中 customerid != \"same email\" 註冊成功");
				String cusindex = json.getString("cusindex");
				String customerid = json.getString("customerid");
				String qrString = "{\"OS\":\"Android\",\"cusindex\":\"" + cusindex + "\",\"customerid\":\"" + customerid + "\"}";
				System.out.println("qrString = " + qrString + " 從BeforeLoginActivity.java ==> onPrSiComplete() 印出");
				UseToast("註冊成功 登入");
				SaveQrInfo(cusindex, customerid);
				ChangeView(AfterLoginActivity.class);
				finish();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class SessionStatusCallback implements Session.StatusCallback {
        @SuppressWarnings("deprecation")
		@Override
        public void call(Session session, SessionState state, Exception exception) {	System.out.println("call(Session session, SessionState state, Exception exception)");
	        if (session.isOpened()) {													System.out.println("if (session.isOpened())");
	            //make request to the /me API
	            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
	            	@Override
	                public void onCompleted(GraphUser user, Response response) {
	                    if (user != null) {
	                        // Display the parsed user info
	                    	sFbEmail = user.getProperty("email").toString();			Log.e("email", sFbEmail.toString());
	                    	sFbId = user.getId();										Log.e("id", sFbId.toString());
	                    	new ProcessLogin(BeforeLoginActivity.this).execute(sFbEmail, sFbId, "FbLogin");
//	                    	SaveQrInfo(email.toString(), id);
//	                    	ChangeView(AfterLoginActivity.class);											System.out.println("ChangeView(AfterLoginActivity.class);");
//	                		finish();																		System.out.println("finish()");
	                    }
	            	}
	            });
	        }
        }
    }

}
