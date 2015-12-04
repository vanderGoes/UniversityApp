package aau.dk.login;


import aau.dk.menufragments.MenuActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class FacebookLoginActivity extends Activity {
	static private String TAG = "FacebookLoginActivity";

	protected ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_menu);
		Bundle extras = getIntent().getExtras();

		
		Parse.initialize(getApplicationContext(),
				"[APPLICATION ID]",
				"[CLIENT ID]");
		
		final ParseUser parseUser = new ParseUser();
		
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Log.e(TAG,"start");

					// make request to the /me API
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								Log.i(TAG, "the user is not null");
								Toast.makeText(FacebookLoginActivity.this,
										"Hello " + user.getName() + "!",
										Toast.LENGTH_LONG).show();
								FacebookLoginActivity.this.progressDialog = ProgressDialog.show(
							            FacebookLoginActivity.this, "", "Creation of your account..", true);
								
								parseUser.setUsername(user.getUsername());
								parseUser.put("firstName", user.getFirstName());
								parseUser.put("lastName", user.getLastName());
								parseUser.setPassword("test");
								Log.i(TAG,"The user is registered in the parse DB");

								parseUser.signUpInBackground(new SignUpCallback() {
									
									@Override
									public void done(ParseException e) {
										if(e==null){
											Intent signinactivity = new Intent();
											signinactivity.setClass(getApplicationContext(), MainActivity.class);
											startActivity(signinactivity);
										}
										else{
											Log.i(TAG,"There is an exeption in the Parse registration");
											Toast.makeText(FacebookLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
									}
									}});
								
							
								FacebookLoginActivity.this.progressDialog.dismiss();
								Log.i(TAG,"Everything is well done");

								
								Intent mainmenu = new Intent();
								mainmenu.setClass(getApplicationContext(), MenuActivity.class);
								mainmenu.putExtra("Visitor", false);
								startActivity(mainmenu);
							}
							else{
								Toast.makeText(FacebookLoginActivity.this, "Something went wrong !",
										Toast.LENGTH_LONG).show(); 
							}
						}
					});
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
