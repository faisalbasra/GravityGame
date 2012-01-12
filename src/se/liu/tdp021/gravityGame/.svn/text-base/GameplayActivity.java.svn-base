package se.liu.tdp021.gravityGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;


enum Direction {
	UNDEFINED(-1), DOWN(0), LEFT(1), UP(2), RIGHT(3);
	
	private final int value;
	private Direction(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public int toInt(Direction direction) {
		return direction.getValue();
	}
	
	public Direction toEnum(int gravity) {
		if (gravity == 0) return Direction.DOWN;
		if (gravity == 1) return Direction.LEFT;
		if (gravity == 2) return Direction.UP;		
		if (gravity == 3) return Direction.RIGHT;
		
		return UNDEFINED;
	}
};

public class GameplayActivity extends Activity {
	private static String TAG = "GravGameActivity";
	private GameplayView gameplayView = null;
	private RotationSensor rotationSensor = null;
	private Direction recentDeviceOrientation;
	public HashMap<Direction, Integer> tiltThreshold = new HashMap<Direction, Integer>();
	
	public static Vector<DynamicGameObject> dynamicObjects= null;
	public static Vector<StaticGameObject> staticObjects= null;
	public static ArrayList<StaticGameObject> sortedStaticObjects = null;
	public static Vector<Integer> collectibleIndexes = null;
	public int numOfCollectibles;
	
	GameObjectSorter sorter = null;
	
	public static SpriteManager sprites = null;

	//TEMP VARIABLES TO BE REPLACED WITH OPTIONS-BASED VARS//
	
	public static boolean option_pauseOnFlip = true;
	public static boolean gameOver = false;
	public static boolean isDying = false;
	
	public static int tiltThresholdRotation = 45;
//	public static int tiltThresholdLeft = 23;
//	public static int tiltThresholdRight = -17;
//	public static int tiltThresholdUp = -28;
//	public static int tiltThresholdDown = -70;
	public static int tiltThresholdLeft = 23;
	public static int tiltThresholdRight = -23;
	public static int tiltThresholdUp = -23;
	public static int tiltThresholdDown = -23;
	public static int tiltThresholdTiltMode = 19;
	
	public float userBasePitch = 0;
	public float userBaseRoll = 0;
	
	// END TEMP VARIABLES //
	
	public static Player player = null;
	private int touchPlayerMoveCoord = -1;  //Set by onTouchEvent when the player should move; set to less than zero when screen isn't touched
	public static boolean supressPlayerFlips = false;
	public static boolean supressFlipCountReset = false;
	private View autoPauseMessageView = null;
	private PopupWindow autoPauseMessagePopup = null;
	private PopupWindow highscorePopup = null;
	
	private Timer timer = null;
	public static boolean paused = false;
	public static boolean autoPaused = false;
	
	
	public static boolean isInExplorationMode = false;
	public static int panningXDiff;
	public static int panningYDiff;
	
	public float currentPitch;
	public float currentRoll;

	public static Direction gravity = Direction.DOWN;

	
	
	private Display display = null;
	
	public static int screenWidth;
	public static int screenHeight;
	
	private int viewHeight;
	private int viewWidth;
	
	public static boolean gameStarted = false;
	
	private ActivityHandler handler = null;
	private int numberOfFlips = 0;
	
	
	public static Resources resources = null;
	public static Context context = null;
	private String levelname; //level_id
	private Time startTime; //used to calculate level complete time
	
	
	private EditText mEdit; //used for highscorepopup
	private String highscoreUsername;
	
	public static Level currentLevel = null;
	/**
	 * Initialize all helper objects.
	 */
	private void initializeHelpers() {
		// Initialize GameObject containers
		dynamicObjects = new Vector<DynamicGameObject>();
        staticObjects = new Vector<StaticGameObject>();
        sortedStaticObjects = new ArrayList<StaticGameObject>();
        collectibleIndexes = new Vector<Integer>();
        
        sprites = new SpriteManager(this);
        rotationSensor = new RotationSensor(this);
        
        gameplayView = (GameplayView) findViewById(R.id.gameplayview);
        
        sorter = new GameObjectSorter("x");
        
        timer = new Timer();
        startTime = new Time();
        startTime.setToNow();

        
        handler = new ActivityHandler();
	}
	
	
	/**
	 * Load and start a new level. 
	 * @param level
	 */
	private void startLevel(Level level, Intent intent) {
		currentLevel = level;		
		levelname = intent.getStringExtra("data");
		isDying = false;
		paused = false;
		isInExplorationMode = false;
		
		level.GetLevel(getResources(), levelname);
		sprites.setColours(level.colours);
        sprites.loadTextures(level.textureNames);
        sprites.loadBackgroundTexture(level.backgroundTextureName, level.bounds);
        
        gravity = level.playerStartingGravity;
        recentDeviceOrientation = gravity;
        player = new Player(this, level.playerStartingLocation.x, level.playerStartingLocation.y);
        player.baseFlipsLeft = level.playerFlipsAllowed;
        
        staticObjects.addAll(level.spikes);
        staticObjects.addAll(level.barriers);
        staticObjects.addAll(level.goals);
        staticObjects.addAll(level.flippers);
        staticObjects.addAll(level.blockers);
        staticObjects.addAll(level.collectibles);
        staticObjects.addAll(level.messages);
        staticObjects.addAll(level.walls);
        numOfCollectibles = level.collectibles.size();  //Used in OnCreate to avoid needless searching for collectible indices
        
        dynamicObjects.add(player);

        sortedStaticObjects.addAll(staticObjects);
	}
	
