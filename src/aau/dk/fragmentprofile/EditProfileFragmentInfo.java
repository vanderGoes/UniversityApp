package aau.dk.fragmentprofile;

import aau.dk.login.MainActivity;
import aau.dk.login.SignUpActivity;
import aau.dk.universityapplication.R;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EditProfileFragmentInfo extends Fragment {
	protected ProgressDialog progressDialog;
	private String profileID = null;
	public static final String TAG = "Profile Information";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// create your view using LayoutInflater
		return inflater.inflate(R.layout.activity_edit_profile_fragment_info,
				container, false);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// do your variables initialisations here except Views!!!

	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// initialise your views

		final EditText etUsername = (EditText) getView().findViewById(
				R.id.etUsername);
		final EditText etEmail = (EditText) getView()
				.findViewById(R.id.etEmail);
		final EditText etFirstname = (EditText) getView().findViewById(
				R.id.etFirstname);
		final EditText etLastname = (EditText) getView().findViewById(
				R.id.etLastname);
		final EditText etUsertype = (EditText) getView().findViewById(
				R.id.etUsertype);
		final Spinner spUniversity = (Spinner) getView().findViewById(
				R.id.spUniversity);
		final Spinner spStudy = (Spinner) getView().findViewById(R.id.spStudy);

		final List<String> liUniversity = new ArrayList<String>();
		final List<String> liUniversityID = new ArrayList<String>();
		final List<String> liStudy = new ArrayList<String>();
		final List<String> liStudyID = new ArrayList<String>();

		final EditText etPassword = (EditText) getView().findViewById(R.id.etPassword);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			profileID = currentUser.getObjectId();
		} else {
			Toast.makeText(getActivity().getApplicationContext(),
					"Error Current User", Toast.LENGTH_LONG).show();
		}

		// addItemsOnSpinnerUniversity();
		final ArrayAdapter<String> dataAdapterUniversity = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				liUniversity);
		dataAdapterUniversity
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spUniversity.setAdapter(dataAdapterUniversity);

		ParseQuery<ParseObject> queryU = ParseQuery.getQuery("University");
		queryU.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (int i = 0; i < objects.size(); i++) {
						liUniversity.add(objects.get(i).get("name").toString());
						liUniversityID.add(objects.get(i).getObjectId()
								.toString());
						dataAdapterUniversity.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error loading universities", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// addItemsOnSpinnerStudy();
		final ArrayAdapter<String> dataAdapterStudy = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, liStudy);
		dataAdapterStudy
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spStudy.setAdapter(dataAdapterStudy);

		ParseQuery<ParseObject> queryS = ParseQuery.getQuery("Study");
		//queryS.whereEqualTo("universityID",
		//		liUniversityID.get(spUniversity.getSelectedItemPosition()));
		queryS.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objectsS, ParseException e) {
				if (e == null) {
					for (int i = 0; i < objectsS.size(); i++) {
						liStudy.add(objectsS.get(i).get("name").toString());
						liStudyID.add(objectsS.get(i).getObjectId().toString());
						dataAdapterStudy.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error loading studies", Toast.LENGTH_LONG).show();
				}
			}
		});

		// ShowProfile();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		query.getInBackground(profileID, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					etUsername.setText(object.get("username").toString());
					etEmail.setText(object.get("email").toString());
					etFirstname.setText(object.get("firstName").toString());
					etLastname.setText(object.get("lastName").toString());
					etUsertype.setText(object.get("userType").toString());

					ParseQuery<ParseObject> queryU = ParseQuery
							.getQuery("University");
					queryU.getInBackground(object.get("University").toString(),
							new GetCallback<ParseObject>() {
								public void done(ParseObject objectU,
										ParseException e) {
									if (e == null) {
										String UniversityName = (String) objectU
												.get("name").toString();
										if (UniversityName != null) {
											spUniversity.setSelection(liUniversity
													.indexOf(UniversityName));
										} else {
											Toast.makeText(
													getActivity()
															.getApplicationContext(),
													"Please choose an university",
													Toast.LENGTH_SHORT).show();
										}
									} else {
										// something went wrong
									}
								}
							});
					ParseQuery<ParseObject> queryS = ParseQuery
							.getQuery("Study");
					queryS.getInBackground(object.get("Study").toString(),
							new GetCallback<ParseObject>() {
								public void done(ParseObject objectS,
										ParseException e) {
									if (e == null) {
										String StudyName = (String) objectS
												.get("name").toString();
										if (StudyName != null) {
											spStudy.setSelection(liStudy
													.indexOf(StudyName));
										} else {
											Toast.makeText(
													getActivity()
															.getApplicationContext(),
													"Please choose a a study.",
													Toast.LENGTH_SHORT).show();
										}
									} else {
										// something went wrong
									}
								}
							});
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"Error show profile", Toast.LENGTH_LONG).show();
				}
			}
		});

		Button btnUpdateProfile = (Button) getView().findViewById(
				R.id.btnUpdateProfile);
		btnUpdateProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(etUsername.getText().toString())) {
					etUsername.setError("Username cannot be empty!");
				} else if ("".equals(etEmail.getText().toString())) {
					etEmail.setError("E-mail cannot be empty!");
				} else if ("".equals(etFirstname.getText().toString())) {
					etFirstname.setError("Firstname cannot be empty!");
				} else if ("".equals(etLastname.getText().toString())) {
					etLastname.setError("Lastname cannot be empty!");
				} else {
					ParseQuery<ParseObject> queryUp = ParseQuery
							.getQuery("_User");
					queryUp.getInBackground(profileID,
							new GetCallback<ParseObject>() {
								public void done(ParseObject objectUp,
										ParseException e) {
									if (e == null) {
										objectUp.put("username", etUsername
												.getText().toString());
										if (etPassword != null) {
										objectUp.put("password", etPassword
												.getText().toString());
										}
										objectUp.put("email", etEmail.getText()
												.toString());
										objectUp.put("firstName", etFirstname
												.getText().toString());
										objectUp.put("lastName", etLastname
												.getText().toString());
										objectUp.put(
												"University",
												liUniversityID.get(spUniversity
														.getSelectedItemPosition()));
										objectUp.put(
												"Study",
												liStudyID.get(spStudy
														.getSelectedItemPosition()));
										objectUp.saveInBackground();
										Toast.makeText(
												getActivity()
														.getApplicationContext(),
												"Profile updated",
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(
												getActivity()
														.getApplicationContext(),
												"Error update",
												Toast.LENGTH_SHORT).show();
									}
								}

							});

				}
			}
		});

	}
}
