package se.liu.tdp021.gravityGame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

public class Wall extends StaticGameObject {
	private static String TAG = "Wall";
	
	private PointF topPosition = null;
	private PointF bottomPosition = null;
	private String textureName = null;
	Paint defaultColour = null;
	Paint colour = null;
	
	
	public Wall(float topX, float topY, float bottomX, float bottomY, String textureName, Paint defaultColour, Paint colour) {
		
		topPosition = new PointF(topX, topY);
		bottomPosition = new PointF(bottomX + 1, bottomY + 1);
		
		this.textureName = textureName;
		this.defaultColour = defaultColour;
		this.colour = colour;

		
		Log.v("Wall", ("Wall added at (" + topX + "," + topY + ") to (" + bottomX + "," + bottomY + ")"));
	}
	
	public void update() {	}
	
	
	public void draw(Canvas targetCanvas) {
		/*TODO: Logic suggests that a wall stopping at y9 and another starting at y10 should not leave a gap,
		 * however in the beta this is not the case; there's a visible 1 pixel gap.
		 * While the collision is presumably fine, this looks quite ugly.
		 * Pixel examination of this glitch in the emulator shows that a 0-9 wall is only 9 pixels wide, not 10,
		 * so it appears to be an issue with Canvas.drawRect.
		 */
		
		if (colour != null) {
			targetCanvas.drawRect(topPosition.x, topPosition.y, bottomPosition.x, bottomPosition.y, colour);
		}
		if (textureName != null) {
			GameplayActivity.sprites.textures.get(textureName).setBounds((int) topPosition.x, (int) topPosition.y, (int) bottomPosition.x, (int) bottomPosition.y);
			GameplayActivity.sprites.textures.get(textureName).draw(targetCanvas);
		} else if (defaultColour != null) {
			targetCanvas.drawRect(topPosition.x, topPosition.y, bottomPosition.x, bottomPosition.y, GameplayActivity.sprites.colours.get("wall"));
		}
	}
	
	public PointF getTopPosition() {
		return topPosition;
	}
	
	public PointF getBottomPosition() {
		return bottomPosition;
	}
	
	public int getType() {
		return 1;
	}
	
	
	public int width() {
		return (int) (bottomPosition.x - topPosition.x);
	}
	
	public int height() {
		return (int) (bottomPosition.y - topPosition.y);
	}
}
