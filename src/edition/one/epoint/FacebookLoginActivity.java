package edition.one.epoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;

public class FacebookLoginActivity extends Activity {
    private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";

    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private List<String> permissions = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {								System.out.println("onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        
        buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        
        permissions.add("email");
        permissions.add("public_profile");
        permissions.add("user_friends");
        permissions.add("user_location");
        permissions.add("user_birthday");
        permissions.add("user_likes");
        
        Session.OpenRequest openRequest = new Session.OpenRequest(this).setPermissions(permissions).setCallback(statusCallback);
        
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
                session.openForRead(openRequest);
            }
        }
        updateView();																System.out.println("line70: updateView();");
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

    private void updateView() {														System.out.println("line98: updateView() {...}");
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
//            textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
            buttonLoginLogout.setText(R.string.logout);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { 
                	onClickLogout();
                }
            });
        } else {
//            textInstructionsOrLink.setText(R.string.instructions);
            buttonLoginLogout.setText(R.string.login);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                	onClickLogin();
                }
            });
        }
    }

    private void onClickLogin() {													System.out.println("onClickLogin()");
        Session session = Session.getActiveSession();
        
        permissions.add("email");
        permissions.add("public_profile");
        permissions.add("user_friends");
        permissions.add("user_location");
        permissions.add("user_birthday");
        permissions.add("user_likes");

        Session.OpenRequest openRequest = new Session.OpenRequest(this).setPermissions(permissions).setCallback(statusCallback);
        
        if (!session.isOpened() && !session.isClosed()) {							System.out.println("if (!session.isOpened() && !session.isClosed())");
			session.openForRead(openRequest);
        } else if (session.isOpened()) {											System.out.println("else if (session.isOpened())");
        	session.closeAndClearTokenInformation();
        } else if (session.isClosed()) {											System.out.println("else if (session.isClosed())");
        	Session.openActiveSession(this, true, permissions, statusCallback);
        } else {																	System.out.println("else");
        	Session.openActiveSession(this, true, statusCallback);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {													System.out.println("if (!session.isClosed()) 其實就等於 if (session.isOpened())");
        	session.closeAndClearTokenInformation();
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @SuppressWarnings("deprecation")
		@Override
        public void call(Session session, SessionState state, Exception exception) {	System.out.println("call(Session session, SessionState state, Exception exception)");
        if (session.isOpened()) {														System.out.println("if (session.isOpened())");
            //make request to the /me API
            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
            	@Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // Display the parsed user info
                        textInstructionsOrLink.setText(buildUserInfoDisplay(user));
                    }
                }
            });
         }
        	
        	updateView();																System.out.println("line166: updateView();");
        }
    }
    
    private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");
        
        String usertostring = user.toString();											Log.e("usertostring", usertostring);
        
        // Example: typed access (link)
        // - no special permissions required
        String link = user.getLink();
        userInfo.append(String.format("link: %s\n\n", link));
        
        // Example: typed access (id)
        // - no special permissions required
        String id = user.getId();
        userInfo.append(String.format("id: %s\n\n", id));
        
        // Example: typed access (email)
        // - requires email	 permission
        Object email = user.getProperty("email");										Log.e("email", email.toString());
        userInfo.append(String.format("email: %s\n\n", email));
        
        // Example: typed access (firstName)
        // - no special permissions required
        String firstName = user.getFirstName();
        userInfo.append(String.format("firstName: %s\n\n", firstName));
        
        // Example: typed access (MiddleName)
        // - no special permissions required
        String MiddleName = user.getMiddleName();
        userInfo.append(String.format("MiddleName: %s\n\n", MiddleName));
        
        // Example: typed access (lastName)
        // - no special permissions required
        String lastName = user.getLastName();
        userInfo.append(String.format("lastName: %s\n\n", lastName));
        
        // Example: typed access (name)
        // - no special permissions required
        String name = user.getName();
        userInfo.append(String.format("Name: %s\n\n", name));
        
        // Example: typed access (Username)
        // - no special permissions required
        String Username = user.getUsername();
        userInfo.append(String.format("Username: %s\n\n", Username));

        // Example: typed access (birthday)
        // - requires user_birthday permission
        String birthday = user.getBirthday();
        userInfo.append(String.format("Birthday: %s\n\n", birthday));

        // Example: partially typed access, to location field,
        // name key (location)
        // - requires user_location permission
        Object location = user.getLocation().getProperty("name");
        userInfo.append(String.format("Location: %s\n\n", location));

        // Example: access via property name (locale)
        // - no special permissions required
        Object locale = user.getProperty("locale");										Log.e("locale", locale.toString());
        userInfo.append(String.format("Locale: %s\n\n", locale));

        // Example: access via key for array (languages) 
        // - requires user_likes permission
        JSONArray languages = (JSONArray)user.getProperty("languages");
        if (languages.length() > 0) {
            ArrayList<String> languageNames = new ArrayList<String> ();
            for (int i=0; i < languages.length(); i++) {
                JSONObject language = languages.optJSONObject(i);
                // Add the language name to a list. Use JSON
                // methods to get access to the name field. 
                languageNames.add(language.optString("name"));
            }           
            userInfo.append(String.format("Languages: %s\n\n", languageNames.toString()));
        }

        return userInfo.toString();
    }
    
}
