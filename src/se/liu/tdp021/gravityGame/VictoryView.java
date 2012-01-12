package se.liu.tdp021.gravityGame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class VictoryView extends View {
	protected static String TAG = "VictoryActivity";
	
	public VictoryView(Context context, AttributeSet attrSet) {
		super(context, attrSet);
		
	}
	
	public void showMessage() {
		Log.v(TAG, "JKJHILKJ");
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		showMessage();
	}
	
	
}
