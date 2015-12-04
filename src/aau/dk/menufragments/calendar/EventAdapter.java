package aau.dk.menufragments.calendar;

import java.util.ArrayList;
import java.util.List;

import aau.dk.pojo.Event;
import aau.dk.universityapplication.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {

    private final List<Event> events;

    public EventAdapter(final Context context, final ArrayList<Event> aTodos) {
    	events = aTodos;
    }
    
    public void addItem(Event aTodo)
    {
    	events.add(aTodo);
    }

    public int getCount() {
        return events.size();
    }

    public Object getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * Sor megjelenen�t�s�nek be�ll�t�sa
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        final Event event = events.get(position);

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
    		Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.activity_calendar_event_row, null);

        ImageView imageViewIcon = (ImageView) itemView.findViewById(R.id.imageViewPriority);
        switch (event.getPriority()) {
			case LOW:
				imageViewIcon.setImageResource(R.drawable.low);
				break;
			case MEDIUM:
				imageViewIcon.setImageResource(R.drawable.medium);
				break;
			case HIGH:
				imageViewIcon.setImageResource(R.drawable.high);
				break;
			default:
				imageViewIcon.setImageResource(R.drawable.high);
				break;
		}
        
        TextView textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
        textViewTitle.setText(event.getTitle());
        
        TextView textViewDueDate = (TextView) itemView.findViewById(R.id.textViewDueDate);
        textViewDueDate.setText(event.getDueDate());
        
        return itemView;
    }

    /**
     * Egye elem t�rl�se
     */
    public void deleteRow(Event aTodo) {
        if(events.contains(aTodo)) {
        	events.remove(aTodo);
        }
    }
}
