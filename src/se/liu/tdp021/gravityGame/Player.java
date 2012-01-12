package se.liu.tdp021.gravityGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class Player extends DynamicGameObject {
	protected static String TAG = "Player";
	private float xPos;
	private float yPos;
	private float xPrev;
	private float yPrev;
	private float maxSpeed;
	private float maxFallingSpeed;
	private float moveSpeed = 3;
	private boolean hasMoved = false;
	
	private int spriteWidth;
	private int spriteHeight;
	
	private Point startingLocation;
	private Direction startingGravity;
	
	private GameplayActivity gameplayActivity = null;
	
	// NEW MOVE SYSTEM [OBSOLETE: TODO; Toss later]//
	private int targetX, targetY = -1;
	// END NEW MOVE SYSTEM //
	
	
	private Bitmap currentSprite;
	
	private boolean onGround = false;
	public Direction pausedDirection = GameplayActivity.gravity;  //Used to determine if a flip use should be consumed
	//CONFIGURATION, can't be bothered to use a config file for something this trivial
	private double flipManualVelocityDivisor = 1.5;
	private double flipForcedVelocityDivisor = 3;
	//END CONFIGURATION
	
	public int baseFlipsLeft = 2;  //REMEMBER: leaving the ground costs one flip
	public int flipsLeft = baseFlipsLeft;
	public int flipCost = 1;
	
	private float xVelocity = 0;
	private float yVelocity = 0;
	
	private float friction = 0.1f; // TODO Stronger friction on ground!
	
	public float deathX;
	public float deathY;
	
	
	
	private static boolean v = false;
	
	
	public Player(Context context, float x, float y) {
		maxSpeed = 4.5f;
		maxFallingSpeed = 9;
		xPos = x;
		yPos = y;
		xPrev = xPos;
		yPrev = yPos;
		
		startingLocation = new Point((int)xPos,(int)yPos);
		startingGravity = GameplayActivity.gravity;
		pausedDirection = GameplayActivity.gravity;
		
		currentSprite = GameplayActivity.sprites.player[GameplayActivity.gravity.toInt(GameplayActivity.gravity)];//BitmapFactory.decodeResource(context.getResources(), R.drawable.asset_char_future);
		//spriteDown = currentSprite; //BitmapFactory.decodeResource(context.getResources(), R.drawable.asset_char_future);
		//spriteLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.asset_char_future_left);
		//spriteRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.asset_char_future_right);
		//spriteUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.asset_char_future_up);
		
		spriteHeight = currentSprite.getHeight();
		spriteWidth = currentSprite.getWidth();
		
		Log.v(TAG, ("Player created at (" + xPos +"," + yPos + ") with gravity " + startingGravity));
		
	}
	
	public void triggerDeath(GameplayActivity caller) {
		if (GameplayActivity.isDying) {
			Log.v(TAG, ("triggering death"));
			xVelocity = 0;
			yVelocity = 0;
			deathX = xPos;
			deathY = yPos;
			gameplayActivity = caller;
			
			switch (GameplayActivity.gravity) {
			case DOWN:
				yVelocity = -maxFallingSpeed/2;
				break;
			case LEFT:
				xVelocity = maxFallingSpeed/2;
				break;
			case UP:
				yVelocity = maxFallingSpeed/2;
				break;
			case RIGHT:
				xVelocity = -maxFallingSpeed/2;
				break;
			}
		}
		
		
	}
	
	public void update() {
		if (!onGround) {
			applyGravity();
		}
		
		Direction gravity = GameplayActivity.gravity;
		
		if (gravity == Direction.DOWN || gravity == Direction.UP) {  //Up or Down
			if (xVelocity > 0) {
				xVelocity -= friction;
				if (xVelocity < 0) {
					xVelocity = 0;
				}
			} else if (xVelocity < 0) {
				xVelocity += friction;
				if (xVelocity > 0) {
					xVelocity = 0;
				}
			}
			
			/*// NEW MOVE SYSTEM //
			if (targetX != -1 && xPos != targetX) {
				if (xPos < targetX) {  //Character is to the left of target
					if (targetX-xPos <= maxSpeed) {  //We're close enough to jump to target
						xVelocity = 0;
						xPos = targetX;
						targetX = -1;  //Target unset
					} else {  //Not close enough to jump
						movePlayer(3);  //Move player naturally right
					}
				} else {  //Character is to the right of target
					if (xPos-targetX <= maxSpeed) {  //We're close enough to jump
						xVelocity = 0;
						xPos = targetX;
						targetX = -1;  //Target unset
					} else {  //Not close enough to jump
						movePlayer(1);  //Move player naturally left
					}
				}
			}
			*/// END NEW MOVE SYSTEM //
			if (!GameplayActivity.isDying) {
				if (xVelocity > maxSpeed) {
					xVelocity = maxSpeed;
				} else if (xVelocity < -maxSpeed) {
					xVelocity = -maxSpeed;
				}
				
				if (yVelocity > maxFallingSpeed) {
					yVelocity = maxFallingSpeed;
				} else if (yVelocity < -maxFallingSpeed) {
					yVelocity = -maxFallingSpeed;
				}
			}
		} else if (gravity == Direction.LEFT || gravity == Direction.RIGHT) {  //Left or Right
			if (yVelocity < 0) {
				yVelocity += friction;
				if (yVelocity > 0) {
					yVelocity = 0;
				}
			} else if (yVelocity > 0) {
				yVelocity -= friction;
				if (yVelocity < 0) {
					yVelocity = 0;
				}
			}
			
			/*// NEW MOVE SYSTEM //
			if (targetY != -1 && yPos != targetY) {
				if (yPos < targetY) {  //Character is above target
					if (targetY-yPos <= maxSpeed) {  //We're close enough to jump to target
						yVelocity = 0;
						yPos = targetY;
						targetY = -1;  //target unset
					} else {  //Not close enough to jump
						movePlayer(0);  //Move player naturally down
					}
				} else {  //Character is below target
					if (yPos-targetY <= maxSpeed) {  //We're close enough to jump
						yVelocity = 0;
						yPos = targetY;
						targetY = -1;  //target unset
					} else {  //Not close enough to jump
						movePlayer(2);  //Move player naturally up
					}
				}
			}
			*/// END NEW MOVE SYSTEM //
			
			if (!GameplayActivity.isDying) {
				if (xVelocity > maxFallingSpeed) {
					xVelocity = maxFallingSpeed;
				} else if (xVelocity < -maxFallingSpeed) {
					xVelocity = -maxFallingSpeed;
				}
				
				if (yVelocity > maxSpeed) {
					yVelocity = maxSpeed;
				} else if (yVelocity < -maxSpeed) {
					yVelocity = -maxSpeed;
				}
			}
		}
		
		setPosition(xPos + xVelocity, yPos + yVelocity, true);
		
		hasMoved = false;
		
		if (GameplayActivity.isDying) {
			switch (GameplayActivity.gravity) {
			case DOWN:
				if (yPos >= deathY + (GameplayActivity.screenHeight/2))
					gameplayActivity.restartLevel();
				break;
			case LEFT:
				if (xPos <= deathX - (GameplayActivity.screenWidth/2))
					gameplayActivity.restartLevel();
				break;
			case UP:
				if (yPos <= deathY - (GameplayActivity.screenHeight/2))
					gameplayActivity.restartLevel();
				break;
			case RIGHT:
				if (xPos >= deathX + (GameplayActivity.screenWidth/2))
					gameplayActivity.restartLevel();
				break;
			
			}
		}
	}
	
	
	public void draw(Canvas targetCanvas) {
		targetCanvas.drawBitmap(GameplayActivity.sprites.player[GameplayActivity.gravity.toInt(GameplayActivity.gravity)], xPos, yPos, null);
		//targetCanvas.drawBitmap(currentSprite, xPos, yPos, null);
	}
	
	// NEW MOVE SYSTEM //
	public void setTargetX(int x) {
		Log.v(TAG, ("Setting player xTarget to " + x + " with xPos being " + xPos));
		targetX = x;
	}
	
	public void setTargetY(int y) {
		Log.v(TAG, ("Setting player yTarget to " + y + " with yPos being " + yPos));
		targetY = y;
	}
	// END NEW MOVE SYSTEM //
	
	public void resetPlayer() {
		xPos = startingLocation.x;
		yPos = startingLocation.y;
		xPrev = startingLocation.x;
		yPrev = startingLocation.y;
		GameplayActivity.gravity = startingGravity;
		xVelocity = 0;
		yVelocity = 0;
		flipsLeft = baseFlipsLeft;
		Log.v(TAG, "Reset player at (" + xPos + "," + yPos + ") with gravity " + startingGravity);
	}
	
	public void applyGravity() {
		switch (GameplayActivity.gravity) {
		case LEFT:
			xVelocity -= .12;
			break;
		case UP:
			yVelocity -= .12;
			break;
		case RIGHT:
			xVelocity += .12;
			break;
		case DOWN:
			yVelocity += .12;
			break;
		}
	}
	
	public void changeGravityDirection(Direction direction) {
		
		if (direction != GameplayActivity.gravity && (GameplayActivity.supressPlayerFlips || flipsLeft > 0) ) {
			//If this function is called with supressPlayerFlips = true, we are touching a flipper
			Log.v(TAG, "Setting gravity direction to " + direction);
			gravityChangeSlowdown((GameplayActivity.supressPlayerFlips ? flipForcedVelocityDivisor : flipManualVelocityDivisor));
			switch (direction) {
			case DOWN:
				GameplayActivity.gravity = Direction.DOWN;
				break;
			case LEFT:
				GameplayActivity.gravity = Direction.LEFT;
				break;
			case UP:
				GameplayActivity.gravity = Direction.UP;
				break;
			case RIGHT:
				GameplayActivity.gravity = Direction.RIGHT;
				break;
			}
			currentSprite = GameplayActivity.sprites.player[GameplayActivity.gravity.toInt(GameplayActivity.gravity)];
			if (!GameplayActivity.autoPaused && !GameplayActivity.supressPlayerFlips) {
				setOnGroundState(false);
				flipsLeft -= flipCost;  //Deduct one allowed flip
			}
		}
	}
	
	public void gravityChangeSlowdown(double fraction) {
		Log.v(TAG, "Gravity change-induced slowdown; dividing by " + fraction);
		switch (GameplayActivity.gravity) {
		case DOWN:
			yVelocity /= fraction;
			break;
		case LEFT:
			xVelocity /= fraction;
			break;
		case UP:
			yVelocity /= fraction;
			break;
		case RIGHT:
			xVelocity /= fraction;
			break;
		}
	}
	
	public void setPosition(float x, float y, boolean setPrev) {
		
		if (v)
		{
			//Log.v(TAG, "xPrev: " + xPrev + ", yPrev: " + yPrev + ", xNew: " + x + ", yNew: " + y);
			v = false;
		}

		xPrev = xPos;
		yPrev = yPos;
		hasMoved = true;

		xPos = x;
		yPos = y;
	}
	
	public void setCollisionX(float x) {
		xPrev = xPos;
		xPos = x;
		//Log.d(TAG, "Collision X set to " + x);
	}
	
	public void setCollisionY(float y) {
		yPrev = yPos;
		yPos = y;
		//Log.d(TAG, "Collision Y set to " + y);
	}
	
	public boolean allowedToFlip() {
		return flipsLeft > 0;
	}
	
	public void consumeAllFlips() {
		flipsLeft = 0;
		
	}
	
	public boolean isOnGround() {
		return onGround;
	}
	

	public void movePlayer(/*Directions direction,*/ int touchPos) {
		touchPos += 1;  //To prevent divide-by-zero
		//Log.v(TAG, ("MovePlayer: touchPos = " + touchPos + ", screen thing is " + GameplayActivity.screenWidth/2));
		
		if (onGround) {
			Direction gravity = GameplayActivity.gravity;
			
			if (gravity == Direction.DOWN || gravity == Direction.UP) {
				
				if (touchPos < GameplayActivity.screenWidth / 2) {
					// Left  :: (screen width - touch position) / screen width * move speed
					float targetSpeed = (float)( ( (float)( (float)GameplayActivity.screenWidth-(float)touchPos)/(float)(GameplayActivity.screenWidth) )*moveSpeed);
					xVelocity = -targetSpeed;
					//Log.v(TAG, ("Target speed left (left/right in portrait): " + -targetSpeed));
					//Log.v(TAG, ("Calc: " + (float)((float)GameplayActivity.screenWidth-(float)touchPos) + "/" + (float)(GameplayActivity.screenWidth) + " * " + moveSpeed + " = " + targetSpeed));
				}
				else {  //touchPos >= screenWidth / 2
					// Right  :: touch position / screen width * move speed
					float targetSpeed = (float)((float)((float)touchPos/(float)GameplayActivity.screenWidth)*moveSpeed);
					xVelocity = targetSpeed;
					//Log.v(TAG, ("Target speed right (right/left in portrait): " + targetSpeed));
					//Log.v(TAG, ("Calc: " + touchPos + "/" + (GameplayActivity.screenWidth) + " * " + moveSpeed + " = " + targetSpeed));
				}
			} else {  //Directions.LEFT || Directions.RIGHT
				
				if (touchPos < GameplayActivity.screenHeight / 2) {
					// Top  :: (screen height - touch position) / screen height * move speed 
					float targetSpeed = (float)( ( (float)( (float)GameplayActivity.screenHeight-(float)touchPos)/(float)(GameplayActivity.screenHeight) )*moveSpeed);
					yVelocity = -targetSpeed;
					//Log.v(TAG, ("Target speed up (left/right in landscape): " + -targetSpeed));
					//Log.v(TAG, ("Calc: " + (float)((float)GameplayActivity.screenHeight-(float)touchPos) + "/" + (float)(GameplayActivity.screenHeight) + " * " + moveSpeed + " = " + targetSpeed));
				}
				else {  //touchPos >= screenHeight / 2
					// Bottom  :: touch position / screen height * move speed
					float targetSpeed = (float)((float)((float)touchPos/(float)GameplayActivity.screenHeight)*moveSpeed);
					yVelocity = targetSpeed;
					//Log.v(TAG, ("Target speed down (right/left in landscape): " + targetSpeed));
					//Log.v(TAG, ("Calc: " + touchPos + "/" + (GameplayActivity.screenHeight) + " * " + moveSpeed + " = " + targetSpeed));
				}
			}
		}
		
		
		/*   //ORIGINAL
		if (onGround && (GameplayActivity.gravity == Directions.DOWN || GameplayActivity.gravity == Directions.UP)) {
						
			if (direction == Directions.DOWN || direction == Directions.RIGHT) {
				// Right  :: touch position / screen width * move speed
				float targetSpeed = (float)((float)((float)touchPos/(float)GameplayActivity.screenWidth)*moveSpeed);
				xVelocity = targetSpeed;
				Log.v(TAG, ("(Direction "+direction+") Target speed right: " + targetSpeed));
				Log.v(TAG, ("Calc: " + touchPos + "/" + (GameplayActivity.screenWidth) + " * " + moveSpeed + " = " + targetSpeed));
			} else if (direction == Directions.LEFT || direction == Directions.UP) {
				// Left  :: (screen width - touch position) / screen width * move speed
				float targetSpeed = (float)( ( (float)( (float)GameplayActivity.screenWidth-(float)touchPos)/(float)(GameplayActivity.screenWidth) )*moveSpeed);
				xVelocity = -targetSpeed;
				Log.v(TAG, ("(Direction "+direction+") Target speed left: " + -targetSpeed));
				Log.v(TAG, ("Calc: " + (float)((float)GameplayActivity.screenWidth-(float)touchPos) + "/" + (float)(GameplayActivity.screenWidth) + " * " + moveSpeed + " = " + targetSpeed));
			}
		} else if (onGround && (GameplayActivity.gravity == Directions.LEFT || GameplayActivity.gravity == Directions.RIGHT)) {
			if (direction == Directions.DOWN || direction == Directions.RIGHT) {
				// Right
				yVelocity += moveSpeed;
			} else if (direction == Directions.LEFT || direction == Directions.UP) {
				// Left
				yVelocity -= moveSpeed;
			}
		}
		*/
	}
	
	public PointF getTopPosition() {
		return new PointF(xPos, yPos);
	}
	
	public PointF getBottomPosition() {
		return new PointF(xPos + spriteWidth, yPos + spriteHeight);
	}
	
	public int getType() {
		return 0;
	}
	
	public float getXVelocity() {
		return xVelocity;
	}
	
	public float getYVelocity() {
		return yVelocity;
	}
	
	public float getMaxSpeed() {
		return maxSpeed;
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}
	
	public void setXVelocity(float vel) {
		xVelocity = vel;
	}
	
	public void setYVelocity(float vel) {
		yVelocity = vel;
	}
	
	public void setOnGroundState(boolean state) {
		if (onGround != state)
		{
			onGround = state;
			if (onGround && !GameplayActivity.supressFlipCountReset)  //Reset flip amount if we've hit the ground, and are not being supressed
				flipsLeft = baseFlipsLeft;
		}

	}
	
	public int width() {
		return spriteWidth;
	}
	
	public int height() {
		return spriteHeight;
	}
	
	public float getXPrev() {
		return xPrev;
	}
	
	public float getYPrev() {
		return yPrev;
	}
	
	
	
	public boolean v(boolean s) {
		v = s;
		return true;
	}
}