	/**
	 * Reset the current level, restoring all (if any) collectibles and creating a new player at the start location
	 */
	public void restartLevel() {
		restoreCollectibles();
		recentDeviceOrientation = Direction.UNDEFINED;
		//TODO: reset all stat counters
		numberOfFlips = 0;
		isDying = false;
		player.resetPlayer();
		gameplayView.levelRestart();
	}
	
	public void restoreCollectibles() {
		for (int i = 0; i < collectibleIndexes.size(); i++) {
			((Collectible)sortedStaticObjects.get(collectibleIndexes.get(i))).restore();
		}
		//collectibleIndexes.clear();
	}
	
	/**
	 * Clear the current level, and destroy all level objects.
	 */
	private void clearLevel() {
		sortedStaticObjects.clear();
		sortedStaticObjects = null;
		
		dynamicObjects.clear();
		dynamicObjects = null;

		collectibleIndexes.clear();
		collectibleIndexes = null;
		option_pauseOnFlip = true;
		gameOver = false;
		isDying = false;
		supressPlayerFlips = false;
		supressFlipCountReset = false;
		paused = false;
		autoPaused = false;
		isInExplorationMode = false;
		gameStarted = false;

		staticObjects.clear();
		staticObjects = null;
		
		sprites = null;
		
		player = null;
		
		gravity = Direction.DOWN;
		
		gameOver = false;
	}
	
	
	private void getScreenSize() {
		display = getWindowManager().getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
	}
	
	/**
	 * 
	 */
	private void destroyHelpers() {
		dynamicObjects = null;
        staticObjects = null;
        sortedStaticObjects = null;
        
        sprites = null;
        rotationSensor = null;
        
        gameplayView = null;
        
        sorter = null;
        
        timer = null;
        
        handler = null;
	}
	

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);

        getScreenSize();
        initializeHelpers();

        viewHeight = screenHeight;
        viewWidth = screenWidth;
        
        setGyroThresholds();
        
        if (viewHeight != 0 && viewWidth != 0) {
        	//defineOuterWalls();
        } else {
        	finish();
        }
        
    	resources = getResources();
    	context = this.getBaseContext();
    	//packageName = this.getPackageName();
    	
    	//Get options
    	SharedPreferences prefs = getPreferences(0);
    	option_pauseOnFlip = !prefs.getBoolean("tiltmode", false);
    	Log.e("GAMEOPTIONS", "PauseOnFlip is " + option_pauseOnFlip);
    	//TODO:option_
    	
        //Build the level
        Intent intent = getIntent();
        Level level = new Level();
        startLevel(level, intent);

    	Collections.sort(sortedStaticObjects, sorter);
    	
    	if (numOfCollectibles > 0) {
	    	//Find the indices
	    	for (int i = 0; i < sortedStaticObjects.size(); i++) {
	    		if (sortedStaticObjects.get(i) instanceof Collectible) {
	    			collectibleIndexes.add(i);  //We found a collectible, add it
	    			((Collectible)sortedStaticObjects.get(i)).ID = i;  //Note the index inside the target collectible
	    			Log.v("LevelStart:getting collectibles", "Found a collectible at index " + i);
	    			if (collectibleIndexes.size() == numOfCollectibles) {
	    				Log.v("LevelStart:getting collectibles", "Found all collectibles, breaking at " + i + "/" + sortedStaticObjects.size() + " objects checked");
	    				break;  //We found all the collectibles, break
	    			}
	    		}
	    	}
	    	Log.v("LevelStart:getting collectibles", "Parsing is done, found " + collectibleIndexes.size() + " collectibles");
    	}
    	
