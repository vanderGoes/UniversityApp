package aau.dk.course;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.SendCallback;
import aau.dk.universityapplication.R;

public class ChatActivity extends Activity {
	static protected MessageListAdapter me;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		
		
		setContentView(R.layout.activitychat);
		Parse.initialize(this,
				"[APPLICATION ID]",
				"[CLIENT ID]");
		final String profileID=intent.getStringExtra("profileID");
		//Toast.makeText(ChatActivity.this,
   			//  profileID, Toast.LENGTH_LONG).show();
		final String courseID=intent.getStringExtra("courseID");
		//Toast.makeText(ChatActivity.this,
	   		//	  courseID, Toast.LENGTH_LONG).show();
		//final String courseID="AApMnb8Cfa";
		//final String courseChannel=courseID;
		
		//I suscribe to the channel in order 
		//to receive push notification when somebody write in the chat
		//PushService.subscribe(getApplicationContext(), courseChannel, ChatActivity.class);
		//PushService.setDefaultPushCallback(this, ChatActivity.class);
		
		
		me=new MessageListAdapter(getApplicationContext());
		ListView l=(ListView)findViewById(R.id.in);
		l.setAdapter(me);
		
		affiche(courseID);
		
		Button btn=(Button) findViewById(R.id.button_send);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				
			EditText et=(EditText)findViewById(R.id.edit_text_out);
			String s=et.getText().toString();
			
			ParseObject o=new ParseObject("Message");
			
			o.put("profileID", profileID);
			
			o.put("courseID", courseID);
			o.put("messageText", s);
			me.addMessage(o);
			o.saveInBackground();
			
			/*JSONObject object = new JSONObject();
	        try {
	        	ParseInstallation installation = ParseInstallation.getCurrentInstallation();
	        	String id=installation.getInstallationId();
	        	object.put("alert","Chat");
	        	object.put("title", s);
	                    object.put("profileID", profileID);
	                    object.put("courseID", courseID);
	                       object.put("messageText", s);
	                    object.put("action", "c");                   
	                   
	                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
	                    pushQuery.whereNotEqualTo("installationId", id);
	                   
	                    ParsePush pushMessageClient1 = new ParsePush();
	                    pushMessageClient1.setData(object);
	                    pushMessageClient1.setChannel(courseChannel);
	                    pushMessageClient1.setQuery(pushQuery);
	                    pushMessageClient1.sendInBackground(new SendCallback() {
	           
							@Override
							public void done(com.parse.ParseException arg0) {
								// TODO Auto-generated method stub
					         	
								if (arg0 != null){
									//Something wrong 
									Toast.makeText(ChatActivity.this,
					     			  "An error appear", Toast.LENGTH_LONG).show();
								}

	                        }
								
							
						});
	            } catch (JSONException e) {
	            	e.printStackTrace();
	            }*/
		}
		
	});
			
	}
	
	public void addMessage(String messageID,String message,String courseID,String profileID,
			int type){
		final ParseObject mes = new ParseObject("Message");
		mes.put("messageID", messageID);
		mes.put("messageText", message);
		mes.put("courseID", courseID);
		mes.put("profileID", profileID);
		mes.put("typeMessage", type);
		//mes.put("createdAt", new Date());
		// profile id type, date ,time, courseID
		mes.saveInBackground();
		}
	
	public void trier(List<ParseObject> liste){
		Collections.sort(liste, new Comparator<ParseObject>() {
		public int compare(ParseObject o1,ParseObject o2){
			Date d1=o1.getCreatedAt();
			Date d2=o2.getCreatedAt();
			return d1.compareTo(d2);
		}
		});
	}
	
	public void affiche(String courseID){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
		query.whereEqualTo("courseID", courseID);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0,
					com.parse.ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg1==null){
					
					trier(arg0);
					
				if (arg0.isEmpty()){
					Toast.makeText(ChatActivity.this,
		     			  "No message", Toast.LENGTH_LONG).show();
					}
					Iterator i=arg0.iterator();
				while (i.hasNext()){
					
					ParseObject x=(ParseObject)i.next();
					
					me.addMessage(x);
					
					me.notifyDataSetChanged();
					}
					
				}
				
			}
		
		});
	
		
		
	}
	

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
