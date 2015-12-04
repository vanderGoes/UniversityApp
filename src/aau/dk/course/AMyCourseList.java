package aau.dk.course;

import java.util.ArrayList;
import java.util.List;

import aau.dk.universityapplication.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AMyCourseList extends Fragment {

	public static final String TAG = "Your courses";

	private ListView mylistview;
	private ArrayAdapter<String> myadapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// create your view using LayoutInflater
		return inflater.inflate(R.layout.activity_mycourselist, container,
				false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final String profileID = ParseUser.getCurrentUser().getObjectId().toString();
		
		mylistview = (ListView) getView().findViewById(R.id.mylistview);
		myadapter = new ArrayAdapter<String>(getActivity(),
				R.layout.activity_mymain_listview);
		final List<String> mycourseID = new ArrayList<String>();

		if (ParseUser.getCurrentUser().get("userType").equals("student")) {
			ParseQuery<ParseObject> query = ParseQuery
					.getQuery("StudentCourse");
			query.whereEqualTo("profileD", profileID);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
						for (int i = 0; i < objects.size(); i++) {
							ParseQuery<ParseObject> queryC = ParseQuery
									.getQuery("Course");
							queryC.addAscendingOrder("name");
							String mycourseIDtag = objects.get(i)
									.get("courseID").toString();
							queryC.getInBackground(mycourseIDtag,
									new GetCallback<ParseObject>() {
										@Override
										public void done(ParseObject objectC,
												ParseException e) {
											if (e == null) {
												String mycourseName = objectC
														.get("name").toString();
												myadapter.add(mycourseName);
												mycourseID.add(objectC
														.getObjectId()
														.toString());
												myadapter
														.notifyDataSetChanged();
											} else {
												Toast.makeText(
														getActivity(),
														"Error loading course name.",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});
						}
					} else {

					}
				}
			});
		} else {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
			query.whereEqualTo("teacher", profileID);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
						for (int i = 0; i < objects.size(); i++) {
							String mycourseName = objects.get(i).get("name")
									.toString();
							myadapter.add(mycourseName);
							mycourseID.add(objects.get(i).getObjectId()
									.toString());
							myadapter.notifyDataSetChanged();
						}
					} else {
						Toast.makeText(getActivity(),
								"Error loading course name.",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		mylistview.setAdapter(myadapter);
		mylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (ParseUser.getCurrentUser().get("userType").toString()
						.equals("student")) {
					Intent i = new Intent(getActivity(),
							ACourseInfoStudent.class);
					i.putExtra("courseID", mycourseID.get(position).toString());
					startActivity(i);
				} else {
					Intent i = new Intent(getActivity(),
							ACourseInfoTeacher.class);
					i.putExtra("courseID", mycourseID.get(position).toString()
							.toString());
					startActivity(i);
				}
			}
		});

	}
}
