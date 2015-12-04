package aau.dk.menufragments.calendar;

import java.util.ArrayList;
import java.util.TimeZone;

import aau.dk.menufragments.calendar.DatePickerDialogFragment.IDatePickerDialogFragment;
import aau.dk.menufragments.calendar.EventCreateFragment.IEventCreateFragment;
import aau.dk.pojo.Event;
import aau.dk.universityapplication.R;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class CalendarListFragment extends ListFragment implements
IEventCreateFragment, IDatePickerDialogFragment {

	//Private fragment needed
	private DatePickerDialogFragment dateFragment ;

	// Log tag
	public static final String TAG = "CalendarListFragment";

	// State
	private EventAdapter adapter;

	// Listener
	private IEventListFragment listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("WHERE ", this.getClass().getName());
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_calendar, menu);
	}

	@Override
	public void onEventCreated(Event newEvent) {
		// TODO Auto-generated method stub
		adapter.addItem(newEvent);
		adapter.notifyDataSetChanged();


		ContentResolver cr = getActivity().getContentResolver();
		ContentValues values = new ContentValues();
		values.put(CalendarContract.Events.DTSTART, newEvent.getLongDate());
		values.put(CalendarContract.Events.DTEND, newEvent.getLongDate()+60000);
		values.put(CalendarContract.Events.TITLE, newEvent.getTitle());
		values.put(CalendarContract.Events.DESCRIPTION, newEvent.getDescription());
		values.put(CalendarContract.Events.CALENDAR_ID, 1);
		values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
		Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
		newEvent.setEventId(Long.parseLong(uri.getLastPathSegment()));

	}

	@Override
	public void onStart() {
		super.onStart();

		// TODO: storage in the db or internal memory of the phone
		// Fill the first 3 elements for default
		ArrayList<Event> events = new ArrayList<Event>();
		/*events.add(new Event("Meeting at cine", Priority.LOW, "2013. 09. 26.",
				"description1 Meeting at cine"));
		events.add(new Event("Labwork", Priority.MEDIUM, "2013. 09. 27.",
				"description2 labwork"));
		events.add(new Event("Labwork 2", Priority.HIGH, "2013. 09. 28.",
				"description3 labwork 2"));*/

		adapter = new EventAdapter(getActivity(), events);
		setListAdapter(adapter);

		registerForContextMenu(getListView());
	}

	/**
	 * fill the menu with the 3 elements
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.equals(getListView())) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(((Event) getListAdapter()
					.getItem(info.position)).getTitle());
			String[] menuItems = getResources().getStringArray(
					R.array.eventMenu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		if (menuItemIndex == 0) {// option delete of the menu
			
			DeleteCalendarEntry(((Event) getListAdapter().getItem(info.position)).getEventId());
			((EventAdapter) getListAdapter())
			.deleteRow((Event) getListAdapter().getItem(info.position));
			((EventAdapter) getListAdapter()).notifyDataSetChanged();
		}

		return true;
	}

	private void DeleteCalendarEntry( long entryID) {
		Uri deleteUri = null;
		deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, entryID);
		int rows = getActivity().getContentResolver().delete(deleteUri, null, null);
		Log.i(TAG, "Rows deleted :" + rows);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_createEvent) {
			EventCreateFragment createFragment = new EventCreateFragment();
			createFragment.setTargetFragment(this, 0);

			FragmentManager fm = getFragmentManager();
			createFragment.show(fm, EventCreateFragment.TAG);
		}
		if(item.getItemId() == R.id.menu_goCalendar){
			// A date-time specified in milliseconds since the epoch.
			dateFragment = new DatePickerDialogFragment();
			dateFragment.setTargetFragment(this, 0);
			FragmentManager fm = getFragmentManager();
			dateFragment.show(fm, DatePickerDialogFragment.TAG);
		}
		return super.onOptionsItemSelected(item);
	}



	public interface IEventListFragment {
		public void onEventSelected(Event selecedEvent);
	}

	@Override
	public void onDateSelected(String date) {
		long startTime = dateFragment.getSelectedDate();
		Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
		builder.appendPath("time");
		ContentUris.appendId(builder, startTime);
		Intent intent = new Intent(Intent.ACTION_VIEW)
		.setData(builder.build());
		startActivity(intent);		
	}

}
