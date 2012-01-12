package se.liu.tdp021.gravityGame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

public class Spike extends StaticGameObject {

	/*TODO: If we make it to Final:
	 * -Create spikes the same way as walls; (x1,y1) to (x2,y2), and repeat a 25x25 spike texture over the entire box
	 * -Allow recolouring of the spike if possible, using a tint value as extracted from Level.colours['spike']
	 */
	private PointF topPosition = null;
	private PointF bottomPosition = null;
	private int rotation = 0;
	
	private float xPos, yPos;
	private float width, height, length;
	
	private Paint tempPaint = new Paint();
	
	
	public Spike(float x1, float y1, float x2, float y2, float rot) {
		xPos = x1;
		yPos = y1;
		
		tempPaint.setARGB(255,255,0,0);
		
		topPosition = new PointF(x1, y1);
		if (rot % 2 == 0) //If 0 or 2, i.e. up or down
		{	width = x2-x1; height = 25.0f; // the spikes are x2-x1 wide and span horizontally
		}
		else
		{	width = 25.0f; height = y2-y1; } // the spikes are y2-y1 wide and span vertically
		bottomPosition = new PointF(x2, y2);
		
		rotation = (int) rot;  // Save the rotation for future checks
		
		Log.v("Spike", ("Spikes added at (" + x1 + "," + y1 + ") [to (" + bottomPosition.x + "," + bottomPosition.y + ") ], grounded in direction " + rotation));
	}
	


	@Override
	public void draw(Canvas canvas) {
		//canvas.drawRect(topPosition.x, topPosition.y, bottomPosition.x, bottomPosition.y, GameplayActivity.sprites.colours.get("spike"));
		if (rotation % 2 == 0) {
			int sprite_size = GameplayActivity.sprites.spike[rotation].getWidth();
			for (int i = 0; i < Math.floor(width/sprite_size); i++) {
				canvas.drawBitmap(GameplayActivity.sprites.spike[rotation], xPos+(sprite_size*i), yPos, null);
			}
			
		} else {
			int sprite_size = GameplayActivity.sprites.spike[rotation].getHeight();
			for (int i = 0; i < Math.floor(height/sprite_size); i++) {
				canvas.drawBitmap(GameplayActivity.sprites.spike[rotation], xPos, yPos+(sprite_size*i), null);
			}
		}
		canvas.drawBitmap(GameplayActivity.sprites.spike[rotation], xPos, yPos, null);
		//TODO: Final version: Make these tile based on a rectangle as defined by the level editor
		
		
		
		/*
		 * for (i = 0; i < floor(length/global.ANDROID_SPIKE_WIDTH); i+=1)
{
  if (rotation mod 2 == 0)  //Rotation-1 to counter the +1 indexing, see script:generateLevel comments for explaination
  draw_sprite_ext(sprite_index, -1, x1+(global.ANDROID_SPIKE_WIDTH*i), y1, 1, 1, 0, image_blend, 1);
  else
  draw_sprite_ext(sprite_index, -1, x1, y1+(global.ANDROID_SPIKE_WIDTH*i), 1, 1, 0, image_blend, 1);
}
		 * 
		 * 
		 * 
		 */

		
	}

	@Override
	public PointF getTopPosition() {
		return topPosition;
	}

	@Override
	public PointF getBottomPosition() {
		return bottomPosition;
	}

	@Override
	public int getType() {
		// TODO: MAKE SURE THE TYPE MATCHES WHAT A SPIKE IS SUPPOSED TO BE
		return 3;
	}

	@Override
	public int width() {
		return (int)width;
	}

	@Override
	public int height() {
		return (int)height;
	}

}
