package aau.dk.menufragments;

import java.util.List;

import aau.dk.login.MainActivity;
import aau.dk.universityapplication.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ShowOneProfileActivity extends Activity {
	private static String IdUser = null;
	protected ProgressDialog progressDialog;
	// Declare Variables
	TextView txtname;
	String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.activity_show_one_profile);
		 final ProgressDialog mProgressDialog = ProgressDialog.show(this, "Please wait",
		          "Loading information...", true);
		
		TextView tvUsername = (TextView) findViewById(R.id.username);
		final TextView tvFirstName = (TextView) findViewById(R.id.firstName);
		final TextView tvLastName = (TextView) findViewById(R.id.lastName);
		final TextView tvEmail = (TextView) findViewById(R.id.email);
		final TextView tvUniversity = (TextView) findViewById(R.id.University);
		final TextView tvUserType = (TextView) findViewById(R.id.userType);
		final ImageView Img = (ImageView) findViewById(R.id.iv_picture);
		Parse.initialize(getApplicationContext(),
				"[APPLICATION ID]",
				"[CLIENT ID]");
		// Retrieve data from CourseActivity on item click event
		Intent i = getIntent();

		// Get the name
		name = i.getStringExtra("username");
		tvUsername.setText(name);

		
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", name);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List objects, com.parse.ParseException e) {
				// TODO Auto-generated method stub
				
				ParseUser u = (ParseUser)objects.get(0);
				String email = u.get("email").toString();
				String firstName = u.get("firstName").toString();
				String lastName = u.get("lastName").toString();
				String University = u.get("University").toString();
				String userType = u.get("userType").toString();
				IdUser = u.getObjectId();
				tvFirstName.setText(firstName + " " + lastName);
				tvEmail.setText(email);
				//tvLastName.setText(lastName);
				tvUniversity.setText(University);
				tvUserType.setText(userType);
				//Toast.makeText(ShowOneProfileActivity.this, IdUser ,Toast.LENGTH_LONG).show();
			}
		});

		ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Photo");
		query1.whereEqualTo("userID", IdUser);
		query1.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject object, com.parse.ParseException e) {
				// TODO Auto-generated method stub
				ParseFile image = (ParseFile) object.get("photo");
				image.getDataInBackground(new GetDataCallback() {
					@Override
					public void done(byte[] data, com.parse.ParseException e) {
						// TODO Auto-generated method stub
						Bitmap b=BitmapFactory.decodeByteArray(data, 0, data .length);
						int width = b.getWidth();
						int height = b.getHeight();

						// Determine how much to scale: the dimension requiring less scaling is
						// closer to the its side. This way the image always stays inside your
						// bounding box AND either x/y axis touches it.
						float xScale = ((float) 300) / width;
						float yScale = ((float) 300) / height;
						float scale = (xScale <= yScale) ? xScale : yScale;

						// Create a matrix for the scaling and add the scaling data
						Matrix matrix = new Matrix();
						matrix.postScale(scale, scale);

						// Create a new bitmap and convert it to a format understood by the ImageView
						Bitmap scaledBitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);
						BitmapDrawable result = new BitmapDrawable(scaledBitmap);
						width = scaledBitmap.getWidth();
						height = scaledBitmap.getHeight();

						// Apply the scaled bitmap
						Img.setImageDrawable(result);

						// Now change ImageView's dimensions to match the scaled image
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) Img.getLayoutParams();
						params.width = width;
						params.height = height;
						Img.setLayoutParams(params);
						Img.setImageBitmap(b);
						mProgressDialog.dismiss();
					}
				
				});
			}
		});
	
		//ShowOneProfileActivity.this.progressDialog.dismiss();

		// Locate the TextView in singleitemview.xml


		// Load the text into the TextView
		//txtname.setText(name);

	}
}