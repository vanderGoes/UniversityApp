package aau.dk.menufragments.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
/**
 * This class is used to select the date of a event and then to add to calendar.
 * @author Lizeth
 *
 */
public class DatePickerDialogFragment extends DialogFragment {

	// Log tag
	public static final String TAG = "DatePickerDialogFragment";

	// State
	private Calendar calSelectedDate = Calendar.getInstance();

	// Listener
	private IDatePickerDialogFragment listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (getTargetFragment() != null) {
			try {
				listener = (IDatePickerDialogFragment) getTargetFragment();
			} catch (ClassCastException ce) {
				Log.e(TAG,
						"Target Fragment does not implement fragment interface!");
			} catch (Exception e) {
				Log.e(TAG, "Unhandled exception!");
				e.printStackTrace();
			}
		} else {
			try {
				Log.d("WHERE", "THIS IS THE OK OF THE BUTTON ");
				listener = (IDatePickerDialogFragment) activity;
				Log.d("WHERE", "IT PASS ----THIS IS THE OK OF THE BUTTON ");
				/*
				 * 
				 				ContentResolver cr = getContentResolver();
				ContentValues values = new ContentValues();
				values.put(CalendarContract.Events.DTSTART,
						System.currentTimeMillis());
				values.put(CalendarContract.Events.DTEND,
						System.currentTimeMillis() + 60000);
				values.put(CalendarContract.Events.TITLE, "Best Giga party !!!");
				values.put(CalendarContract.Events.DESCRIPTION, "Demostration 2");
				values.put(CalendarContract.Events.CALENDAR_ID, 1);
				values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone
						.getDefault().getID());
				Uri uri = cr
						.insert(CalendarContract.Events.CONTENT_URI, values);
				// getContentResolver().delete(CalendarContract.Events.CONTENT_URI,
				// CalendarContract.Events._ID+"=599", null);
				Log.d("KEY_LOG", uri.toString());
				 * */
			} catch (ClassCastException ce) {
				Log.e(TAG,
						"Parent Activity does not implement fragment interface!");
			} catch (Exception e) {
				Log.e(TAG, "Unhandled exception!");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		calSelectedDate.setTime(new Date(System.currentTimeMillis()));
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new DatePickerDialog(getActivity(), mDateSetListener,
				calSelectedDate.get(Calendar.YEAR),
				calSelectedDate.get(Calendar.MONTH),
				calSelectedDate.get(Calendar.DAY_OF_MONTH));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// uj datum beallitasa
			calSelectedDate.set(Calendar.YEAR, year);
			calSelectedDate.set(Calendar.MONTH, monthOfYear);
			calSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			if (listener != null) {
				listener.onDateSelected(buildDateText());
			}

			dismiss();
		}
	};

	private String buildDateText() {
		StringBuilder dateString = new StringBuilder();
		dateString.append(calSelectedDate.get(Calendar.YEAR));
		dateString.append(". ");
		dateString.append(calSelectedDate.get(Calendar.MONTH) + 1);
		dateString.append(". ");
		dateString.append(calSelectedDate.get(Calendar.DAY_OF_MONTH));
		dateString.append(".");

		return dateString.toString();
	}


	
	public interface IDatePickerDialogFragment {
		public void onDateSelected(String date);
	}
	public long getSelectedDate(){
		return this.calSelectedDate.getTimeInMillis();
	}
	
	
}
