package edition.one.epoint.pager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import edition.one.epoint.BeforeLoginActivity;
import edition.one.epoint.R;
import edition.one.epoint.method.QRcodeCreate;

public class QRcodePager extends Fragment {
	public static final String DEFAULT ="N/A";
	String cusindex = "No Data !!!!!";
	String customerid = "No ID !!!!!";
	private FrameLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_one, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViewComponent();
	}
	
	private void setupViewComponent() {
		// TODO Auto-generated method stub
//		System.out.println(" \n" + "\ncusindex 是 " + cusindex + ",\ncustomerid 是 " + customerid + ",\n在 QRcodePager.class LoadQrInfo() 之前");
		LoadQrInfo();
//		System.out.println(" \n" + "\ncusindex 是 " + cusindex + ",\ncustomerid 是 " + customerid + ",\n在 QRcodePager.class LoadQrInfo() 之後");
		
		layout = (FrameLayout) getView().findViewById(R.id.lay);
		((TextView) getView().findViewById(R.id.textView)).setText("QRcode");
		View view = QRcodeCreate.setQRCodeCompoment(getActivity(), cusindex, customerid);
		layout.addView(view);
	}
	
	public void LoadQrInfo() {
		SharedPreferences sharedPreferences= getActivity().getSharedPreferences("MyIdPs", Context.MODE_PRIVATE);
		//使用 getString() 從 SharedPreferences 抓取相對的值！
		//getString("要抓取值的標頭", 找不到此標頭時給予的值)
		String getCusin = sharedPreferences.getString("consindex", DEFAULT);
		String getCust = sharedPreferences.getString("customerid", DEFAULT);
//		System.out.println(" \n" + "\n．getCusin 是 " + getCusin + ",\n．getCust 是 " + getCust + ",\n在 QRcodePager.class LoadQrInfo() 之中");
//		System.out.println(" \n" + "\ncusindex 是 " + cusindex + ",\ncustomerid 是 " + customerid + ",\n在 QRcodePager.class LoadQrInfo() 之中 else{} 之前");
		if (getCusin.equals(DEFAULT) || getCust.equals(DEFAULT)) {
			UseToast("No data was found !!!");
			ChangeView();
			getActivity().finish();
		} else{
			UseToast("Data load successfully !!!");
			cusindex = getCusin;
			customerid = getCust;
//			System.out.println(" \n" + "\ncusindex 是 " + cusindex + ",\ncustomerid 是 " + customerid + ",\n在 QRcodePager.class LoadQrInfo() 之中 else{} 之後");
		}
		
	}
	
    private void UseToast(String message) {
		Toast toast = Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
		toast.show();
	}
    
    private void ChangeView() {
		Intent it = new Intent();
		it.setClass(getActivity(), BeforeLoginActivity.class);
		startActivity(it);
	}

}
