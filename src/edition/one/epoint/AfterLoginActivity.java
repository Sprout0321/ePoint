package edition.one.epoint;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;

import edition.one.epoint.method.TabPagerAdapter;

public class AfterLoginActivity extends FragmentActivity {

	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	ViewPager Pager;
	TabPagerAdapter TabAdapter;
	ActionBar actionBar;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_afterlogin);
		setTitle("ePoint (標題可改)");
		getActionBar().setIcon(R.drawable.ic_launcher_2);
		//ActionBar左邊增加一個返回鍵
		getActionBar().setDisplayHomeAsUpEnabled(false);
		setupViewComponent();
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (session == null) {														//System.out.println("if (session == null)");
            if (savedInstanceState != null) {										//System.out.println("if (session == null) ====> if (savedInstanceState != null)");
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {													//System.out.println("if (session == null) ====> if (session == null)");
                session = new Session(this);
            }
            Session.setActiveSession(session);										//System.out.println("Session.setActiveSession(session);");
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {		//System.out.println("if (session == null) ====>  if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED))");
//                session.openForRead(openRequest);
            }
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
//			// Respond to the action bar's Up/Home button
//			// 按下ActionBar左邊增加的返回鍵
//		    case android.R.id.home:
//		        NavUtils.navigateUpFromSameTask(this);
//		        return true;
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_logout:
			openLogout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			toggleActionBar();
		}
		return true;
	}

	private void toggleActionBar() {
		actionBar = getActionBar();

		if (actionBar != null) {
			if (actionBar.isShowing()) {
				actionBar.hide();
			} else {
				actionBar.show();
			}
		}
	}

	private void setupViewComponent() {

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); // Enable Tabs on Action Bar
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				Pager.setCurrentItem(tab.getPosition());
			}
			@Override
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}
		};
		
		TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
		Pager = (ViewPager) findViewById(R.id.pager);
		Pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar = getActionBar();
				actionBar.setSelectedNavigationItem(position);
			}
			
			public void onPageScrollStateChanged(int state) {

			}
			
			public void onPageScrolled(int position, float positionOffset, int positionOffestPixels) {

			}
		});
		Pager.setAdapter(TabAdapter);

		// Add New Tab
		actionBar.addTab(actionBar.newTab().setText("QRcode").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Get Points").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Others").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Others2").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Others3").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Others4").setTabListener(tabListener));

	}

    private void ChangeView() {
		Intent it = new Intent();
		it.setClass(AfterLoginActivity.this, BeforeLoginActivity.class);
		startActivity(it);
	}
    
	private void UseToast(String message) {
		Toast toast = Toast.makeText(AfterLoginActivity.this, message,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
		toast.show();
	}

	private void openSearch() {
		// TODO Auto-generated method stub
		UseToast("Search");
	}
	
	private void openSettings() {
		// TODO Auto-generated method stub
		UseToast("Settings");
	}
	
	private void openLogout() {
		SharedPreferences sharedPreferences = getSharedPreferences("MyIdPs", Context.MODE_PRIVATE);
		//取得到SharedPreferences.Editor物件
		SharedPreferences.Editor editor = sharedPreferences.edit();
		//清除Preference中的數值，如果用remove可以移除某鍵的值
		editor.clear();
		//最後也要提交commit
		editor.commit();
		UseToast("Clear data successfully");
		
		Session session = Session.getActiveSession();
		session.closeAndClearTokenInformation();
		
		ChangeView();
		finish();
	}
	
	private class SessionStatusCallback implements Session.StatusCallback {
        @SuppressWarnings("deprecation")
		@Override
        public void call(Session session, SessionState state, Exception exception) {	//System.out.println("call(Session session, SessionState state, Exception exception)");
	        if (session.isOpened()) {													//System.out.println("if (session.isOpened())");
	            //make request to the /me API
	            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
	            	@Override
	                public void onCompleted(GraphUser user, Response response) {
	                    if (user != null) {
	                        // Display the parsed user info
	                    }
	            	}
	            });
	        }
        }
    }
	
}
