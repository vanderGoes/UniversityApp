package aau.dk.course;

import java.util.ArrayList;

import com.parse.Parse;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CourseFA extends FragmentPagerAdapter {

	public static final int NUM_ITEMS = 3;
	
	private ArrayList<Fragment> fragments;
	
	public CourseFA(FragmentManager fm) {
		super(fm);
		
		fragments = new ArrayList<Fragment>();
		fragments.add(new AMyCourseList());
		fragments.add(new ACourseList());
		fragments.add(new ACourseCreate());
		
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
			return AMyCourseList.TAG;
		case 1:
			return ACourseList.TAG;
		case 2:
			return ACourseCreate.TAG;
		default:
			return "unknown";
		}
	}
}
