package aau.dk.login;

import java.util.ArrayList;
import java.util.List;

import aau.dk.menufragments.MapActivity;
import aau.dk.menufragments.MenuActivity;
import aau.dk.universityapplication.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

	public static final String USERNAME = "";
	public static final boolean Visitor = false;
	protected ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		Parse.initialize(getApplicationContext(),
				"[APPLICATION ID]",
				"[CLIENT ID]");
		final EditText etUsernameSignUp = (EditText) findViewById(R.id.etUsernameSignUp);
		final EditText etPasswordSignUp = (EditText) findViewById(R.id.etPasswordSignUp);
		final EditText etFirstNameSignUp = (EditText) findViewById(R.id.etFirstNameSignUp);
		final EditText etLastNameSignUp = (EditText) findViewById(R.id.etLastNameSignUp);
		final Spinner spUniversity = (Spinner) findViewById(R.id.spUniversity);
		final Spinner spStudy = (Spinner) findViewById(R.id.spStudy);

		final List<String> liUniversity = new ArrayList<String>();
		final List<String> liUniversityID = new ArrayList<String>();
		final List<String> liStudy = new ArrayList<String>();
		final List<String> liStudyID = new ArrayList<String>();

		final ParseUser user = new ParseUser();

		// addItemsOnSpinnerUniversity();
		final ArrayAdapter<String> dataAdapterUniversity = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				liUniversity);
		dataAdapterUniversity
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spUniversity.setAdapter(dataAdapterUniversity);

		ParseQuery<ParseObject> queryU = ParseQuery.getQuery("University");
		queryU.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (int i = 0; i < objects.size(); i++) {
						liUniversity.add(objects.get(i).get("name").toString());
						liUniversityID.add(objects.get(i).getObjectId()
								.toString());
						dataAdapterUniversity.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(
							getApplicationContext().getApplicationContext(),
							"Error loading universities", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// addItemsOnSpinnerStudy();
		final ArrayAdapter<String> dataAdapterStudy = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				liStudy);
		dataAdapterStudy
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spStudy.setAdapter(dataAdapterStudy);

		ParseQuery<ParseObject> queryS = ParseQuery.getQuery("Study");
		// queryS.whereEqualTo("universityID",
		// liUniversityID.get(spUniversity.getSelectedItemPosition()));
		queryS.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objectsS, ParseException e) {
				if (e == null) {
					for (int i = 0; i < objectsS.size(); i++) {
						liStudy.add(objectsS.get(i).get("name").toString());
						liStudyID.add(objectsS.get(i).getObjectId().toString());
						dataAdapterStudy.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(
							getApplicationContext().getApplicationContext(),
							"Error loading studies", Toast.LENGTH_LONG).show();
				}
			}
		});

		Button btnJumpToMenu = (Button) findViewById(R.id.btnJumpToMenu);
		btnJumpToMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SignUpActivity.this.progressDialog = ProgressDialog.show(
						SignUpActivity.this, "", "Creation of your account..",
						true);
				// Bundle extras = getIntent().getExtras();

				user.setUsername(etUsernameSignUp.getText().toString());
				user.setPassword(etPasswordSignUp.getText().toString());

				user.put("firstName", etFirstNameSignUp.getText().toString());
				user.put("lastName", etLastNameSignUp.getText().toString());
				user.setEmail(etUsernameSignUp.getText().toString()
						+ "@exemple.com");
				user.put("userType", "Student");
				user.put("lastName", etLastNameSignUp.getText().toString());
				user.put("lastName", etLastNameSignUp.getText().toString());
				user.put("University", liUniversityID.get(spUniversity
						.getSelectedItemPosition()));
				user.put("Study",
						liStudyID.get(spStudy.getSelectedItemPosition()));
				SignUpActivity.this.progressDialog.dismiss();
				user.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							Intent j = new Intent();
							j.putExtra(USERNAME, etUsernameSignUp.getText()
									.toString());
							j.setClass(getApplicationContext(),
									ProfilePictureActivity.class);
							startActivity(j);
						} else {
							Toast.makeText(SignUpActivity.this, e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					}
				});

			}
		});

		Button btnJumpToFb = (Button) findViewById(R.id.btnJumpToFb);
		btnJumpToFb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent facebook = new Intent();
				facebook.setClass(getApplicationContext(),
						FacebookLoginActivity.class);
				startActivity(facebook);
			}
		});

	}

}
