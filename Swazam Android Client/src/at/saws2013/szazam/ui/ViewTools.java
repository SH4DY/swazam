package at.saws2013.szazam.ui;

import android.view.View;

public class ViewTools {
	
	public static void disableViews(View... views){
		for (View view : views) {
			view.setEnabled(false);
		}
	}
	
	public static void enableViews(View... views){
		for (View view : views) {
			view.setEnabled(true);
		}
	}

}
