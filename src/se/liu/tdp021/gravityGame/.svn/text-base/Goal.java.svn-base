package se.liu.tdp021.gravityGame;

import android.graphics.Canvas;
import android.graphics.PointF;

public class Goal extends StaticGameObject {	

	public PointF topPosition = null;
	public PointF bottomPosition = null;
	
	public Goal(float x, float y) {
		topPosition = new PointF(x, y);
		bottomPosition = new PointF(x+GameplayActivity.sprites.goal.getWidth(), y+GameplayActivity.sprites.goal.getHeight());
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(GameplayActivity.sprites.goal, topPosition.x, topPosition.y, null);
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
		
		return 0;
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
