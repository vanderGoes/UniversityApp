package aau.dk.fragmentprofile;

import java.io.ByteArrayOutputStream;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import aau.dk.login.ProfilePictureActivity;
import aau.dk.menufragments.MenuActivity;
import aau.dk.universityapplication.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileFragmentPicture extends Fragment {


	public static final String TAG = "Profile Picture";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// create your view using LayoutInflater 
		return inflater.inflate(R.layout.activity_edit_profile_fragment_picture, container, false);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// do your variables initialisations here except Views!!!
	}

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		// initialise your views
		final ImageView Img = (ImageView) getView().findViewById(R.id.iv_picture);

		ParseUser currentUser = ParseUser.getCurrentUser();
		final String IdUser = currentUser.getObjectId();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("userID", IdUser);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				ParseFile image = (ParseFile) object.get("photo");
				image.getDataInBackground(new GetDataCallback() {
					
					@Override
					public void done(byte[] data, ParseException e) {
						// TODO Auto-generated method stub
						Bitmap b=BitmapFactory.decodeByteArray(data, 0, data .length);
						int width = b.getWidth();
						int height = b.getHeight();

						// Determine how much to scale: the dimension requiring less scaling is
						// closer to the its side. This way the image always stays inside your
						// bounding box AND either x/y axis touches it.
						float xScale = ((float) 700) / width;
						float yScale = ((float) 700) / height;
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
					}
				
				});
			}
		});

	}
}