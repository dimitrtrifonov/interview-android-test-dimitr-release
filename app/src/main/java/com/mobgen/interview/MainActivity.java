package com.mobgen.interview;

import android.app.Activity;
import android.os.Bundle;



/**
 * Animation examples:
 * 
 * fadeIn = new AlphaAnimation(0, 1); fadeIn.setInterpolator(new
 * DecelerateInterpolator()); fadeIn.setDuration(300);
 * 
 * fadeOut = new AlphaAnimation(1, 0); fadeOut.setInterpolator(new
 * AccelerateInterpolator()); fadeOut.setDuration(300);
 * */

public class MainActivity extends Activity {

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
	}
}
