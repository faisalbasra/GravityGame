package se.liu.tdp021.gravityGame;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

public class Flipper extends StaticGameObject {
	PointF point1 = null;
	PointF point2 = null;
	int rotation = 0;

	public Flipper(float x1, float y1, float x2, float y2, int rot) {
		point1 = new PointF(x1, y1);
		point2 = new PointF(x2, y2);
		rotation = rot;
		
		Log.v("Flipper", ("Flipper added at (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ") with rotation " + rot));
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(GameplayActivity.sprites.gravSwitcher[rotation], point1.x, point1.y, null);
		//canvas.drawBitmap(point1.x, point1.y, GameplayActivity.sprites.gravSwitcher[rotation]);	
	}

	@Override
	public PointF getTopPosition() {
		return point1;
	}

	@Override
	public PointF getBottomPosition() {
		return point2;
	}

	@Override
	public int getType() {
		return 953;  //TODO: Remove this obsolete method; we are using InstanceOf anyway
	}

	@Override
	public int width() {
		return (int)(point2.x - point1.x);
	}

	@Override
	public int height() {
		return (int)(point2.y - point1.y);
	}

}
