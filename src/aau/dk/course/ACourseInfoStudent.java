package aau.dk.course;

import java.util.List;

import aau.dk.universityapplication.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ACourseInfoStudent extends Activity {

	private boolean enrolled = false;
	private String enrolledID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		final String courseID = intent.getStringExtra("courseID");

		setContentView(R.layout.activity_ci_student);

		final TextView tvCourseNameFill = (TextView) findViewById(R.id.tvCourseNameFill);
		final TextView tvCourseCodeFill = (TextView) findViewById(R.id.tvCourseCodeFill);
		final TextView tvECTSFill = (TextView) findViewById(R.id.tvECTSFill);
		final TextView tvTeacherFill = (TextView) findViewById(R.id.tvTeacherFill);
		final Button btnEnrollCourse = (Button) findViewById(R.id.btnEnrollCourse);

		Parse.initialize(this, "[APPLICATION ID]",
				"[CLIENT ID]");

		final String profileID = ParseUser.getCurrentUser().getObjectId()
				.toString();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
		query.getInBackground(courseID, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {

					tvCourseNameFill.setText(object.get("name").toString());
					tvCourseCodeFill.setText(object.get("courseCode")
							.toString());
					tvECTSFill.setText(object.get("ects").toString());
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("_User");
					query.getInBackground(object.get("teacher").toString(),
							new GetCallback<ParseObject>() {
								public void done(ParseObject objectT,
										ParseException e) {
									if (e == null) {
										tvTeacherFill.setText(objectT.get(
												"firstName").toString()
												+ " " + objectT.get("lastName"));
									} else {
										Toast.makeText(getApplicationContext(),
												"Error show teacher",
												Toast.LENGTH_LONG).show();
									}
								}
							});
					ParseQuery<ParseObject> queryE = ParseQuery
							.getQuery("StudentCourse");
					queryE.findInBackground(new FindCallback<ParseObject>() {
						public void done(List<ParseObject> objectsE,
								ParseException e) {
							if (e == null) {
								for (int i = 0; i < objectsE.size(); i++) {
									if ((objectsE.get(i).get("courseID")
											.toString().equals(courseID))
											&& (objectsE.get(i).get("profileD")
													.toString()
													.equals(profileID))) {
										enrolled = true;
										enrolledID = objectsE.get(i)
												.getObjectId();
										btnEnrollCourse
												.setText("Unenroll from course");
									}
								}
							}
						}
					});
				} else {
					Toast.makeText(getApplicationContext(),
							"Error show course", Toast.LENGTH_LONG).show();
				}
			}
		});

		btnEnrollCourse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (enrolled == false) {
					ParseObject newEnroll = new ParseObject("StudentCourse");
					newEnroll.put("courseID", courseID);
					newEnroll.put("profileD", profileID);
					newEnroll.saveInBackground();
					Toast.makeText(getApplicationContext(),
							"You have been enrolled.", Toast.LENGTH_LONG)
							.show();
					btnEnrollCourse.setText("Unenroll from course");
					ParseQuery<ParseObject> queryE = ParseQuery
							.getQuery("StudentCourse");
					queryE.findInBackground(new FindCallback<ParseObject>() {
						public void done(List<ParseObject> objectsE,
								ParseException e) {
							if (e == null) {
								for (int i = 0; i < objectsE.size(); i++) {
									if ((objectsE.get(i).get("courseID")
											.toString().equals(courseID))
											&& (objectsE.get(i).get("profileD")
													.toString()
													.equals(ParseUser
															.getCurrentUser()
															.getObjectId()))) {
										enrolled = true;
										enrolledID = objectsE.get(i)
												.getObjectId();
									}
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"Error enrolledID", Toast.LENGTH_LONG)
										.show();
							}
						}
					});
				} else {
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("StudentCourse");
					query.getInBackground(enrolledID,
							new GetCallback<ParseObject>() {
								public void done(ParseObject objectD,
										ParseException e) {
									objectD.deleteInBackground();
								}
							});
					enrolled = false;
					enrolledID = null;
					btnEnrollCourse.setText("Enroll for course");
					Toast.makeText(getApplicationContext(),
							"You have been unenrolled.", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		Button btnCourseChat = (Button) findViewById(R.id.btnCourseChat);
		btnCourseChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(ACourseInfoStudent.this,
						ChatActivity.class);
				//Toast.makeText(getApplicationContext(), courseID, Toast.LENGTH_LONG).show();
				myIntent.putExtra("courseID", courseID); // Optional parameters
				//Toast.makeText(getApplicationContext(), profileID, Toast.LENGTH_LONG).show();
				myIntent.putExtra("profileID", ParseUser.getCurrentUser().getObjectId()
						.toString());
						//profileID);
				ACourseInfoStudent.this.startActivity(myIntent);
			}
		});
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

}
