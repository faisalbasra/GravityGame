package se.liu.tdp021.gravityGame;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LevelSelectorActivity extends Activity {
	
	
	public LevelSelectorActivity() {
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_selection);
		
		final ListView lv1 = (ListView) findViewById(R.id.level_selection_listview);

		LevelSelector levelSelector = new LevelSelector();
		final ArrayList<HashMap<String,String>> levels = levelSelector.getLevelList(getResources());

		lv1.setAdapter(new LevelSelectorAdapter(this, levels));
		
		lv1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Intent gameplayIntent = new Intent(LevelSelectorActivity.this, GameplayActivity.class);
				
				gameplayIntent.putExtra("data", levels.get(position).get("data"));
        		startActivity(gameplayIntent);
			}
		});
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(LevelSelectorActivity.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			return true;
		}
		
		return false;
	}
	
	public class LevelSelectorAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		
		private ArrayList<HashMap<String,String>> levels = null;
		
		public LevelSelectorAdapter(Context context, ArrayList<HashMap<String,String>> allLevels) {
			 inflater = LayoutInflater.from(context);
			 levels = allLevels;
		}
		
		public long getItemId(int position) {
			  return position;
		}
		
		public int getCount() {
			return levels.size();
		}
		
		public Object getItem(int position) {
			  return levels.get(position);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.level_selection_row, null);
			}
			
			TextView nameView = (TextView) convertView.findViewById(R.id.level_name);
			nameView.setText(levels.get(position).get("name"));
			
			TextView descriptionView = (TextView) convertView.findViewById(R.id.level_description);
			descriptionView.setText(levels.get(position).get("description"));
			
			return convertView;
		}
	}
}
