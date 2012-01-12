package se.liu.tdp021.gravityGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class HighscoreLevelActivity extends Activity {
	protected static String TAG = "HighscoreLevelActivity";
	private int username_id;
	private Cursor cHighscores;
	
	//row id, for added highscore
	private long highscorePosition = -1;
	
	private String startedFromActivity = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HighscoreDbAdapter highscore = new HighscoreDbAdapter(getBaseContext());
        highscore.open();
        
        
        setContentView(R.layout.highscore);
        Bundle parameters = getIntent().getExtras();
        String level_id = parameters.getString("level_id");
        
        // To decide where to go if this activity is finished, we need to know where we came from.
        startedFromActivity = parameters.getString("startedFrom");
        
    	//save highscore, if came from a game
    	if(parameters.containsKey("username") 
    			&& parameters.containsKey("level_id")) {
    		
        	String username = parameters.getString("username");

			if(highscore.usernameExist(username))
        		username_id = highscore.usernameToId(username);
        	else {
        		highscore.addUsername(username);
        		username_id = highscore.usernameToId(username);
        	}
        	
			int flips = parameters.getInt("flips");
        	int collectedTokens = parameters.getInt("collectedTokens");
    		
        	highscorePosition = highscore.addHighscore(level_id,
        												username_id,flips,collectedTokens);
    	}
    
    	final Button button = (Button) findViewById(R.id.back_to_menu);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finishActivity();
            }
        });
        
		final ListView lv1 = (ListView) findViewById(R.id.highscores_listview);

		cHighscores = highscore.levelHighscores(level_id);
		startManagingCursor(cHighscores);
		String[] from = {HighscoreDbAdapter.KEY_USERNAME,HighscoreDbAdapter.KEY_FLIPS,
				HighscoreDbAdapter.KEY_COLLECTIBLES};
		int[] to = {R.id.username,R.id.flips,R.id.collectibles};
		
		
		
		SimpleCursorAdapter notes =
	            new HighscoreCursorAdapter(this, R.layout.highscorelevel_row, cHighscores, from, to);
		lv1.setAdapter(notes);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishActivity() {
    	// If we don't know where to go, default to Main Menu
    	Intent i = new Intent(HighscoreLevelActivity.this, MainMenu.class);
    	
    	if (startedFromActivity != null) {
    		if (startedFromActivity.equals("GameplayActivity")) {
    			i = new Intent(HighscoreLevelActivity.this, LevelSelectorActivity.class);
    		} else if (startedFromActivity.equals("HighscoreActivity")) {
    			i = new Intent(HighscoreLevelActivity.this, HighscoreActivity.class);
    		}
    	} 
    	startActivity(i);
    	finish();
    }
    
    public class HighscoreCursorAdapter extends SimpleCursorAdapter {

        private Cursor c;
        private Context context;
        private int color_new_highscore_row;
        private int color_highscore_row;

		public HighscoreCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			this.c = c;
			this.context = context;
			
			//colors used for rows in listview
			Resources res = getResources();
			color_new_highscore_row = res.getColor(R.color.new_highscore_row);
			color_highscore_row = res.getColor(R.color.highscore_row);
		}
	
		public View getView(int pos, View inView, ViewGroup parent) {
		       View v = inView;
		       if (v == null) {
		            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            v = inflater.inflate(R.layout.highscorelevel_row, null);
		       }
		       this.c.moveToPosition(pos);		
		       
		       //highlight recently added highscore
		       if(c.getLong(c.getColumnIndex("_id")) == highscorePosition) {
		    	   v.setBackgroundColor(color_new_highscore_row);
		       } else {
		    	   v.setBackgroundColor(color_highscore_row);
		       }

		       TextView username = (TextView) v.findViewById(R.id.username);
		       username.setText(pos+1 + ". " + getString(R.string.highscore_username) + " " + 
		    		   this.c.getString(this.c.getColumnIndex(HighscoreDbAdapter.KEY_USERNAME)));
		       
		       TextView flips = (TextView) v.findViewById(R.id.flips);
		       flips.setText(getString(R.string.highscore_flips) + " " + 
		    		   this.c.getString(this.c.getColumnIndex(HighscoreDbAdapter.KEY_FLIPS)));
		       
		       TextView collectibles = (TextView) v.findViewById(R.id.collectibles);
		       collectibles.setText(getString(R.string.highscore_collectibles) + " " + 
		    		   this.c.getString(this.c.getColumnIndex(HighscoreDbAdapter.KEY_COLLECTIBLES)));
		      
		       return(v);
		}
    }


}
