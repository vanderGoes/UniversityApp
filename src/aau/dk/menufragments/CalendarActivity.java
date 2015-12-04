package aau.dk.menufragments;


import java.util.TimeZone;

import aau.dk.menufragments.calendar.CalendarListFragment;
import aau.dk.menufragments.calendar.DatePickerDialogFragment;
import aau.dk.menufragments.calendar.EventCreateFragment;
import aau.dk.menufragments.calendar.CalendarListFragment.IEventListFragment;
import aau.dk.menufragments.calendar.EventCreateFragment.IEventCreateFragment;
import aau.dk.pojo.Event;
import aau.dk.universityapplication.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CalendarActivity extends FragmentActivity implements IEventListFragment{
	private ViewGroup fragmentContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_calendar);
		
		//Load the fragment container with all activities:
		fragmentContainer = (ViewGroup) findViewById(R.id.FragmentContainer);
		
	}
	
	// ITodoListFragment
		public void onEventSelected(Event selectedTodo) {
			//TODO: This part implements a fragment with the functionality to show details of the selected event.
			
//			if (fragmentContainer != null) {
//				FragmentManager fm = getSupportFragmentManager();
//
//				FragmentTransaction ft = fm.beginTransaction();
//				ft.replace(R.id.FragmentContainer, EventDetailsFragment
//						.newInstance(selectedTodo.getDescription()));
//				ft.commit();
//			} else {
//				Intent i = new Intent(this, ActivityEventDetails.class);
//				i.putExtra(EventDetailsFragment.KEY_TODO_DESCRIPTION,
//						selectedTodo.getDescription());
//				startActivity(i);
//			}
		}
	
}