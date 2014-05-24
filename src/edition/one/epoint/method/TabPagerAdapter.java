package edition.one.epoint.method;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import edition.one.epoint.pager.OthersPager;
import edition.one.epoint.pager.PointPager;
import edition.one.epoint.pager.QRcodePager;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	
	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
        case 0:
            //Fragment for Android Tab
            return new QRcodePager();
        case 1:
           //Fragment for iOS Tab
            return new PointPager();
        case 2:
            //Fragment for Windows Tab
            return new OthersPager();
        case 3:
            //Fragment for Windows Tab
            return new OthersPager();
        case 4:
            //Fragment for Windows Tab
            return new OthersPager();
        case 5:
            //Fragment for Windows Tab
            return new OthersPager();
        }
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6; //Numbers of Tabs
	}


    }