    	triggerAutoPause();
    }    
    

    @Override
    public void onRestart() {
    	super.onRestart();
    	
    	getScreenSize();
    	initializeHelpers();
    	
    	//Build the level
        Intent intent = getIntent();
        Level level = new Level();
        startLevel(level, intent);
        
        Collections.sort(sortedStaticObjects, sorter);
    }
    
    
    @Override
    public void onStop() {
    	super.onStop();
    	
    	clearLevel();
    	destroyHelpers();

    	gameOver = false;
    	gameStarted = false;
    	
    	
    	// Make sure we don't leak any popups
    	if (autoPauseMessagePopup != null) {
    		autoPauseMessagePopup.dismiss();
    		autoPauseMessagePopup = null;
    	}
    	if (highscorePopup != null) {
    		highscorePopup.dismiss();
    		highscorePopup = null;
    	}
    }
    
    
    @Override
    public void onPause() {
    	super.onPause();
    	rotationSensor.stopSensor();
    	timer.cancel();
    	timer.purge();
    }
        
    @Override
    public void onResume() {
    	super.onResume();
    	rotationSensor.startSensor(this.getBaseContext());
    	
        // Timer has to be started after all elements has been added to the GameObject vectors
    	timer = new Timer();
        timer.schedule(new Update(), 0, (long) 33.3);
    }
    
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	//TODO: We need to clean out all the memory we can here.
    }
    
    private void increaseFlipCount() {
    	numberOfFlips += 1;
    }
    
    
    public void updateSensorValues(float pitchValue, float rollValue) {
    	
    	currentPitch = pitchValue;
    	currentRoll = rollValue;
    	
    	if (!supressPlayerFlips && !isDying && gameStarted && ((paused && autoPaused) || (!paused))) {  //Don't bother updating the sensor at all if the player can't flip anyway
    		Direction direction = null;
    		float compareValue;
    		float userBaseThreshold;
    		//Log.v("Roll", "Comparing " + rollValue + " and " + tiltThreshold.get(Direction.RIGHT) );
    		
    		//highest: pitch (y) or roll (x)? 
    		if(Math.abs(rollValue) > Math.abs(pitchValue)) {
    			compareValue = rollValue;
    			userBaseThreshold = userBaseRoll;
    			//right or left 
    			if(rollValue > 0) 
    				direction = Direction.LEFT; 
    			else 
    				direction = Direction.RIGHT; 
    		}
    		else {
    			compareValue = pitchValue;
    			userBaseThreshold = userBasePitch;
    			//down or up
    			if(pitchValue > 0) 
    				direction = Direction.UP; 
    			else 
    				direction = Direction.DOWN; 
    		}

    		if(Math.abs(compareValue) > Math.abs(tiltThreshold.get(direction))) {
    		//if (Math.abs(Math.abs(compareValue) - (Math.abs(userBaseThreshold)/* + Math.abs(tiltThresholdTiltMode)*/)) >= tiltThresholdTiltMode) {
    			
    			//is it a new flip?
	    		if (recentDeviceOrientation != direction && gravity != direction && !autoPaused) {
	    			
	    			if (option_pauseOnFlip) {
	    				triggerAutoPause();
	    			} else {
	    				increaseFlipCount();  //Tilt mode; count the flip
	    			}
	    			
	    			
	    		}
	    		if (!option_pauseOnFlip) {
	    			player.changeGravityDirection(direction);
	    		}
    			recentDeviceOrientation = direction;
    			Log.v("Fuckery", "Orientation: " + recentDeviceOrientation);
    			
    		} else {
    			recentDeviceOrientation = Direction.UNDEFINED;
    		}
    		
    	}
    		
    }
    
  //TODO:FIX THESE MOVE-PLAYER CALLS, manipulate them to run at about 0.5 max speed
	@Override 
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && !isDying) {
			if (GameplayActivity.gravity == Direction.DOWN) {
				player.movePlayer(0);  
			} else if (GameplayActivity.gravity == Direction.UP) {
				player.movePlayer(0);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && !isDying) {
			if (GameplayActivity.gravity == Direction.DOWN) {
				player.movePlayer(screenWidth);
			} else if (GameplayActivity.gravity == Direction.UP) {
				player.movePlayer(screenWidth);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && !isDying) {
			if (GameplayActivity.gravity == Direction.LEFT) {
				player.movePlayer(0);
			} else if (GameplayActivity.gravity == Direction.RIGHT) {
				player.movePlayer(0);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && !isDying) {
			if (GameplayActivity.gravity == Direction.LEFT) {
				player.movePlayer(screenHeight);
			} else if (GameplayActivity.gravity == Direction.RIGHT) {
				player.movePlayer(screenHeight);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_1) {
			if (!supressPlayerFlips && !isDying)
				player.changeGravityDirection(Direction.DOWN);
			Log.v(TAG, "Set gravity to 0/down using keypad");
		} else if (keyCode == KeyEvent.KEYCODE_2) {
			if (!supressPlayerFlips && !isDying)
				player.changeGravityDirection(Direction.LEFT);
			Log.v(TAG, "Set gravity to 1/left using keypad");
		} else if (keyCode == KeyEvent.KEYCODE_3) {
			if (!supressPlayerFlips && !isDying)
				player.changeGravityDirection(Direction.UP);
			Log.v(TAG, "Set gravity to 2/up using keypad");
		} else if (keyCode == KeyEvent.KEYCODE_4) {
			if (!supressPlayerFlips && !isDying)
				player.changeGravityDirection(Direction.RIGHT);
			Log.v(TAG, "Set gravity to 3/right using keypad");
		} 
		
		else if (keyCode == KeyEvent.KEYCODE_MENU && !isDying) {
			if (paused) {
				Log.v(TAG, "Unpausing game");
				paused = false;
				GameplayActivity.isInExplorationMode = false;
				supressPlayerFlips = false;
//				timer = new Timer();
//              timer.schedule(new Update(), 0, (long) 33.3);
			} else {
				Log.v(TAG, "Pausing game");
				paused = true;
//				timer.cancel();
//				timer.purge();
				displayPauseMenu();
			}
		} else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			option_pauseOnFlip = !option_pauseOnFlip;  //Temporary key to enable/disable auto-pausing on gravity switch
			setGyroThresholds();  //TODO: Remove this temp key and this GyroSet()
		}
		
		return false;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
			Log.v(TAG, "Touch event type " + event.getAction());
			int evType = event.getAction(); 
			
			if (evType == MotionEvent.ACTION_DOWN || evType == MotionEvent.ACTION_MOVE && !isDying) {  //We want to move/unpause
				
				if (GameplayActivity.isInExplorationMode) {
					// The game is paused. Map exploration mode.
					float posX = event.getX();
					float posY = event.getY();
					
					int x = 0;
					int y = 0;
					int panningSpeed = 5;
					
					if (posX < viewWidth / 3) {
						// Left
						x = -panningSpeed;
					} else if (posX > viewWidth - (viewWidth / 3)) {
						// Right
						x = panningSpeed;
					}
					
					if (posY < viewHeight / 3) {
						// Top
						y = -panningSpeed;
					} else if (posY > viewHeight - (viewHeight / 3)) {
						// Bottom
						y = panningSpeed;
					}
					
					GameplayActivity.panningXDiff = x;
					GameplayActivity.panningYDiff = y;
					
				} else {
				
					if (autoPaused) {
						//The player has rotated the device with autoPausing enabled, unpause
						disableAutoPause();
						if (!gameStarted) {
							gameStarted = true;
							userBasePitch = currentPitch;
							userBaseRoll = currentRoll;
							Log.d("Sensor", "UBP set to " + userBasePitch + ", UBR set to " + userBaseRoll);
						}
						
					} else {
						int posX = (int)event.getX();
						int posY = (int)event.getY();
						
						/*// NEW MOVE SYSTEM //
						if (GameplayActivity.gravity == 0 || GameplayActivity.gravity == 2) {
							player.setTargetX(posX);
						}
						else {  //Gravity 1 or 3; left or right
							player.setTargetY(posY);			
						}*/
						// END NEW MOVE SYSTEM //
						  //OLD SYSTEM
						
						if (GameplayActivity.gravity == Direction.DOWN || GameplayActivity.gravity == Direction.UP) {
							touchPlayerMoveCoord = posX;
						} else if (GameplayActivity.gravity == Direction.LEFT || GameplayActivity.gravity == Direction.RIGHT) {
							touchPlayerMoveCoord = posY;
						}
					}
				}
				  // END OLD SYSTEM
			} else if (evType == MotionEvent.ACTION_UP) {
				touchPlayerMoveCoord = -1;
				GameplayActivity.panningXDiff = 0;
				GameplayActivity.panningYDiff = 0;
			}
			
		
		return true;
	}
	
	private boolean collidedFromLeft(DynamicGameObject dynObj, StaticGameObject solid) {
		return ((dynObj.getXPrev() + dynObj.width()) <=/*=*/ solid.getTopPosition().x && dynObj.getBottomPosition().x > solid.getTopPosition().x);
	}  // If previous X2 was outside solid X1, but is now inside
	
	private boolean collidedFromRight(DynamicGameObject dynObj, StaticGameObject solid) {
		return (dynObj.getXPrev() >=/*=*/ solid.getBottomPosition().x && dynObj.getTopPosition().x < solid.getBottomPosition().x );
	}  //If previous X1 was outside solid X2, but is now inside
	
	private boolean collidedFromTop(DynamicGameObject dynObj, StaticGameObject solid) {
		return ((dynObj.getYPrev() + dynObj.height()) <=/*=*/ solid.getTopPosition().y && dynObj.getBottomPosition().y > solid.getTopPosition().y);
	}  //If previous Y2 was outside solid Y1, but is now inside
	
	private boolean collidedFromBottom(DynamicGameObject dynObj, StaticGameObject solid) {
		return (dynObj.getYPrev() >=/*=*/ solid.getBottomPosition().y && dynObj.getTopPosition().y < solid.getBottomPosition().y);
	}  //If previous Y1 was outside solid Y2, but is now inside
	
	
	
	//private void checkCollisions(Vector<DynamicGameObject> dynObjects, Vector<StaticGameObject> statObjects) {
	private void checkCollisions() {
		boolean isAirborne = true;
		boolean hasBeenFlipped = false;
		boolean hasBeenFlipBlocked = false;
		Direction barrierRotation = Direction.UNDEFINED;
		
		for (DynamicGameObject dyn : GameplayActivity.dynamicObjects) {
//			boolean isAirborne = true;
			for (int i = 0; i != GameplayActivity.sortedStaticObjects.size(); i++) {
				StaticGameObject stat = GameplayActivity.sortedStaticObjects.get(i);
				
//			for (GameObject stat : GameplayActivity.sortedStaticObjects) {
				PointF dynTopPos = dyn.getTopPosition();
				PointF dynBottomPos = dyn.getBottomPosition();
				PointF statTopPos = stat.getTopPosition();
				PointF statBottomPos = stat.getBottomPosition();
				
				
				if (dynTopPos.y >= statBottomPos.y ||
						dynBottomPos.y <= statTopPos.y ||
						dynTopPos.x >= statBottomPos.x ||
						dynBottomPos.x <= statTopPos.x) { // No Collision
					
					
				} else if (!isDying) { // Collision
					
					
					//TODO: If further DynamicObjects are added, insert this check in below IFs: dyn instanceof Player && 
					if (stat instanceof Goal) {
						// Player collides with a Goal
						gameOver = true;
						return;
					} else if (stat instanceof Flipper) {
						hasBeenFlipped = true;
						Log.v("Flipping", "Colliding");
						if (!supressPlayerFlips) {
							Log.v("Flipping", "Also flipping player");
							supressPlayerFlips = true;
							player.changeGravityDirection(gravity.toEnum(((Flipper)stat).rotation));
						}
						continue;
					} else if (stat instanceof Barrier) {
						barrierRotation = gravity.toEnum(((Barrier)stat).rotation);
						Log.v("BarrCol", ("Collided with barrier @ rotation " + barrierRotation));
					} else if (stat instanceof FlipBlocker) {
						player.flipsLeft = 0;
						supressFlipCountReset = true;
						hasBeenFlipBlocked = true;
						continue;
					} else if (stat instanceof Collectible) {
						((Collectible)stat).collect();  //Hides the collectible
						//TODO: +1 to # collected tokens in Stats object. Do it inside the token.collect()?
						continue;
					} else if (stat instanceof Spike) {
						isDying = true;
						gameplayView.playerDied();
						player.triggerDeath(this);
						//restartLevel();
						return;
					} else if (stat instanceof MessageBox) {

						// Ignore collision
						continue;
					}
					
					switch (GameplayActivity.gravity) {
					/* // NOTE //
					 * Inside each case, the comments regarding walking direction
					 * are relative to the rotation of the device.
					 * E.g. if you hold the device in left landscape, "walking left" means moving your character
					 * left across the screen as you hold it.
					 * "On top" is simply the surface you can stand on with the current gravity, and "below"
					 * will rarely occur since there's no jumping or spring pads.
					 * */
					
					case DOWN:
						// "default" (standard portrait) orientation
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.RIGHT) {
							if (collidedFromLeft(dyn, stat)) {
								//Walking right
								dyn.setXVelocity(0);
								dyn.setCollisionX(statTopPos.x - dyn.width());
							}
						}
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.LEFT) {
							if (collidedFromRight(dyn, stat)) {
							// Walking left
							dyn.setXVelocity(0);
							dyn.setCollisionX(statBottomPos.x);
							}
						}
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.UP) {
							if (collidedFromBottom(dyn, stat)) {
								// Below
								dyn.setYVelocity(0);
								dyn.setCollisionY(statBottomPos.y);
							}
						}

						
						//(standing surface)
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.DOWN) {
							if (collidedFromTop(dyn, stat)) {
								// On top
								dyn.setYVelocity(0);
								dyn.setOnGroundState(true);
								dyn.setCollisionY(statTopPos.y - dyn.height());
							}
						}
						
						if (dynTopPos.x < statBottomPos.x ||
								dynBottomPos.x > statTopPos.x) {
							isAirborne = false;
						}
						
						barrierRotation = Direction.UNDEFINED;
								
						break;
					case LEFT:
						// Left in landscape mode

						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.RIGHT) {
							if (collidedFromLeft(dyn, stat)) {
								// Below
								dyn.setXVelocity(0);
								dyn.setCollisionX(statTopPos.x);
							}
						}
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.DOWN) {
							if (collidedFromTop(dyn, stat)) {
								// walking right
								dyn.setYVelocity(0);
								dyn.setCollisionY(statTopPos.y - dyn.height());
							}
						}
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.UP) {	
							if (collidedFromBottom(dyn, stat)) {
								// walking left
								dyn.setYVelocity(0);
								dyn.setCollisionY(statBottomPos.y);
							}
						}
						
						//(standing surface)
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.LEFT) {
							if (collidedFromRight(dyn, stat)) {
								// On top
								dyn.setXVelocity(0);
								dyn.setOnGroundState(true);
								dyn.setCollisionX(statBottomPos.x);
							}
						}
						
						if (dynTopPos.y < statBottomPos.y ||
								dynBottomPos.y > statTopPos.y) {
							isAirborne = false;
						}
						
						barrierRotation = Direction.UNDEFINED;
						
						break;
					case UP:
						// Upside down in portrait mode
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.RIGHT) {
							if (collidedFromLeft(dyn, stat)) {
								// Walking left
								dyn.setXVelocity(0);
								dyn.setCollisionX(statTopPos.x - dyn.width());
							}
						}
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.LEFT) {
							if (collidedFromRight(dyn, stat)) {
								// Walking right
								dyn.setXVelocity(0);
								dyn.setCollisionX(statBottomPos.x);
							}
						}
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.DOWN) {
							if (collidedFromTop(dyn, stat)) {
								// Below
								dyn.setYVelocity(0);
								dyn.setCollisionY(statTopPos.y - dyn.height());
							}
						}
						
						//(standing surface)
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.UP) {
							if (collidedFromBottom(dyn, stat)) {
								// On top
								dyn.setYVelocity(0);
								dyn.setOnGroundState(true);
								dyn.setCollisionY(statBottomPos.y);
							}
						}
						
						if (dynTopPos.x < statBottomPos.x ||
								dynBottomPos.x > statTopPos.x) {
							isAirborne = false;
						}
						barrierRotation = Direction.UNDEFINED;
						break;
					case RIGHT:
						// Right in landscape mode
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.LEFT) {
							if (collidedFromRight(dyn, stat)) {
								// Below
								dyn.setXVelocity(0);
								dyn.setCollisionX(statBottomPos.x);
							}
						}
							
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.DOWN) {
							if (collidedFromTop(dyn, stat)) {
								// Walking left
								dyn.setYVelocity(0);
								dyn.setCollisionY(statTopPos.y - dyn.height());
							}
						}
						
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.UP) {
							if (collidedFromBottom(dyn, stat)) {
								// Walking right
								dyn.setYVelocity(0);
								dyn.setCollisionY(statBottomPos.y);
							}
						}
						
						//(standing surface)
						if (barrierRotation == Direction.UNDEFINED || barrierRotation == Direction.RIGHT) {
							if (collidedFromLeft(dyn, stat)) {
								// On top
								dyn.setXVelocity(0);
								dyn.setOnGroundState(true);
								dyn.setCollisionX(statTopPos.x - dyn.width());
							}
						}
						
						if (dynTopPos.y < statBottomPos.y ||
								dynBottomPos.y > statTopPos.y) {
							isAirborne = false;
						}
						barrierRotation = Direction.UNDEFINED;
						break;
					}
					
				}
				
				// Avoid unnecessary collision tests.
				if (i + 1 < GameplayActivity.sortedStaticObjects.size() &&
						GameplayActivity.sortedStaticObjects.get(i+1).getTopPosition().x > dynBottomPos.x) {
					// No further tests needed.
					break;
					//return;
				}
			}
		}
		
		if (!hasBeenFlipped) {  //No contact with flippers this pass, allow flipping
			//Log.v("FlippingEnd", "Hasn't been flipped, supression disabled");
			supressPlayerFlips = false;
		}
		
		if (!hasBeenFlipBlocked) {  //No contact with flipBlockers this pass, allow counter to reset on landing
			supressFlipCountReset = false;
		}
		
		if (isAirborne && player.isOnGround()) {
			player.setOnGroundState(false);
		}
		//Log.d(TAG, "Total number of collision tests: " + numOfTests);
		//Log.d(TAG, "Total number of collisions detected: " + numOfCollisions);
	}
	
	private void triggerAutoPause() {
		if (!autoPaused && player.allowedToFlip()) {
			Log.v((TAG+"::Pausing"), ("AutoPause triggered, direction " + gravity));
			paused = true;
			autoPaused = true;

			player.pausedDirection = gravity;
			if (!gameOver && gameStarted) {
				displayAutoPauseMessage();
			}
		}
	}
	
	private void displayAutoPauseMessage() {
		if (autoPauseMessagePopup == null) {
			LayoutInflater inflater = getLayoutInflater();
			autoPauseMessageView = inflater.inflate(R.layout.autopause_message,(ViewGroup)findViewById(R.layout.gameplay));

			autoPauseMessagePopup = new PopupWindow(autoPauseMessageView,150,50, true); 
			autoPauseMessagePopup.setFocusable(false);
			autoPauseMessagePopup.setOutsideTouchable(true);
			autoPauseMessagePopup.setTouchable(false);
			
			autoPauseMessagePopup.showAtLocation(this.findViewById(R.id.gameplayview), Gravity.CENTER, 0, viewHeight - 60);
		}
	}
	
	private void disableAutoPause() {
		paused = false;
		autoPaused = false;
		Log.v((TAG+"::Pausing"), ("AutoPause disabled"));
		if (recentDeviceOrientation != player.pausedDirection) {
			player.changeGravityDirection(recentDeviceOrientation);  //Set the gravity direction
			increaseFlipCount();  //Costs one flip
			//The player has changed the orientation of the device before unpausing
			//player.flipsLeft -= player.flipCost;  //Base case is flipsLeft = 1, cost = 1, == no more flipping
		}
		
		if (autoPauseMessagePopup != null) {
			autoPauseMessagePopup.dismiss();
			autoPauseMessagePopup = null;
		}
	}
	
	public void setGyroThresholds() {
		//TODO: Clean up this function after proper options are added
		if (!option_pauseOnFlip) {  //Tiltmode engaged, individual values for all directions
			tiltThreshold.put(Direction.DOWN, tiltThresholdDown); 
			tiltThreshold.put(Direction.LEFT, tiltThresholdLeft);
			tiltThreshold.put(Direction.UP, tiltThresholdUp); 
			tiltThreshold.put(Direction.RIGHT, tiltThresholdRight);
		} else {
			tiltThreshold.put(Direction.DOWN, -tiltThresholdRotation); 
			tiltThreshold.put(Direction.LEFT, tiltThresholdRotation);
			tiltThreshold.put(Direction.UP, tiltThresholdRotation); 
			tiltThreshold.put(Direction.RIGHT, -tiltThresholdRotation);
		}
	}
	
	private void loadCachedUsername() {
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		highscoreUsername = settings.getString("username",null);	
	}
	
	private void newCachedUsername(String username) {
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", username);
		editor.commit();
	}

	/**
	 * Highscore popup window
	 */
	public void finishLevel() {		
		
		//popup window
		LayoutInflater inflater = getLayoutInflater();
		
		View mView = inflater.inflate(R.layout.highscore_popup,(ViewGroup)findViewById(R.layout.gameplay));
		
		highscorePopup = new PopupWindow(mView, viewWidth, viewHeight, true);
		highscorePopup.showAtLocation(this.findViewById(R.id.gameplayview), Gravity.TOP, 0, 0);

		highscorePopup.setFocusable(true);
		highscorePopup.setTouchable(true);

		final View mPopupWindowView = highscorePopup.getContentView();
		mEdit = (EditText) mPopupWindowView.findViewById(R.id.highscore_name);
		
		loadCachedUsername();
		
		//set cached username in username input box
		if(highscoreUsername != null)
			mEdit.setText(highscoreUsername);
		
		//submit score
		final Button button = (Button) mPopupWindowView.findViewById(R.id.highscore_submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	String username = mEdit.getText().toString();
            	
            	//username have been typed
            	if(username.length() != 0) {
            		//if new username, cache it
                	if(!username.equals(highscoreUsername))
                		newCachedUsername(username);
                	
                	
            		int collectedTokens = 0;
            		for (int i = 0; i < collectibleIndexes.size(); i++) {
            			if ( ((Collectible)sortedStaticObjects.get(collectibleIndexes.get(i))).isCollected() ) {
            				collectedTokens++;
            			}
            		}
            		
            		
            		//get value from text form and send it to next activity
                	Bundle parameters = new Bundle();
            		Intent i = new Intent(GameplayActivity.this, HighscoreLevelActivity.class);
            		parameters.putString("username", username); 
            		parameters.putString("level_id", levelname);
            		parameters.putString("startedFrom", "GameplayActivity");
            		parameters.putInt("flips",numberOfFlips);
            		parameters.putInt("collectedTokens", collectedTokens);
                	i.putExtras(parameters);
                	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	highscorePopup.dismiss();
                	highscorePopup = null;
            		startActivity(i);
            		
            	} 
            	//username not typed
            	else {
            		Context context = getApplicationContext();
                	CharSequence text = "You need to write a name!";
                	int duration = Toast.LENGTH_SHORT;

                	Toast toast = Toast.makeText(context, text, duration);
                	toast.show();
            	}
            }
        });
        
        //restoreCollectibles();
		
        paused = true;
