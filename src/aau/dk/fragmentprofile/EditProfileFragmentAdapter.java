package aau.dk.fragmentprofile;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EditProfileFragmentAdapter extends FragmentPagerAdapter {

	public static final int NUM_ITEMS = 2;
	
	private ArrayList<Fragment> fragments;
	
	public EditProfileFragmentAdapter(FragmentManager fm) {
		super(fm);
		
		fragments = new ArrayList<Fragment>();
		fragments.add(new EditProfileFragmentPicture());
		fragments.add(new EditProfileFragmentInfo());
	}

	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		return NUM_ITEMS;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return EditProfileFragmentPicture.TAG;
		case 1:
			return EditProfileFragmentInfo.TAG;
		default:
			return "unknown";
		}
	}
}
