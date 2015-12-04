package aau.dk.fragmentprofile;
import com.parse.Parse;
import com.parse.ParseUser;

import aau.dk.universityapplication.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;

public class EditProfileActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
       
        Parse.initialize(this,
                "[APPLICATION ID]",
                "[CLIENT ID]");
		
        EditProfileFragmentAdapter adapter =
        		new EditProfileFragmentAdapter(getSupportFragmentManager());
        
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);
       
        
        //p.setCurrentItem(EditProfileFragmentAdapter.NUM_ITEMS-1);
    }

}
