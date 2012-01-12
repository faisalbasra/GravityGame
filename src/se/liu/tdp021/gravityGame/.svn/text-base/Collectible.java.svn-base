package se.liu.tdp021.gravityGame;

import android.graphics.Canvas;
import android.graphics.PointF;

public class Collectible extends StaticGameObject {
	
	public PointF point1, point2 = null;
	protected int ID = -1;
	public boolean collected = false;
	
	public Collectible(float x, float y) {
		point1 = new PointF(x, y);
		point2 = new PointF(x+GameplayActivity.sprites.collectible.getWidth(), y+GameplayActivity.sprites.collectible.getHeight());
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	public void collect() {
		collected = true;
	}
	
	public void restore() {
		collected = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if (!collected)
		canvas.drawBitmap(GameplayActivity.sprites.collectible, point1.x, point1.y, null);
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
		// TODO Proper value?
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
