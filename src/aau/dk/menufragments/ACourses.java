package aau.dk.menufragments;

import com.parse.Parse;
import com.parse.ParseUser;

import aau.dk.universityapplication.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.Toast;

public class ACourses extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);

		Parse.initialize(this, "[APPLICATION ID]",
				"[CLIENT ID]");
		
		if (ParseUser.getCurrentUser().get("userType").toString().equals("teacher")) {
			aau.dk.course.CourseFA adapter = new aau.dk.course.CourseFA(getSupportFragmentManager());
			ViewPager p = (ViewPager) findViewById(R.id.pager);
			p.setAdapter(adapter);
		} else {
			aau.dk.course.CourseFAStudent adapter = new aau.dk.course.CourseFAStudent(
					getSupportFragmentManager());
			ViewPager p = (ViewPager) findViewById(R.id.pager);
			p.setAdapter(adapter);
		}

		// p.setCurrentItem(EditProfileFragmentAdapter.NUM_ITEMS-1);
	}

}
