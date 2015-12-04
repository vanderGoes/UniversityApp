package aau.dk.menufragments.calendar;

import aau.dk.menufragments.calendar.DatePickerDialogFragment.IDatePickerDialogFragment;
import aau.dk.pojo.Event;
import aau.dk.universityapplication.R;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EventCreateFragment extends DialogFragment implements
		IDatePickerDialogFragment {

	// Log tag
	public static final String TAG = "TodoCreateFragment";

	// UI
	private long eventcounter;
	private EditText editTodoTitle;
	private Spinner spnrTodoPriority;
	private TextView txtDueDate;
	private EditText editTodoDescription;
	private long longDate;
	private DatePickerDialogFragment datePicker;
	// Listener
	private IEventCreateFragment listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (getTargetFragment() != null) {
			try {
				listener = (IEventCreateFragment) getTargetFragment();
			} catch (ClassCastException ce) {
				Log.e(TAG,
						"Target Fragment does not implement fragment interface!");
			} catch (Exception e) {
				Log.e(TAG, "Unhandled exception!");
				e.printStackTrace();
			}
		} else {
			try {
				listener = (IEventCreateFragment) activity;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View root = inflater.inflate(R.layout.activity_calendar_create_event, container, false);
		// Dialog cimenek beallitasa
		getDialog().setTitle(R.string.itemCreateTodo);

		// UI elem referenciak elkerese
		editTodoTitle = (EditText) root.findViewById(R.id.todoTitle);

		spnrTodoPriority = (Spinner) root.findViewById(R.id.todoPriority);
		String[] priorities = new String[3];
		priorities[0] = "Low";
		priorities[1] = "Medium";
		priorities[2] = "High";
		spnrTodoPriority.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, priorities));

		txtDueDate = (TextView) root.findViewById(R.id.todoDueDate);
		txtDueDate.setText("  -  ");
		txtDueDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		editTodoDescription = (EditText) root
				.findViewById(R.id.todoDescription);

		// A gombok esemenykezeloinek beallitasa
		Button btnOk = (Button) root.findViewById(R.id.btnCreateTodo);
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Event.Priority selectedPriority = Event.Priority.LOW;

				switch (spnrTodoPriority.getSelectedItemPosition()) {
				case 0:
					selectedPriority = Event.Priority.LOW;
					break;
				case 1:
					selectedPriority = Event.Priority.MEDIUM;
					break;
				case 2:
					selectedPriority = Event.Priority.HIGH;
					break;
				default:
					break;
				}

				if (listener != null) {
					long eventId=0;
					Event newEvent = new Event(longDate,editTodoTitle.getText()
							.toString(), selectedPriority, txtDueDate.getText()
							.toString(), editTodoDescription.getText()
							.toString(),eventId);
					listener.onEventCreated(newEvent);
				}

				dismiss();
			}
		});

		Button btnCancel = (Button) root.findViewById(R.id.btnCancelCreateTodo);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		return root;
	}

	private void showDatePickerDialog() {
		FragmentManager fm = getFragmentManager();

		datePicker = new DatePickerDialogFragment();
		datePicker.setTargetFragment(this, 0);
		datePicker.show(fm, DatePickerDialogFragment.TAG);
	}

	// IDatePickerDialogFragment
	public void onDateSelected(String date) {
		txtDueDate.setText(date);
		longDate = datePicker.getSelectedDate();
	}

	// Listener interface
	public interface IEventCreateFragment {
		public void onEventCreated(Event newTodo);
	}
}