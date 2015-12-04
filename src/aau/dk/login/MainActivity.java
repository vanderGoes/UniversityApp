package aau.dk.login;

import aau.dk.menufragments.MapActivity;
import aau.dk.menufragments.MenuActivity;
import aau.dk.universityapplication.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;



public class MainActivity extends Activity {

	public static final boolean Visitor = false;
	protected ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final EditText etUsername = (EditText) findViewById(R.id.etUsername);
		final EditText etPassword = (EditText) findViewById(R.id.etPassword);
		Parse.initialize(getApplicationContext(),
				"[Application ID]",
				"[Client ID]");
		ParseUser currentUser = ParseUser.getCurrentUser();
		final ParseUser user = new ParseUser();

		if (currentUser != null) {
			Toast.makeText(MainActivity.this, "Welcome on University application\nYou are logged in as "+ currentUser.getUsername(), Toast.LENGTH_LONG).show();
			Intent j = new Intent();
			j.setClass(getApplicationContext(), MenuActivity.class);
			j.putExtra("Visitor", false);
			startActivity(j);
		}
		else {
			Toast.makeText(MainActivity.this, "Please sign in first", Toast.LENGTH_LONG).show();

			Button btnJumpToSignUp = (Button) findViewById(R.id.btnJumptoSignUp);
			btnJumpToSignUp.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent w = new Intent();
					w.setClass(getApplicationContext(), SignUpActivity.class);
					startActivity(w);
				}
			});

			Button btnJumptoMenu = (Button) findViewById(R.id.btnJumptoMenu);
			btnJumptoMenu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(etUsername.getText().toString().length() == 0 || etPassword.getText().toString().length() == 0){
						Toast.makeText(MainActivity.this, "Please indicate your username and password before log in", Toast.LENGTH_LONG).show();
					}
					else{
						MainActivity.this.progressDialog = ProgressDialog.show(
					            MainActivity.this, "", "Logging in...", true);
						ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {						@Override
							public void done(ParseUser user, com.parse.ParseException e) {
								if (e==null && user != null) {
									Intent signin = new Intent();
									signin.setClass(getApplicationContext(), MenuActivity.class);
									signin.putExtra("Visitor", false);
									MainActivity.this.progressDialog.dismiss();
									startActivity(signin);
									Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_LONG).show();// Hooray! The user is logged in.
								} else if (user==null){

									Toast.makeText(MainActivity.this, "Please sign up if it is the first time you are using University App", Toast.LENGTH_LONG).show();
									MainActivity.this.progressDialog.dismiss();
								}
								else{
									Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
									MainActivity.this.progressDialog.dismiss();
								}
							}});


					}	
					}
					
			});
			Button btnJumpToVisit = (Button) findViewById(R.id.btnJumpToVisit);
			btnJumpToVisit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MenuActivity.class);
					i.putExtra("Visitor", true);
					startActivity(i);
				}
			});
		}


	}

	


}



