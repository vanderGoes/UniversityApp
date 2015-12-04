package aau.dk.course;

import aau.dk.universityapplication.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ACourseCreate extends Fragment {

	public static final String TAG = "Create course";	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// create your view using LayoutInflater 
		return inflater.inflate(R.layout.activity_coursecreate, container, false);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		
		final EditText etCourseName = (EditText) getView().findViewById(R.id.etCourseName);
		final EditText etCourseCode = (EditText) getView().findViewById(R.id.etCourseCode);
		final EditText etECTS = (EditText) getView().findViewById(R.id.etECTS);
		final TextView tvTeacherFill = (TextView) getView().findViewById(R.id.tvTeacherFill);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			String teacherName = currentUser.get("firstName") + " " + currentUser.get("lastName");
			tvTeacherFill.setText(teacherName);
		} else {
			Toast.makeText(getActivity(), "Error Current User",
					Toast.LENGTH_LONG).show();
		}
		
		Button btnCreateCourse = (Button) getView().findViewById(R.id.btnCreateCourse);
		btnCreateCourse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(etCourseName.getText().toString())) {
					etCourseName.setError("Name cannot be empty!");
				} else if ("".equals(etCourseCode.getText().toString())) {
					etCourseCode.setError("Code cannot be empty!");
				} else if ("".equals(etECTS.getText().toString())) {
					etECTS.setError("ECTS cannot be empty!");
				} else {
					ParseObject object = new ParseObject("Course");
					object.put("name", etCourseName.getText().toString());
					object.put("courseCode", etCourseCode.getText().toString());
					object.put("ects",
							Integer.parseInt(etECTS.getText().toString()));
					object.put("teacher", ParseUser.getCurrentUser().getObjectId().toString());
					object.saveInBackground();
					Toast.makeText(getActivity(), "Course created",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
