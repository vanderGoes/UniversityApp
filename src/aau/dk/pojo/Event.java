package aau.dk.pojo;

/**
 * Class with information of events to be included into calendar
 * @author Lizeth
 *
 */
public class Event {

	public enum Priority { LOW, MEDIUM, HIGH }
	
	private long longDate;
	private String title;
	private Priority priority;
	private String dueDate;
	private String description;
	private long eventID;
	
	public Event(long longdate, String aTitle, Priority aPriority, String aDueDate, String aDescription, long event_id)
	{
		longDate = longdate;
		title = aTitle;
		priority = aPriority;
		dueDate = aDueDate;
		description = aDescription;
		eventID = event_id;
	}

	public long getEventId(){
		return eventID;
	}
	public long getLongDate(){
		return longDate;
	}
	public String getTitle() {
		return title;
	}

	public Priority getPriority() {
		return priority;
	}

	public String getDueDate() {
		return dueDate;
	}

	public String getDescription() {
		return description;
	}
	
	public void setEventId(long event_id){
		this.eventID = event_id;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLongDate(long longdate){
		this.longDate=longdate;
	}
	
}
