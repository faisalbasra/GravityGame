package se.liu.tdp021.gravityGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity{
	protected static String TAG = "MainMenu";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		Button startGameButton = (Button) findViewById(R.id.StartGame);
        startGameButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent levelSelectorIntent = new Intent(MainMenu.this, LevelSelectorActivity.class);
        		startActivity(levelSelectorIntent);
        	}
        });
        
        
		Button highscoreButton = (Button) findViewById(R.id.Highscore);
		highscoreButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent highscoreIntent = new Intent(MainMenu.this, HighscoreActivity.class);
        		startActivity(highscoreIntent);
        	}
        });
        
        
		Button optionsButton = (Button) findViewById(R.id.Options);
        optionsButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent optionIntent = new Intent(MainMenu.this, OptionsActivity.class);
        		startActivity(optionIntent);
        	}
        });
        
        
		Button exitButton = (Button) findViewById(R.id.Exit);
        exitButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		MainMenu.this.moveTaskToBack(true);
        	}
        });
        

	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
}