//		try {
//			finalize();
//		} catch (Throwable e) {
//		}
	}
	
	class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			finishLevel();
		}
	}
	
	class Update extends TimerTask {
		public void run() {
			if (!paused) {
				
				if (touchPlayerMoveCoord >= 0) {  //We should move the player due to touch input
						player.movePlayer(touchPlayerMoveCoord);
				}
				
				for (DynamicGameObject object : dynamicObjects) {
					object.update();
				}
				
				//checkCollisions(dynamicObjects, staticObjects);
				checkCollisions();
				
				if (GameplayActivity.gameOver) {
					handler.sendEmptyMessage(1);
				}
				
				if (!isDying && currentLevel.bounds != null && !insideBounds(player)) {
					// Player is falling through the universe!
					// Kill him and restart level.
					//restartLevel();
					isDying = true;
					player.triggerDeath(GameplayActivity.this);
					gameplayView.playerDied();
				}
				
				
			}
			gameplayView.postInvalidate();
		}
	}
	/*
	class TriggerStartupMessage extends TimerTask {
		public void run() {
			PopupWindow startupMessage = new Pop
		}
	}*/
	
	/**
	 * Check if sender is inside level bounds
	 * @return
	 */
	private boolean insideBounds(GameObject sender) {
		if (sender.getBottomPosition().y < 0 ||
				sender.getBottomPosition().x < 0 ||
				sender.getTopPosition().x > currentLevel.bounds.x ||
				sender.getTopPosition().y > currentLevel.bounds.y) {
			return false;
		}
		return true;
	}
	
	
	public void displayPauseMenu() {
		Log.v(TAG, "Displaying Pause Menu");
		
		LayoutInflater inflater = getLayoutInflater();
		View pauseMenu = inflater.inflate(R.layout.pause_menu,(ViewGroup)findViewById(R.layout.gameplay));
		
		final PopupWindow popup = new PopupWindow(pauseMenu, viewWidth, viewHeight, true);
		popup.showAtLocation(this.findViewById(R.id.gameplayview), Gravity.TOP, 0, 0);
		
		View popupView = popup.getContentView();
		
		Button resumeButton = (Button) popupView.findViewById(R.id.resume_game_button);
		Button panButton = (Button) popupView.findViewById(R.id.pan_map_button);
		
		Button exitToMenuButton = (Button) popupView.findViewById(R.id.main_menu_button);
		
		resumeButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		paused = false;
        		popup.dismiss();
//        		timer = new Timer();
//                timer.schedule(new Update(), 0, (long) 33.3);
        	}
        });
		
		panButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		GameplayActivity.isInExplorationMode = true;
        		supressPlayerFlips = true;
        		popup.dismiss();
        	}
        });
		
		exitToMenuButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		popup.dismiss();
        		Intent i = new Intent(GameplayActivity.this, MainMenu.class);
    			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(i);
    			finish();
        	}
        });
	}
	
	public class SpriteManager {
		public Bitmap[] player = new Bitmap[4];
		public Bitmap[] spike = new Bitmap[4];
		public Bitmap[] gravSwitcher = new Bitmap[4];
		
		public Bitmap goal = null;
		public Bitmap collectible = null;
		public BitmapDrawable backgroundTexture = null;
		public HashMap<String, Paint> colours = new HashMap<String, Paint>();
		public HashMap<String, BitmapDrawable> textures = new HashMap<String, BitmapDrawable>();
		
		public SpriteManager(Context res) {
			//Load all sprites
			
			player[0] = BitmapFactory.decodeResource(res.getResources(), R.drawable.pirate_down);
			player[1] = BitmapFactory.decodeResource(res.getResources(), R.drawable.pirate_left);
			player[2] = BitmapFactory.decodeResource(res.getResources(), R.drawable.pirate_up);
			player[3] = BitmapFactory.decodeResource(res.getResources(), R.drawable.pirate_right);
			
			spike[0] = BitmapFactory.decodeResource(res.getResources(), R.drawable.spike_down);
			spike[1] = BitmapFactory.decodeResource(res.getResources(), R.drawable.spike_left);
			spike[2] = BitmapFactory.decodeResource(res.getResources(), R.drawable.spike_up);
			spike[3] = BitmapFactory.decodeResource(res.getResources(), R.drawable.spike_right);
			
			goal = BitmapFactory.decodeResource(res.getResources(), R.drawable.goal);
			collectible = BitmapFactory.decodeResource(res.getResources(), R.drawable.coin_token_gold);
			
			gravSwitcher[0] = BitmapFactory.decodeResource(res.getResources(), R.drawable.gravswitch_down);
			gravSwitcher[1] = BitmapFactory.decodeResource(res.getResources(), R.drawable.gravswitch_left);
			gravSwitcher[2] = BitmapFactory.decodeResource(res.getResources(), R.drawable.gravswitch_up);
			gravSwitcher[3] = BitmapFactory.decodeResource(res.getResources(), R.drawable.gravswitch_right);
		}
		
		public void setColours(HashMap<String, Paint> paints) {
			colours = paints;
		}
		
		public void loadTextures(Vector<String> textureNames) {
			for (String name : textureNames) {
				if (name != null) {
					int res = GameplayActivity.resources.getIdentifier(name, "drawable", getPackageName());
					if (res == 0) {
						Log.e(TAG, "Cannot resolve " + name + " to Resource ID!");
					}
					
					textures.put(name, new BitmapDrawable(BitmapFactory.decodeResource(GameplayActivity.resources, res)));
					textures.get(name).setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
				}
			}
		}
		
		public void loadBackgroundTexture(String textureName, PointF bounds) {
			if (textureName != null && bounds.x != 0 && bounds.y != 0) {
				int res = GameplayActivity.resources.getIdentifier(textureName, "drawable", getPackageName());
				if (res == 0) {
					Log.e(TAG, "Cannot resolve " + textureName + " to Resource ID!");
				}
				backgroundTexture = new BitmapDrawable(BitmapFactory.decodeResource(GameplayActivity.resources, res));
				backgroundTexture.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
				backgroundTexture.setBounds(0, 0, (int) bounds.x, (int) bounds.y);
			}
		}
	}
	
	public class GameObjectSorter implements Comparator<GameObject> {
		private String axis;
		int referenceX;
		int referenceY;
		
		public GameObjectSorter(String axis) {
			this.axis = axis;
		}
		
		public GameObjectSorter(String axis, int referenceX, int referenceY) {
			this.axis = axis;
			this.referenceX = referenceX;
			this.referenceY = referenceY;
		}
		
		public void setAxis(String axis) {
			this.axis = axis;
		}
		
		
		public int compare(GameObject object1, GameObject object2) {
			if (axis == "x") {
				boolean isLess = object1.getTopPosition().x < object2.getTopPosition().x;
				if (isLess) return -1;
				return 1;
			} else {
				boolean isLess =  object1.getTopPosition().y < object2.getTopPosition().y;
				if (isLess) return -1;
				return 1;
			}
		}
	}
}