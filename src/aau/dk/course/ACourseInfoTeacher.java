
package aau.dk.course;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import aau.dk.universityapplication.R;

public class ACourseInfoTeacher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ci_teacher);
		Intent intent = getIntent();
		final String courseID = intent.getStringExtra("courseID");

		final EditText etCourseName = (EditText) findViewById(R.id.etCourseName);
		final EditText etCourseCode = (EditText) findViewById(R.id.etCourseCode);
		final EditText etECTS = (EditText) findViewById(R.id.etECTS);
		final TextView tvTeacherFill = (TextView) findViewById(R.id.tvTeacherFill);

		Parse.initialize(this, "[APPLICATION ID]",
				"[CLIENT ID]");

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
		query.getInBackground(courseID, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					etCourseName.setText(object.get("name").toString());
					etCourseCode.setText(object.get("courseCode").toString());
					etECTS.setText(object.get("ects").toString());
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("_User");
					query.getInBackground(object.get("teacher").toString(),
							new GetCallback<ParseObject>() {
								public void done(ParseObject objectT,
										ParseException e) {
									if (e == null) {
										tvTeacherFill.setText(objectT.get(
												"firstName").toString()
												+ " "
												+ objectT.get("lastName")
														.toString());
									} else {
										Toast.makeText(getApplicationContext(),
												"Error show teacher",
												Toast.LENGTH_LONG).show();
									}
								}
							});
				} else {
					Toast.makeText(getApplicationContext(),
							"Error show course", Toast.LENGTH_LONG).show();
				}
			}
		});

		Button btnUpdateCourse = (Button) findViewById(R.id.btnUpdateCourse);
		btnUpdateCourse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(etCourseName.getText().toString())) {
					etCourseName.setError("Name cannot be empty!");
				} else if ("".equals(etCourseCode.getText().toString())) {
					etCourseCode.setError("Code cannot be empty!");
				} else if ("".equals(etECTS.getText().toString())) {
					etECTS.setError("ECTS cannot be empty!");
				} else {
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("Course");
					query.getInBackground(courseID,
							new GetCallback<ParseObject>() {
								public void done(ParseObject object,
										ParseException e) {
									if (e == null) {
										object.put("name", etCourseName
												.getText().toString());
										object.put("courseCode", etCourseCode
												.getText().toString());
										object.put("ects", Integer
												.parseInt(etECTS.getText()
														.toString()));
										object.saveInBackground();
										Toast.makeText(getApplicationContext(),
												"Course updated",
												Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(getApplicationContext(),
												"Error update",
												Toast.LENGTH_LONG).show();
									}
								}
							});
				}
			}
		});

		Button btnCourseDelete = (Button) findViewById(R.id.btnCourseDelete);
		btnCourseDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> queryD = ParseQuery.getQuery("Course");
				queryD.getInBackground(courseID, new GetCallback<ParseObject>() {
					public void done(ParseObject objectD, ParseException e) {
						objectD.deleteInBackground();
					}
				});
			}
		});
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
