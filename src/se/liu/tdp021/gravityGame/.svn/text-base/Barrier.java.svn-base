package se.liu.tdp021.gravityGame;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

public class Barrier extends StaticGameObject {
	
	public PointF topPosition = null;
	public PointF bottomPosition = null;
	public int rotation = 0;

	public Barrier(float x1, float y1, float x2, float y2, int rot) {
		topPosition = new PointF(x1, y1);
		bottomPosition = new PointF(x2 + 1, y2 + 1);
		rotation = rot;
		
		
		Log.v("Barrier", ("Barrier (solid side " + rotation + ") added at (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")"));
	}
	

	@Override
	public void draw(Canvas canvas) {
		switch (rotation) {
		case 0:
			//Solid side facing up (stand on top)
			canvas.drawRect(topPosition.x, topPosition.y, bottomPosition.x, topPosition.y+((bottomPosition.y - topPosition.y)/2), GameplayActivity.sprites.colours.get("wall"));
			canvas.drawRect(topPosition.x, topPosition.y+((bottomPosition.y - topPosition.y)/2), bottomPosition.x, bottomPosition.y, GameplayActivity.sprites.colours.get("barrier"));
			break;
		case 1:
			//Solid side facing left (stand on left)
			canvas.drawRect(topPosition.x+((bottomPosition.x - topPosition.x)/2), topPosition.y, bottomPosition.x, bottomPosition.y, GameplayActivity.sprites.colours.get("wall"));
			canvas.drawRect(topPosition.x, topPosition.y, topPosition.x+((bottomPosition.x - topPosition.x)/2), bottomPosition.y, GameplayActivity.sprites.colours.get("barrier"));
			break;
		case 2:
			//Solid side facing down (stand on underside)
			canvas.drawRect(topPosition.x, topPosition.y+((bottomPosition.y - topPosition.y)/2), bottomPosition.x, bottomPosition.y, GameplayActivity.sprites.colours.get("wall"));
			canvas.drawRect(topPosition.x, topPosition.y, bottomPosition.x, topPosition.y+((bottomPosition.y - topPosition.y)/2), GameplayActivity.sprites.colours.get("barrier"));
			break;
		case 3:
			//Solid side facing right (stand on right)
			canvas.drawRect(topPosition.x, topPosition.y, topPosition.x+((bottomPosition.x - topPosition.x)/2), bottomPosition.y, GameplayActivity.sprites.colours.get("wall"));
			canvas.drawRect(topPosition.x+((bottomPosition.x - topPosition.x)/2), topPosition.y, bottomPosition.x, bottomPosition.y, GameplayActivity.sprites.colours.get("barrier"));
			break;
		}
		
		

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
		
		return 5;  //TODO: Add correct type value; what are we enumerating against, and does it even matter anymore?
	}

	@Override
	public int width() {
		return (int) (bottomPosition.x - topPosition.x);
	}

	@Override
	public int height() {
		return (int) (bottomPosition.y - topPosition.y);
	}

}
