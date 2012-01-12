package se.liu.tdp021.gravityGame;

import java.util.Vector;

import android.R.bool;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageBox extends StaticGameObject {	
	private static String TAG = "MessageBox";
	private PointF topPosition = null;
	private PointF bottomPosition = null;
	private String text = null;
	private Paint background = null;
	private Paint color = null;
	

	private TextView messageBox = null;
	
	private Bitmap textBox = null;
	
	int lineheight = 13;
	int letterWidth = 5;
	
	int maxRowLength = 30;

	private boolean isActive = false;
	
	public MessageBox(PointF topPoint, String message, Paint textColor, Paint backgroundColor, Direction direction) {

		Vector<String> rows = breakLines(message);
		int height = (rows.size() * lineheight) + 5;
		int width = maxRowLength * letterWidth;
	
		textBox = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	
		Canvas c = new Canvas(textBox);
		c.drawARGB(0, 0, 0, 0);
		

		backgroundColor.setStyle(Style.FILL);

		Rect rect = new Rect(0, 0, textBox.getWidth(), textBox.getHeight());
		
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(backgroundColor.getColor());
		c.drawRoundRect(new RectF(rect), 3, 3, p);
		
		int rowNum = 1;
		for (String row : rows) {
			c.drawText(row, 3, lineheight * rowNum, textColor);
			rowNum++;
		}
		
		Matrix mtx = new Matrix();
		
		if (direction == Direction.LEFT) { 
			mtx.postRotate(90);
		} else if (direction == Direction.RIGHT) { 
			mtx.postRotate(-90);
		} else if (direction == Direction.UP) { 
			mtx.postRotate(180);
		}
		
		textBox = Bitmap.createBitmap(textBox, 0, 0, textBox.getWidth(), textBox.getHeight(), mtx, true);

		topPosition = topPoint;
		bottomPosition = new PointF(topPosition.x + c.getWidth(), topPosition.y + c.getHeight());
		
		isActive = true;
	}
	
	
	
//	public MessageBox(float topX, float topY, String message, Paint textColor, Paint backgroundColor) {
//
//		text = message;
//		background = backgroundColor;
//		
//		messageBox = new TextView(GameplayActivity.context);
//		
//		messageBox.setMaxWidth(100);
//		messageBox.setMaxHeight(10);
//		
//		messageBox.setTextSize(15);
//		messageBox.setText(message);
//		messageBox.setTextColor(textColor.getColor());
//		
//		//messageBox.setWidth(40);
//		//messageBox.setHeight(10);
//	
//		
//		messageBox.setBackgroundColor(backgroundColor.getColor());
//		//LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 100);
//		//messageBox.setLayoutParams(layoutParams);
//		
//		//messageBox.setPadding(3, 3, 3, 3);
//		//messageBox.layout((int)topX, (int)topY, (int)(topX + messageBox.getWidth()), (int) (messageBox.getHeight()));
//		
//		messageBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT));
//		
//		topPosition = new PointF(topX, topY);
//		bottomPosition = new PointF(topX + messageBox.getWidth(), topY + messageBox.getHeight());
//	}

	
	public void activate() {
		isActive = true;
	}
	
	public void inactivate() {
		isActive = false;
	}
	
	
	public void setNewPosition(PointF newPos) {
		topPosition = newPos;
	}
	
	
	private Vector<String> breakLines(String string) {
		Vector<String> result = new Vector<String>();
		String[] words = string.split(" ");

		int length = 0;
		int numWords = 0;
		String line = "";
		for (String word : words) {
			// Add as many words as possible until maxRowLength is reached
			word = word.trim();
			length += word.length() + 1;
			if (length > maxRowLength) {
				length -= word.length() + 1;
				break;
			}
			line += word + " ";
			numWords++;
		}
		
		result.add(line);
		
		if (words.length - numWords < 1) {
			// Base case, we are done splitting
			return result;
		} else {
			// Split the rest of the string
			String[] theRest = new String[words.length - numWords];
			System.arraycopy(words, numWords, theRest, 0, words.length - numWords);
			result.addAll(breakLines(arrayToString(theRest, " ")));
		}
		return result;
	}
	
	
	private String arrayToString(String[] strings, String separator) {
		String result = "";
		
		for (String string : strings) {
			result += string + separator;
		}
		
		return result;
	}
	
	@Override
	public void draw(Canvas canvas) {
//		messageBox.draw(canvas);
		
		if (isActive) canvas.drawBitmap(textBox, topPosition.x, topPosition.y, null);
		
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
