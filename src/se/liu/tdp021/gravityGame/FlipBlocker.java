package se.liu.tdp021.gravityGame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

public class FlipBlocker extends StaticGameObject {
	
	PointF point1, point2 = null;
	Paint temppaint = new Paint();
	

	public FlipBlocker(float x1, float y1, float x2, float y2) {
		point1 = new PointF(x1, y1);
		point2 = new PointF(x2 + 1, y2 + 1);
		
		temppaint.setARGB(170, 100, 100, 100);
		
		Log.v("Flipper", ("Flipper added at (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")"));
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(point1.x, point1.y, point2.x, point2.y, temppaint/*GameplayActivity.sprites.colours.get("blocker")*/);	
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
		return 0;
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
