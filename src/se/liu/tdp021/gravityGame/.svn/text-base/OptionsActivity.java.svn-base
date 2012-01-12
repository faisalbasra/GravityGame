package se.liu.tdp021.gravityGame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class OptionsActivity extends Activity {
	
	SharedPreferences settings = null;
	boolean tilt;
	boolean autoPause;
	int delay;
	
    /** Called when the activity is first created. */
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        settings = getPreferences(0);
        tilt = settings.getBoolean("tiltmode", false);
        autoPause = settings.getBoolean("autoPause-mode", false);
        delay = settings.getInt("delay", 1);
        
        setContentView(R.layout.options);
        
        final CheckBox checkbox_tilt = (CheckBox) findViewById(R.id.checkbox_tilt);
        final CheckBox checkbox_pause = (CheckBox) findViewById(R.id.checkbox_pause);
        final Spinner spinnerDelay = (Spinner) findViewById(R.id.spinner1);        
        checkbox_tilt.setChecked(tilt);
        checkbox_pause.setChecked(autoPause);
        
        //TODO: Fix this crap; we need to create an ArrayAdapter that goes with the spinner, or hax an index position.
        //  http://developer.android.com/resources/tutorials/views/hello-spinner.html
        //  http://thinkandroid.wordpress.com/2010/01/13/retrieving-spinner-values/
        //ArrayAdapter adapter = new ArrayAdapter();
        //spinnerDelay.setSelection(adapter.getPosition(Integer.toString(delay))));


        
        
        checkbox_tilt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
                // Perform action on clicks, depending on whether it's now checked
            	if (((CheckBox) v).isChecked()) {
            		tilt = true;
            	} else {
            		tilt = false;
            	}
            	
            }
        });
          checkbox_pause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()){
					autoPause = true;
					
				}
				else{
					autoPause = false;
				}
				
			}
		});      
     spinnerDelay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			//delay = (Integer) parent.getItemAtPosition(pos);
			delay = Integer.parseInt((String)parent.getItemAtPosition(pos));
			
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
    	 
	});
        
    }
    @Override
    protected void onStop(){
       super.onStop();
       
       Log.v("Options", ("tilt = " + tilt));
       Log.v("Options", ("autoPause = " + autoPause));
       Log.v("Options", ("delay = " + delay));
       SharedPreferences.Editor editor = settings.edit();

       editor.putBoolean("autoPause-mode", autoPause);
       editor.putBoolean("tiltmode", tilt);
       editor.putInt("delay", delay);

       // Commit the edits!
       editor.commit();

    
    }
    
    
    
    
    
}
