package aau.dk.menufragments;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import aau.dk.universityapplication.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		
		String MyFile ="my_file.pdf";
		try {
			FileOutputStream out = openFileOutput(MyFile, MODE_PRIVATE);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{Log.e("MINE","File not found");}
				
		
	}
}

