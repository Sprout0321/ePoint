package edition.one.epoint.pager;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edition.one.epoint.R;

public class OthersPager extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_one, container, false);
		view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //do something
                	toggleActionBar();
                }
                return true;
            }
		});
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViewComponent();
	}
	private void setupViewComponent() {
		// TODO Auto-generated method stub
		((TextView) getView().findViewById(R.id.textView)).setText("Others");
	}
	
	private void toggleActionBar() {
		ActionBar actionBar = getActivity().getActionBar();

		if (actionBar != null) {
			if (actionBar.isShowing()) {
				actionBar.hide();
			} else {
				actionBar.show();
			}
		}
	}
}
