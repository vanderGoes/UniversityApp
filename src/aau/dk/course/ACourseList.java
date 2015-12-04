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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ACourseList extends Fragment {

	public static final String TAG = "All courses";

	private ListView listview;
	private ArrayAdapter<String> adapter;
	ProgressDialog mProgressDialog;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// create your view using LayoutInflater
		return inflater.inflate(R.layout.activity_courselist, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listview = (ListView) getView().findViewById(R.id.listview);
		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.activity_main_listview);
		final List<String> courseID = new ArrayList<String>();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
		query.orderByDescending("name");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (int i = 0; i < objects.size(); i++) {
						adapter.add((String) objects.get(i).getString("name"));
						courseID.add(objects.get(i).getObjectId().toString());
						adapter.notifyDataSetChanged();
					}
				} else {
				}
			}
		});
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (ParseUser.getCurrentUser().get("userType").toString()
						.equals("student")) {
					Intent i = new Intent(getActivity(),
							ACourseInfoStudent.class);
					i.putExtra("courseID", courseID.get(position).toString());
					startActivity(i);
				} else {
					Intent i = new Intent(getActivity(),
							ACourseInfoTeacher.class);
					i.putExtra("courseID", courseID.get(position).toString()
							.toString());
					startActivity(i);
				}
			}
		});

	}
}
