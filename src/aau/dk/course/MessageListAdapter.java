package aau.dk.course;

import java.util.ArrayList;
import java.util.Date;

import org.apache.http.ParseException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import aau.dk.universityapplication.R;

public class MessageListAdapter extends BaseAdapter {

	private ArrayList<ParseObject> messages;
	private Context context;
	
	public MessageListAdapter(Context context) {
		this.context = context;
		messages = new ArrayList<ParseObject>();
	}
	
	public void addMessage(ParseObject aMess) {
		messages.add(aMess);
	}
	
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView tvProfile;
		TextView tvDate;
		TextView tvMessage;
		ImageView tvImage;
	}
	
	@Override
	public View getView(int position,
			View convertView, ViewGroup parent) {
	  View v = convertView;
	  if(v == null) {
	    LayoutInflater inflater =
	    		LayoutInflater.from(context);
	    v = inflater.inflate(R.layout.message, null);
	    ViewHolder holder = new ViewHolder();
	    holder.tvProfile= (TextView)v.findViewById(
	    		R.id.tvProfile);
	    holder.tvDate = (TextView)v.findViewById(
	    		R.id.tvDate);
	    holder.tvMessage = (TextView)v.findViewById(
	    		R.id.tvMessage);
	    holder.tvImage=(ImageView)v.findViewById(R.id.tvImage);
	    v.setTag(holder);
	  }

	  final ParseObject mes = messages.get(position);
	  
	  if(mes != null) {
		  final ViewHolder holder = (ViewHolder)v.getTag();
	 
			  //Obtenir le username username
	    ParseQuery<ParseUser> query2 = ParseUser.getQuery();
	    query2.whereEqualTo("objectId", mes.getString("profileID"));
	    query2.getFirstInBackground(new GetCallback<ParseUser>() {
			
			@Override
			public void done(ParseUser arg0, com.parse.ParseException arg1) {
				// TODO Auto-generated method stub
			if (arg1==null){
				if (arg0.getString("userType").equals("teacher")){
					holder.tvProfile.setText("TEACHER : "+arg0.getString("username"));
				}
				else holder.tvProfile.setText(arg0.getString("username"));
			}
			else{
				
				Log.d("Log","Failed boss: "+arg1);
				System.out.println(arg1.getCause());	
			}
			}
		});
	   
	    //Traitement de la date
	    if (mes.getCreatedAt()==null){
	    	holder.tvDate.setText("-");
	    }
	    else
	    {Date today=new Date();
	    Date d=mes.getCreatedAt();
	    if ((d.getDate()==today.getDate())&&(d.getMonth()==today.getMonth())){
	    	 holder.tvDate.setText("today "+d.getHours()+":"+d.getMinutes());
	    }
	    else{
	    	holder.tvDate.setText(d.getDate()+"/"+(d.getMonth()+1));
	    }
	    }
	    //Message
	    String s=mes.getString("messageText");
	    	    
	    	  
	   if (s==null){holder.tvMessage.setText(mes.getString("-"));} 
	   else{holder.tvMessage.setText(mes.getString("messageText"));
	   }
	    
	    //Photo de l'utilisateur
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("userID", mes.get("profileID"));
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject arg0, com.parse.ParseException arg1) {
				// TODO Auto-generated method stub
 if (arg1 == null) {
	 if (arg0.get("photo")!=null){
			    	ParseFile imageFile2 = (ParseFile)arg0.get("photo");
    imageFile2.getDataInBackground(new GetDataCallback() {
	
	@Override
	public void done(byte[] data, com.parse.ParseException e) {
		// TODO Auto-generated method stub
		
  	if (data != null) {
        // Success; data has the file
  		Bitmap b=BitmapFactory.decodeByteArray(data, 0, data .length);
  		if (b==null){
  	    	Toast.makeText(context,"Problème de chargement de l'image", Toast.LENGTH_LONG).show();
  		}
  		else {
  			holder.tvImage.setImageBitmap(b);
      }
  		} 
}
	    });}
			    }
			    
			}
		});
		//Fin query photo
		
	  }
		
	  return v;
	}
}


	