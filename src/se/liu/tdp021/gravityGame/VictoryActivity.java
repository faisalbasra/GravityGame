package se.liu.tdp021.gravityGame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;


public class VictoryActivity extends Activity{

	private VictoryView victoryView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.victory);
		
		victoryView = (VictoryView) findViewById(R.id.victoryview);
	}
	
	@Override 
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(VictoryActivity.this, LevelSelectorActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			try {
				finalize();
			} catch (Throwable e) {
			}
		}
		
		return false;
	}
	
}
