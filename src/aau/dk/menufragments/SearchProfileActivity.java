package aau.dk.menufragments;
import java.util.List;

import aau.dk.universityapplication.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SearchProfileActivity extends Activity {
	// Declare Variables
	ListView listview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from listview_main.xml
		setContentView(R.layout.activity_search_profile);
		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(SearchProfileActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("University Application");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Locate the class table named "Country" in Parse.com
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					"_User");
			query.orderByDescending("_created_at");
			try {
				ob = query.find();
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Locate the listview in listview_main.xml
			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into an ArrayAdapter
			adapter = new ArrayAdapter<String>(SearchProfileActivity.this,
					R.layout.activity_search_profile_listview);
			// Retrieve object "name" from Parse.com database
			for (ParseObject country : ob) {
				adapter.add((String) country.get("username"));
			}
			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
			// Capture button clicks on ListView items
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Send single item click data to ShowOneProfileActivity Class
					Intent i = new Intent(SearchProfileActivity.this,
							ShowOneProfileActivity.class);
					// Pass data "name" followed by the position
					i.putExtra("username", ob.get(position).getString("username")
							.toString());
					// Open ShowOneProfileActivity.java Activity
					startActivity(i);
				}
			});
		}
	}
}