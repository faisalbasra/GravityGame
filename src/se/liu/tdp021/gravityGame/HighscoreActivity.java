package se.liu.tdp021.gravityGame;

import java.util.ArrayList;
import java.util.HashMap;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;

public class HighscoreActivity extends Activity {
	protected static String TAG = "HighscoreActivity";
	private HighscoreDbAdapter highscore;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.highscore);
        
        highscore = new HighscoreDbAdapter(this);
        highscore.open();
        
		final ListView lv1 = (ListView) findViewById(R.id.highscores_listview);
		
		LevelSelector levelSelector = new LevelSelector();
		final ArrayList<HashMap<String,String>> levels = levelSelector.getLevelList(getResources());

		lv1.setAdapter(new HighscoreAdapter(this, levels));
		
		//display highscores for select level
		lv1.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	
		    	Bundle parameters = new Bundle();
        		Intent i = new Intent(HighscoreActivity.this, HighscoreLevelActivity.class);
        		parameters.putString("level_id", levels.get(position).get("data")); 
        		parameters.putString("startedFrom", "HighscoreActivity");
            	i.putExtras(parameters);
        		startActivity(i);
        		finish();
		    }
		 });
		
		//go back to main menu
		final Button button = (Button) findViewById(R.id.back_to_menu);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(HighscoreActivity.this, MainMenu.class);
            	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(i);
            }
        });
    }
    
    
    public ArrayList<HashMap<String, String>> getHighscores() {
    	ArrayList<HashMap<String, String>> highscores = new ArrayList<HashMap<String, String>>();
    	for (int i = 0; i < 10; i++) {
    		HashMap<String, String> highscore = new HashMap<String, String>();
    		highscore.put("name", "Level " + i);
    		highscore.put("time", "Best Time: "+(16*i) + " seconds!");
    		highscores.add(highscore);
    	}
    	
    	return highscores;
    }
 
    public class HighscoreAdapter extends BaseAdapter {
    	private LayoutInflater inflater;
    	
    	private ArrayList<HashMap<String,String>> highscoreLevels = null;
    	
    	public HighscoreAdapter(Context context, ArrayList<HashMap<String,String>> levels) {
    		 inflater = LayoutInflater.from(context);
    		 highscoreLevels = levels;
    	}
    	
    	public long getItemId(int position) {
    		  return position;
    	}
    	
    	public int getCount() {
    		return highscoreLevels.size();
    	}
    	
    	public Object getItem(int position) {
    		  return highscoreLevels.get(position);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		if (convertView == null) {
    			convertView = inflater.inflate(R.layout.highscore_row, null);

    		}
    		
    		TextView nameView = (TextView) convertView.findViewById(R.id.highscore_level_name);
    		String levelname = highscoreLevels.get(position).get("name");
    		nameView.setText(levelname);
    		
    		String text;
    		TextView scoreView = (TextView) convertView.findViewById(R.id.record_time);
    		
    		String level_id = highscoreLevels.get(position).get("data");
    		
    		
    		HashMap<String,String> levelHighscore = highscore.levelHighscore(level_id);
    		if(levelHighscore.size() > 0) {
    			text = getString(R.string.highscore_username) + " " + 
    				levelHighscore.get(HighscoreDbAdapter.KEY_USERNAME) + " " +
    				getString(R.string.highscore_collectibles) + " " + 
    				levelHighscore.get(HighscoreDbAdapter.KEY_COLLECTIBLES) + " " + 
    				getString(R.string.highscore_flips) + " " +
    				levelHighscore.get(HighscoreDbAdapter.KEY_FLIPS);
    			Log.v("asd: ",level_id);
    		} else {
    			text = getString(R.string.highscore_noscore);
    		}

    		scoreView.setText(text);
    		
    		return convertView;
    	}
    }
    
}
