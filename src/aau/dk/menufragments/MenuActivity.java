package aau.dk.menufragments;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import aau.dk.fragmentprofile.EditProfileActivity;
import aau.dk.login.MainActivity;
import aau.dk.menufragments.calendar.DatePickerDialogFragment;
import aau.dk.menufragments.calendar.EventCreateFragment;
import aau.dk.universityapplication.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

public class MenuActivity extends Activity {
	private Boolean Visitor;
	private static String FILENAME = "73771_aalborg-webjuli13pdf.pdf";
	private static String FILE_URL = "http://www.aau.dk/digitalAssets/73/73771_aalborg-webjuli13pdf.pdf";
	private static File FILE = new File(Environment.getExternalStorageDirectory() + "/"
			+ FILENAME);
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		Visitor = getIntent().getExtras().getBoolean("Visitor");
		ParseUser currentUser = ParseUser.getCurrentUser();
		final ParseUser user = new ParseUser();
		if (currentUser == null && Visitor == false) {
			Intent j = new Intent();
			j.setClass(getApplicationContext(), MainActivity.class);
			startActivity(j);
		}
		else if(currentUser == null && Visitor == true){
			setContentView(R.layout.activity_menu_visitor);
			functionnality1(); //MAP
			//functionnality2(); //TEST
			functionnality3(); //calendar
			functionnality6();
		}

		else{

			//This lines allows doing network operations in the main thread 
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			setContentView(R.layout.activity_menu);
			functionnality1(); //MAP
			functionnality2(); //TEST
			functionnality3(); 
			functionnality4(); 
			functionnality5(); 
		}
	}

	
	//MAP
	public void functionnality1(){
		//---- START Functionality for showing the map ----
		Button btnJumpToMap = (Button) findViewById(R.id.btnJumpToF1);
		btnJumpToMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// In this part it's going to load the pdf file
				if (!readPDF(FILENAME)) {
					// If pdf doesn't exist go to internet and get the source
					if (DownloadFile(FILE_URL, FILE)) {
						readPDF(FILENAME);
					} else {
						Toast.makeText(MenuActivity.this, "Application can't download PDF. check your internet connection",
								Toast.LENGTH_SHORT).show();
					}
				}				
			}
		});

		//---- END functionality for showing the map ----

	}
	private boolean readPDF(String filename) {
		// open the pdf
		Log.e("MINE", "File address: " + FILE.toString());
		if (FILE.exists()) {
			Uri path = Uri.fromFile(FILE);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Log.e("MINE", "File exists");
			try {
				startActivity(intent);
				return true;
			} catch (ActivityNotFoundException e) {
				Log.e("MINE", "PDF can be read");
				Toast.makeText(MenuActivity.this,
						"No Application Available to View PDF",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {
			Log.e("MINE", "File doesn�t exists");
			return false;
		}
	}

	public boolean DownloadFile(String url, File outputFile) {
		Toast.makeText(MenuActivity.this,
				"Downloading PDF. Please wait.",
				Toast.LENGTH_SHORT).show();
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			int contentLength = conn.getContentLength();
			DataInputStream stream = new DataInputStream(u.openStream());

			byte[] buffer = new byte[contentLength];
			stream.readFully(buffer);
			stream.close();


			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					outputFile));
			fos.write(buffer);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e("MINE","File not found. "+e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("MINE","IO exception"+e.getMessage());
			return false;
		}
		return true;
	}

	//NOT DETERMINED
	public void functionnality2(){
		Button btnJumpToF2 = (Button) findViewById(R.id.btnJumpToF2);
		btnJumpToF2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), ACourses.class);
				startActivity(i);
			}
		});
	}

	//LOGOUT
	public void functionnality3(){
		Button btnJumpToF3 = (Button) findViewById(R.id.btnJumpToF3);
		btnJumpToF3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), CalendarActivity.class);
				startActivity(i);
			}
		});
	}
	public void functionnality4(){

		Button btnJumpToF4 = (Button) findViewById(R.id.btnJumpToF4);
		btnJumpToF4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), EditProfileActivity.class);
				startActivity(i);
			}
		});
	}

	public void functionnality5(){


		Button btnJumpToF5 = (Button) findViewById(R.id.btnJumpToF5);
		btnJumpToF5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent w = new Intent();
				w.setClass(getApplicationContext(), SearchProfileActivity.class);
				startActivity(w);
			}
		});
	}
	
	public void functionnality6(){


		Button btnJumpToAbout = (Button) findViewById(R.id.btnJumpToAbout);
		btnJumpToAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(MenuActivity.this);
				dialog.setContentView(R.layout.popup_about);
	           
	            // Setting Dialog Title
	           dialog.setTitle("University Application");
	 
	            // Setting Dialog Message
	          
	 
	            // Setting Icon to Dialog
	            //((AlertDialog) dialog).setIcon(R.drawable.ic_launcher);
	 
	            // Setting OK Button
	            
	 
	            // Showing Alert Message
	            dialog.show();
			}
		});
	}
	
//	
//	public void setmenusettings(){
//onCreateOptionsMenu(menu);
//	}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    if(!Visitor){
		    	MenuInflater inflater = getMenuInflater();
		    
		    inflater.inflate(R.menu.menu, menu);
		    }
		    return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (item.getItemId() == R.id.action_logout) {
				ParseUser.logOut();
				ParseUser currentUser = ParseUser.getCurrentUser();
				Intent w = new Intent();
				w.setClass(getApplicationContext(), MainActivity.class);
				startActivity(w);
			}
			if(item.getItemId() == R.id.action_about){
				// Creating alert Dialog with one Button
				 
				final Dialog dialog = new Dialog(MenuActivity.this);
				dialog.setContentView(R.layout.popup_about);
	           
	            // Setting Dialog Title
	           dialog.setTitle("University Application");
	 
	            // Setting Dialog Message
	          
	 
	            // Setting Icon to Dialog
	            //((AlertDialog) dialog).setIcon(R.drawable.ic_launcher);
	 
	            // Setting OK Button
	            
	 
	            // Showing Alert Message
	            dialog.show();
			}
		    return super.onOptionsItemSelected(item);
		}
	}
	








