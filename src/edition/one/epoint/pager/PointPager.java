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

public class PointPager extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_two, container, false);
		view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //do something
//                	toggleActionBar();
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
		TextView textview = (TextView) getView().findViewById(R.id.textView);
		textview.setText("");
		((TextView) getView().findViewById(R.id.textView3)).setText("Your Points 3");
		((TextView) getView().findViewById(R.id.textView5)).setText("Your Points 5");
		((TextView) getView().findViewById(R.id.textView7)).setText("Your Points 7");
		((TextView) getView().findViewById(R.id.textView9)).setText("Your Points 9");
		((TextView) getView().findViewById(R.id.textView11)).setText("Your Points 11");
		((TextView) getView().findViewById(R.id.textView13)).setText("Your Points 13");
		((TextView) getView().findViewById(R.id.textView15)).setText("Your Points 15");
		((TextView) getView().findViewById(R.id.textView17)).setText("Your Points 17");
		((TextView) getView().findViewById(R.id.textView19)).setText("Your Points 19");
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
