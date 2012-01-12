package se.liu.tdp021.gravityGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameplayView extends View  {
	protected static String TAG = "GameplayView";
	
	private Canvas canvas = null;
	
	public static int gameWidth;
	public static int gameHeight;
	
	private int xOffset = 0;
	private int yOffset = 0;
	
	private float playerDeathTranslateX = -1;
	private float playerDeathTranslateY = -1;
	private boolean playerIsDying = false;
		
	PointF currentTranslate = new PointF(0,0);
	
	public GameplayView(Context context, AttributeSet attrSet) {
		super(context, attrSet);
		
        setFocusable(true);
	}
	
	public GameplayView(Context context) {
		super(context);
		Log.e(TAG, "'Wrong' constructor called");
	}
	
	
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		gameWidth = right;
		gameHeight = bottom;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		if (!playerIsDying) {  //Player is alive, proceed as normal
			
			if (GameplayActivity.isInExplorationMode) {
				canvas.translate(currentTranslate.x - GameplayActivity.panningXDiff, currentTranslate.y - GameplayActivity.panningYDiff);
				currentTranslate = new PointF(currentTranslate.x - GameplayActivity.panningXDiff, currentTranslate.y - GameplayActivity.panningYDiff);
				//Log.d(TAG, "Translating canvas in level exploration mode.");
			} else {
				// Center the view on the player
				PointF playerPos = GameplayActivity.player.getTopPosition();
				canvas.translate(-playerPos.x + (gameWidth / 2) - (GameplayActivity.player.width() / 2), 
						-playerPos.y + (gameHeight / 2) - (GameplayActivity.player.height() / 2));
				
				currentTranslate = new PointF(-playerPos.x + (gameWidth / 2) - (GameplayActivity.player.width() / 2), 
						-playerPos.y + (gameHeight / 2) - (GameplayActivity.player.height() / 2));
			}
		} else {  //Player is currently undergoing a death animation, use frozen location
			canvas.translate(playerDeathTranslateX, playerDeathTranslateY);
			currentTranslate = new PointF(playerDeathTranslateX, playerDeathTranslateY);
		}
		
		//canvas.drawPaint(GameplayActivity.sprites.colours.get("background"));
		if (GameplayActivity.sprites.colours.get("background") != null) {
			if (GameplayActivity.currentLevel.bounds != null) {
				canvas.drawRect(new Rect(0,0,(int)GameplayActivity.currentLevel.bounds.x, (int)GameplayActivity.currentLevel.bounds.y), GameplayActivity.sprites.colours.get("background"));
			} else {
				// We have a background colour, but not the bounds. Lets paint the whole world!
				canvas.drawPaint(GameplayActivity.sprites.colours.get("background"));
			}
		}

		if (GameplayActivity.sprites.backgroundTexture != null) {
			GameplayActivity.sprites.backgroundTexture.draw(canvas);
		}

		for (GameObject object : GameplayActivity.staticObjects) {
			object.draw(canvas);
		}

		for (GameObject object : GameplayActivity.dynamicObjects) {
			object.draw(canvas);
		}
	}
	
	public void playerDied() {
		Log.v(TAG, "Player has died, freezing camera");
		playerIsDying = true;
		PointF playerPos = GameplayActivity.player.getTopPosition();
		playerDeathTranslateX = -playerPos.x + (gameWidth / 2) - (GameplayActivity.player.width() / 2);
		playerDeathTranslateY = -playerPos.y + (gameHeight / 2) - (GameplayActivity.player.height() / 2);
	}
	
	public void levelRestart() {
		playerDeathTranslateX = -1;
		playerDeathTranslateY = -1;
		playerIsDying = false;
	}
	
	
	public PointF getCurrentTranslate() {
	    return currentTranslate;
	}
}
