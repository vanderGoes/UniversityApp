package aau.dk.login;

import java.io.ByteArrayOutputStream;
import java.io.File;

import aau.dk.menufragments.SearchProfileActivity;
import aau.dk.menufragments.MenuActivity;
import aau.dk.universityapplication.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;


import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;



import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfilePictureActivity extends Activity {

	public static final boolean Visitor = false;
	private Uri mImageCaptureUri;
	private ImageView mImageView;
	protected ProgressDialog progressDialog;

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	public static boolean PHOTO = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile_picture);
		PHOTO = false;
		Parse.initialize(this,
				"[APPLICATION ID]",
				"[CLIENT ID]");
		ParseUser currentUser = ParseUser.getCurrentUser();
		final String IdUser = currentUser.getObjectId();

		Button btnSkipToMenu = (Button) findViewById(R.id.btnSkipToMenu);
		btnSkipToMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ProfilePictureActivity.this.progressDialog = ProgressDialog.show(
						ProfilePictureActivity.this, "", "Edition of your profile...", true);



				ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
				query.whereEqualTo("userID", "4GXdd80MWx");
				query.getFirstInBackground(new GetCallback<ParseObject>() {
					public void done(ParseObject object, ParseException e) {
						ParseObject photo=new ParseObject("Photo");
						photo.put("userID", IdUser);
						photo.put("photo", (ParseFile)object.get("photo"));
						photo.saveInBackground();
						ProfilePictureActivity.this.progressDialog.dismiss();
						Toast.makeText(ProfilePictureActivity.this,
								"Account created", Toast.LENGTH_LONG).show();
						Intent menu = new Intent();
						menu.setClass(getApplicationContext(), MenuActivity.class);
						menu.putExtra("Visitor", false);
						startActivity(menu);
					}
				});

			}
		});

		Button btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(PHOTO == true){
					Intent w = new Intent();
					w.setClass(getApplicationContext(), MenuActivity.class);
					Toast.makeText(ProfilePictureActivity.this,
							"Account created", Toast.LENGTH_LONG).show();
					w.putExtra("Visitor", false);
					startActivity(w);
				}
				else{
					Toast.makeText(ProfilePictureActivity.this,
							"Please select your photo",
							Toast.LENGTH_SHORT).show();
				}


			}
		});

		final String [] items           = new String [] {"From Camera", "From SD Card"};
		ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder     = new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file        = new File(Environment.getExternalStorageDirectory(),
							"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}

					dialog.cancel();
				} else {
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );

		final AlertDialog dialog = builder.create();

		mImageView = (ImageView) findViewById(R.id.iv_pic);

		((ImageButton) findViewById(R.id.btn_choose)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ProfilePictureActivity.this.progressDialog = ProgressDialog.show(
				ProfilePictureActivity.this, "", "Edition of your profile...", true);
		if (resultCode != RESULT_OK) return;

		Bitmap bitmap   = null;
		String path     = "";

		if (requestCode == PICK_FROM_FILE) {
			mImageCaptureUri = data.getData();
			path = getRealPathFromURI(mImageCaptureUri); //from Gallery

			if (path == null)
				path = mImageCaptureUri.getPath(); //from File Manager

			if (path != null)
				bitmap  = BitmapFactory.decodeFile(path);
		} else {
			path    = mImageCaptureUri.getPath();
			bitmap  = BitmapFactory.decodeFile(path);
		}
		Toast.makeText(ProfilePictureActivity.this,
				path, Toast.LENGTH_LONG).show();
		Parse.initialize(this,
				"[Application ID]",
				"[Client ID]");
		ParseUser currentUser = ParseUser.getCurrentUser();
		final String IdUser = currentUser.getObjectId();
		store(bitmap, IdUser);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

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
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		BitmapDrawable result = new BitmapDrawable(scaledBitmap);
		width = scaledBitmap.getWidth();
		height = scaledBitmap.getHeight();

		// Apply the scaled bitmap
		mImageView.setImageDrawable(result);

		// Now change ImageView's dimensions to match the scaled image
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
		params.width = width;
		params.height = height;
		mImageView.setLayoutParams(params);

		mImageView.setImageBitmap(bitmap);
		ProfilePictureActivity.this.progressDialog.dismiss();

	}

	public String getRealPathFromURI(Uri contentUri) {
		String [] proj      = {MediaStore.Images.Media.DATA};
		Cursor cursor       = managedQuery( contentUri, proj, null, null,null);

		if (cursor == null) return null;

		int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	//Function store update retrieve

	public void store(Bitmap bitmap,String userID){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		final byte[] data = stream.toByteArray(); 
		ParseFile imageFile = new ParseFile("image.jpg", data);
		ParseObject o=new ParseObject("Photo");
		o.put("userID", userID);
		o.put("photo", imageFile);
		imageFile.saveInBackground();
		o.saveInBackground();
		PHOTO = true;
	}

	public void update(Bitmap bitmap,String userID){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("userID", userID);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					object.deleteInBackground();
					Toast.makeText(ProfilePictureActivity.this,
							"Former object deleted", Toast.LENGTH_LONG).show();

				}else{

				}

			}
		});
		store(bitmap,userID);
		Toast.makeText(ProfilePictureActivity.this,
				"New object uploaded", Toast.LENGTH_LONG).show();
	}



	public void retrieve(String userID){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("userID", userID);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					Toast.makeText(ProfilePictureActivity.this,
							"image request", Toast.LENGTH_LONG).show();
					ParseFile imageFile2 = (ParseFile)object.get("photo");
					Toast.makeText(ProfilePictureActivity.this,
							"image loaded", Toast.LENGTH_LONG).show();
					imageFile2.getDataInBackground(new GetDataCallback() {

						public void done(byte[] data, ParseException e) {
							Toast.makeText(ProfilePictureActivity.this,
									"image loaded", Toast.LENGTH_LONG).show();
							if (data != null) {
								// Success; data has the file
								Bitmap b=BitmapFactory.decodeByteArray(data, 0, data .length);
								if (b==null){
									Toast.makeText(ProfilePictureActivity.this,
											"No image", Toast.LENGTH_LONG).show();
								}
								else {

									//image.setImageBitmap(b);}

								}} else {
									Toast.makeText(ProfilePictureActivity.this,
											e.getMessage(), Toast.LENGTH_LONG).show();
									// Failed
								}
						}
					});
				}
			}
		}
				);


	}
}